package controller;

import datastore.*;
import filters.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors; // Added for getWithdrawableApplications
import placement.*;
import user.Student;
import user.User;

/**
 * Controller responsible for handling all business logic for a Student user.
 * This includes viewing available internships, applying for them,
 * accepting offers, and withdrawing applications.
 *
 * @version 1.0
 */
public class StudentController {

    private final IDataStore store;
    private final Student currentStudent;
    /** A reusable filter for querying this student's applications. */
    private final ApplicationFilter applicationFilter;
    /** The filter criteria set by the user in the UI for their internship view. */
    private final InternshipFilter studentFilterPreference;

    /**
     * Constructs a new StudentController.
     *
     * @param inputStore The main {@link IDataStore} containing all application data.
     * @param inputSession The {@link Session} object, which must contain a logged-in {@link Student} user.
     * @throws IllegalArgumentException if the user in the session is not an instance of Student.
     */
    public StudentController(IDataStore inputStore, Session inputSession) {
        this.store = inputStore;

        User currentUser = inputSession.getCurrentUser();

        if (currentUser instanceof Student student) {
            this.currentStudent = student; // Save the student object
            this.applicationFilter = new ApplicationFilter(student.getUserId(), null, null);
            this.studentFilterPreference = new InternshipFilter();
            this.filterClear(); // Initialize the filter to default values
        } else {
            throw new IllegalArgumentException("This controller requires a Student user");
        }
    }


    /**
     * Gets a list of this student's applications, filtered by status.
     *
     * @param status The {@link ApplicationStatus} to filter by, or null for all.
     * @param filledCheck If {@code true}, applications for FILLED internships are excluded.
     * @return A list of {@link Application} objects.
     */
    public List<Application> getApplications(ApplicationStatus status, boolean filledCheck) {
        this.applicationFilter.setApplicationStatus(status);
        this.applicationFilter.setFilledCheck(filledCheck);
        return store.getApplicationStore().filter(this.applicationFilter);
    }


    /**
     * Gets a list of all applications this student can currently withdraw from.
     * Excludes applications that are already WITHDRAWN or UNSUCCESSFUL.
     *
     * @return A list of withdrawable {@link Application} objects.
     */
    public List<Application> getWithdrawableApplications() {
        this.applicationFilter.setApplicationStatus(null); // Get all
        this.applicationFilter.setFilledCheck(false); // Make sure to reset this
        List<Application> allApps = store.getApplicationStore().filter(this.applicationFilter);

        return allApps.stream()
                .filter(app -> !(app.getStatus() == ApplicationStatus.WITHDRAWN) &&
                        !(app.getStatus() == ApplicationStatus.UNSUCCESSFUL))
                .collect(Collectors.toList());
    }


    /**
     * Gets a list of all internships the student is eligible to apply for *right now*.
     * This applies strict, non-user-configurable business rules.
     *
     * @return A list of available {@link Internship} objects.
     */
    public List<Internship> getAvailableInternships() {
        InternshipFilter tempFilter = new InternshipFilter();

        tempFilter.setStatus(InternshipStatus.APPROVED);
        tempFilter.setStudentVisibility(true);
        tempFilter.setPreferredMajor(this.currentStudent.getMajor());
        tempFilter.setEarliestClosingDate(LocalDate.now());
        tempFilter.setLatestOpeningDate(LocalDate.now());

        if (this.currentStudent.getYear() < 3) {
            tempFilter.setLevel(InternshipLevel.BASIC);
        }

        return store.getInternshipStore().filter(tempFilter);
    }

    /**
     * Gets a list of internships based on the student's
     * saved filter preferences ({@link #studentFilterPreference}).
     *
     * @return A list of {@link Internship} objects matching the user's filter.
     */
    public List<Internship> getFilteredInternships() {
        InternshipFilter tempFilter = new InternshipFilter(this.studentFilterPreference);
        return store.getInternshipStore().filter(tempFilter);
    }

    /**
     * Creates a new application for an internship.
     *
     * @param internshipId The ID of the internship to apply for.
     * @return {@code true} if the application was created successfully,
     * {@code false} if the internship is not available or the student has already applied.
     */
    public boolean CreateApplications(int internshipId) {
        List<Internship> availableInternships = this.getAvailableInternships();

        boolean internshipExists = availableInternships.stream()
                .anyMatch(i -> i.getInternshipId() == internshipId);
        return internshipExists && this.newApplications(internshipId);
    }

    /**
     * Accepts a SUCCESSFUL internship offer.
     * This action sets the application status to ACCEPTED, increments the
     * internship's filled slot count, and forcibly withdraws all other
     * "withdrawable" applications for this student.
     *
     * @param applicationId The ID of the SUCCESSFUL application to accept.
     * @return {@code true} if successful, {@code false} if the application
     * was not found, not in SUCCESSFUL state, or the internship is already FILLED.
     */
    public boolean AcceptApplications(int applicationId) {
        Application accepted = store.getApplicationStore().getById(applicationId);
        if (accepted == null || accepted.getStatus() != ApplicationStatus.SUCCESSFUL || accepted.getInternship().getStatus() == InternshipStatus.FILLED) {
            return false; //
        }
        accepted.studentAccept();
        Internship acceptedInternship = accepted.getInternship();
        if (acceptedInternship != null) {
            acceptedInternship.addFilledSlot();
        }

        List<Application> applications = this.getWithdrawableApplications();
        for (Application app: applications){
            if (app.getApplicationID() != applicationId){
                app.forceWithdraw();
            }
        }
        return true;
    }

    /**
     * Submits a request to withdraw an application.
     * This sets a flag on the application that must be approved by Career Staff.
     *
     * @param applicationId The ID of the application to withdraw.
     * @return {@code true} if the request was submitted, {@code false} if the
     * application was not found, does not belong to the student, or is already
     * in a final state (UNSUCCESSFUL or WITHDRAWN).
     */
    public boolean WithdrawApplication(int applicationId) {
        Application appToWithdraw = store.getApplicationStore().getById(applicationId);

        if (appToWithdraw == null) return false;

        if (!appToWithdraw.getStudent().getUserId().equalsIgnoreCase(currentStudent.getUserId())) {
            return false;
        }
        ApplicationStatus status = appToWithdraw.getStatus();
        if (!(status == ApplicationStatus.UNSUCCESSFUL) && !(status == ApplicationStatus.WITHDRAWN)) {
            appToWithdraw.setStudentWithdrawalRequested();
            return true;
        }

        return false;
    }

    /**
     * Private helper to create and save a new Application object.
     *
     * @param inputIntern The ID of the internship to apply for.
     * @return {@code true} if the application was created, {@code false}
     * if an application for this internship already exists.
     */
    private boolean newApplications(int inputIntern) {
        this.applicationFilter.setApplicationStatus(null); // Check all
        this.applicationFilter.setFilledCheck(false);
        List<Application> applications = store.getApplicationStore().filter(this.applicationFilter);

        if (applications.stream().anyMatch(a -> a.getInternship().getInternshipId() == (inputIntern))) {
            return false; // Application already exists
        }
        int newId = store.getApplicationStore().generateId();
        Internship internship = store.getInternshipStore().getById(inputIntern);
        Application newApp = new Application(newId, this.currentStudent, internship);
        store.getApplicationStore().add(newApp);
        return true;
    }

    /**
     * Gets the {@link InternshipFilter} object used for the "View Internships" feature.
     * This allows the UI to modify the filter's properties directly.
     *
     * @return The {@link InternshipFilter} instance used by this controller.
     */
    public InternshipFilter getStudentFilterPreferences() {
        return this.studentFilterPreference;
    }

    /**
     * Resets the student's internship filter to the default, role-based criteria.
     * This applies all base-level constraints (APPROVED, visible, major, date, level).
     */
    public final void filterClear() {
        this.studentFilterPreference.reset();

        studentFilterPreference.setStatus(InternshipStatus.APPROVED);
        studentFilterPreference.setStudentVisibility(true);
        studentFilterPreference.setPreferredMajor(this.currentStudent.getMajor());
        studentFilterPreference.setEarliestClosingDate(LocalDate.now());
        studentFilterPreference.setLatestOpeningDate(LocalDate.now());

        if (this.currentStudent.getYear() < 3) {
            studentFilterPreference.setLevel(InternshipLevel.BASIC);
        }
    }
}
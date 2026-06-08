package controller;

import datastore.*;
import filters.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import placement.*;
import user.*;

/**
 * Controller responsible for handling all business logic for a Career Staff user.
 * This includes managing company representative approvals, internship approvals,
 * student withdrawal requests, and generating comprehensive reports.
 *
 * @version 1.0
 */
public class CareerStaffController {


    private IDataStore store;
    private Session session;
    /** The filter criteria used for generating reports. */
    private InternshipFilter reportFilter = new InternshipFilter();
    //private InternshipFilter pendingInternshipFilter = new InternshipFilter(InternshipStatus.PENDING, null, null, null, null, null, null, null);
    ApplicationFilter withdrawnApplicationFilter = new ApplicationFilter(null, null, ApplicationStatus.WITHDRAWN);

    /**
     * Constructs a new CareerStaffController.
     *
     * @param store The main {@link IDataStore} containing all application data.
     * @param sess The {@link Session} object, which must contain a logged-in {@link CareerStaff} user.
     * @throws IllegalArgumentException if the user in the session is not an instance of CareerStaff.
     */
    public CareerStaffController(IDataStore store, Session sess) {
        this.store = store;
        this.session = sess;

        User currentUser = session.getCurrentUser();

        if (!(currentUser instanceof CareerStaff)) {
            throw new IllegalArgumentException("This controller requires a Staff user");
        }
    }

    /**
     * Gets a list of all {@link CompanyRep} users with a PENDING status.
     *
     * @return A list of pending Company Representatives.
     */
    public List<CompanyRep> getPendingCompanyReps() {
        return store.getRepStore().getAll().stream()
                .filter(rep -> "PENDING".equals(rep.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * Gets a list of all {@link Internship} opportunities with a PENDING status.
     *
     * @return A list of pending Internships.
     */
    public List<Internship> getPendingInternships() {
        InternshipFilter tempfilter = new InternshipFilter(InternshipStatus.PENDING, null, null, null, null,null, null, null, null);
        return store.getInternshipStore().filter(tempfilter);
    }

    /**
     * Gets a list of all {@link Application} objects where the student has
     * requested withdrawal but it has not yet been processed by staff.
     *
     * @return A list of applications with pending withdrawal requests.
     */
    public List<Application> getWithdrawalRequests() {
        return store.getApplicationStore().getAll().stream()
                .filter(application -> application.isStudentWithdrawalRequested()
                        && !application.isCareerStaffWithdrawalAccepted())
                .collect(Collectors.toList());
    }

    /**
     * Approves a company representative's account.
     *
     * @param repId The ID of the {@link CompanyRep} to approve.
     * @return {@code true} if the rep was found and approved, {@code false} otherwise.
     */
    public boolean approveCompanyRep(String repId){
        CompanyRep rep = store.getRepStore().getById(repId);
        if (rep == null) return false;
        rep.setStatus("APPROVED"); //still need to check
        return true;
    }

    /**
     * Rejects a company representative's account.
     *
     * @param repId The ID of the {@link CompanyRep} to reject.
     * @return {@code true} if the rep was found and rejected, {@code false} otherwise.
     */
    public boolean rejectCompanyRep(String repId) {
        CompanyRep rep = store.getRepStore().getById(repId);
        if (rep == null) return false;
        rep.setStatus("REJECTED");
        return true;
    }

    /**
     * Approves a pending internship opportunity.
     * This also automatically toggles its visibility to 'true'.
     *
     * @param internshipId The ID of the {@link Internship} to approve.
     * @return {@code true} if the internship was found and approved, {@code false} otherwise.
     */
    public boolean approveInternship(int internshipId) {
        Internship internship = store.getInternshipStore().getById(internshipId);
        if (internship == null) return false;
        internship.markApproved();
        internship.toggleVisibility();
        return true;
    }

    /**
     * Rejects a pending internship opportunity.
     *
     * @param internshipId The ID of the {@link Internship} to reject.
     * @return {@code true} if the internship was found and rejected, {@code false} otherwise.
     */
    public boolean rejectInternship(int internshipId) {
        Internship internship = store.getInternshipStore().getById(internshipId);
        if (internship == null) return false;
        internship.markRejected();
        return true;
    }

    /**
     * Approves a student's request to withdraw an application.
     *
     * @param applicationId The ID of the {@link Application} to approve withdrawal for.
     * @return {@code true} if the application was found and had a pending request, {@code false} otherwise.
     */
    public boolean approveWithdrawal(int applicationId) {
        Application app = store.getApplicationStore().getById(applicationId);
        if (app != null && app.isStudentWithdrawalRequested()) {
            app.approveWithdrawal();
            return true;
        }
        return false;
    }

    /**
     * Generates a report (a list of internships) based on the currently
     * set {@link #reportFilter}.
     *
     * @return A list of {@link Internship} objects that match the filter.
     */
    public List<Internship> generateReport() {
        return store.getInternshipStore().filter(reportFilter);
    }

    /**
     * Gets all applications associated with a specific internship.
     *
     * @param internshipId The ID of the internship.
     * @return A list of {@link Application} objects for that internship.
     */
    public List<Application> getApplicationsForInternship(int internshipId) {
        ApplicationFilter filter = new ApplicationFilter(null, internshipId, null);
        return store.getApplicationStore().filter(filter);
    }

    /**
     * Sets the parameters for the internal report filter.
     *
     * @param status The status to filter by.
     * @param preferredMajor The major to filter by.
     * @param level The level to filter by.
     * @param beforeClosingDate The latest closing date.
     * @param afterClosingDate The earliest closing date.
     * @param visible The visibility to filter by.
     * @param companyName The company name to filter by.
     */
    public void setFilter(InternshipStatus status,
                          String preferredMajor,
                          InternshipLevel level,
                          LocalDate beforeClosingDate,
                          LocalDate afterClosingDate,
                          boolean visible,
                          String companyName){
        reportFilter.setStatus(status);
        reportFilter.setPreferredMajor(preferredMajor);
        reportFilter.setLevel(level);
        reportFilter.setEarliestClosingDate(beforeClosingDate); // Note: This seems swapped with latest
        reportFilter.setLatestClosingDate(afterClosingDate);   // Note: This seems swapped with earliest
        reportFilter.setStudentVisibility(visible);
        reportFilter.setCompanyName(companyName);
    }

    /**
     * Gets the {@link InternshipFilter} object used for generating reports.
     * This allows the UI to modify the filter's properties directly.
     *
     * @return The {@link InternshipFilter} instance used by this controller.
     */
    public InternshipFilter getReportFilter() {
        return reportFilter;
    }
}
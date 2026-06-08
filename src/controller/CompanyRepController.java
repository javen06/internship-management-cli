package controller;

import datastore.*;
import filters.ApplicationFilter;
import filters.InternshipFilter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import placement.*;
import user.CompanyRep;
import user.User;

/**
 * Controller for handling all business logic for a Company Representative user.
 * This class manages creating, editing, deleting, and viewing internships
 * and applications that belong to the currently logged-in representative.
 *
 * @version 1.0
 */
public class CompanyRepController {

    private final IDataStore store;
    private final Session session;
    private final CompanyRep currentRep;
    /** The maximum number of non-rejected internships a rep can have. */
    private static final int MAX_INTERNSHIPS_PER_REP = 5; //max intenships allowed

    /** The filter criteria set by the user in the UI for their internship view. */
    private InternshipFilter userInternshipViewfilter;

    /**
     * Constructs a new CompanyRepController.
     *
     * @param store The main {@link IDataStore} containing all application data.
     * @param session The {@link Session} object, which must contain a logged-in {@link CompanyRep} user.
     * @throws IllegalArgumentException if the user in the session is not an instance of CompanyRep.
     */
    public CompanyRepController(IDataStore store, Session session) {
        this.store = store;
        this.session = session;

        this.userInternshipViewfilter = new InternshipFilter();

        User u = session.getCurrentUser();
        if (u instanceof CompanyRep rep) {
            this.currentRep = rep;
        } else {
            throw new IllegalArgumentException("This controller requires a Company Representative user.");
        }
    }


    /**
     * Gets all internships created by this representative,
     * filtered by the user's saved filter preferences.
     *
     * @return A list of {@link Internship} objects.
     */
    public List<Internship> getMyInternships() {
        InternshipFilter tempFilter = new InternshipFilter(this.userInternshipViewfilter);
        tempFilter.setCompanyRepId(currentRep.getUserId());
        return store.getInternshipStore().filter(tempFilter);
    }

    /**
     * Gets all internships created by this representative with a specific status,
     * also filtered by the user's saved filter preferences.
     *
     * @param status The {@link InternshipStatus} to filter by (e.g., PENDING).
     * @return A list of {@link Internship} objects.
     */
    public List<Internship> getMyInternships(InternshipStatus status) {
        InternshipFilter tempFilter = new InternshipFilter(this.userInternshipViewfilter);
        tempFilter.setCompanyRepId(currentRep.getUserId());
        tempFilter.setStatus(status);
        return store.getInternshipStore().filter(tempFilter);
    }



    /**
     * Counts the number of "active" (PENDING or APPROVED) internships
     * for the current representative.
     *
     * @return The count of active internships.
     */
    private long getMyActiveInternshipCount() {
        // Create a new, blank filter to get ALL internships for this rep
        InternshipFilter allFilter = new InternshipFilter();
        allFilter.setCompanyRepId(currentRep.getUserId());
        List<Internship> allMyInternships = store.getInternshipStore().filter(allFilter);

        //count only active ones
        return allMyInternships.stream()
                .filter(i ->
                        i.getStatus() == InternshipStatus.PENDING ||
                                i.getStatus() == InternshipStatus.APPROVED
                )
                .count();
    }

    /**
     * Creates a new internship and submits it for approval.
     *
     * @param title The internship title.
     * @param description The internship description.
     * @param preferredMajor The preferred major.
     * @param level The {@link InternshipLevel}.
     * @param maxSlot The number of available slots.
     * @param openDate The application opening date.
     * @param closeDate The application closing date.
     * @return {@code true} if the internship was created, {@code false} if the
     * representative has reached the {@value #MAX_INTERNSHIPS_PER_REP} limit.
     */
    public boolean createInternship(String title, String description, String preferredMajor,
                                    InternshipLevel level, int maxSlot, LocalDate openDate, LocalDate closeDate) {

        //only allow max 5 internships
        long currentCount = getMyActiveInternshipCount();
        if (currentCount >= MAX_INTERNSHIPS_PER_REP) {
            System.out.println("You have reached the maximum number of internship opportunities (" + MAX_INTERNSHIPS_PER_REP + ").");
            return false;
        }
        int newId = store.getInternshipStore().generateId();

        Internship internship = new Internship(newId, title, description, preferredMajor, level,
                currentRep, currentRep.getCompanyName(), maxSlot, openDate, closeDate); //visible automatically false & status PENDING by default

        store.getInternshipStore().add(internship); //save to datastore
        return true;
    }

    /**
     * Gets all applications for a single internship,
     * provided it belongs to the current representative.
     *
     * @param internshipId The ID of the internship to check.
     * @return A list of {@link Application} objects, or an empty list
     * if the internship is not found or does not belong to the rep.
     */
    public List<Application> getApplicationsByInternship(int internshipId) {
        Internship internship = store.getInternshipStore().getById(internshipId);
        if (internship == null || !isMyInternship(internship)) {
            return List.of();
        }
        //create application filter and return all applications for that intenrship
        ApplicationFilter filter = new ApplicationFilter(null, internshipId, null);
        return store.getApplicationStore().filter(filter);
    }

    /**
     * Approves an application, setting its status to SUCCESSFUL.
     *
     * @param applicationId The ID of the application to approve.
     * @return {@code true} if successful, {@code false} if the application
     * or internship is not found, doesn't belong to the rep, or is already filled.
     */
    public boolean approveApplication(int applicationId) {
        Application app = store.getApplicationStore().getById(applicationId);
        if (app == null) return false;
        // must belong to this rep
        if (!isMyInternship(app.getInternship())) return false;
        if (app.getInternship().getStatus() == InternshipStatus.FILLED) return false;
        app.setSuccessful();
        return true;
    }

    /**
     * Rejects an application, setting its status to UNSUCCESSFUL.
     *
     * @param applicationId The ID of the application to reject.
     * @return {@code true} if successful, {@code false} if the application
     * is not found or does not belong to the rep.
     */
    public boolean rejectApplication(int applicationId) {
        Application app = store.getApplicationStore().getById(applicationId);
        if (app == null) return false;

        if (!isMyInternship(app.getInternship())) return false;

        app.setUnsuccessful();
        return true;
    }

    /**
     * Toggles the visibility of an internship for students.
     *
     * @param internshipId The ID of the internship to toggle.
     * @return {@code true} if successful, {@code false} if the internship
     * is not found or does not belong to the rep.
     */
    public boolean toggleVisibility(int internshipId) {
        Internship internship = store.getInternshipStore().getById(internshipId);
        if (internship == null) return false;
        if (!isMyInternship(internship)) return false;

        internship.toggleVisibility();
        return true;
    }


    /**
     * Helper method to check if an internship belongs to the
     * currently logged-in representative.
     *
     * @param internship The {@link Internship} to check.
     * @return {@code true} if the internship's rep ID matches the current user's ID.
     */
    private boolean isMyInternship(Internship internship) {
        if (internship.getCompanyRep() != null) {
            return internship.getCompanyRep().getUserId().equalsIgnoreCase(currentRep.getUserId());//return true if internship rep and current logged in rep is the same
        }
        //fallback: compare by company name if rep object was not set
        return internship.getCompanyName() != null
                && internship.getCompanyName().equalsIgnoreCase(currentRep.getCompanyName());
    }


    /**
     * Gets all applications for all internships belonging
     * to the current representative.
     *
     * @return A list of all {@link Application} objects.
     */
    public List<Application> getAllApplicationsForMyInternships() {
        List<Integer> myInternshipIds = getMyInternships().stream()
                .map(Internship::getInternshipId)
                .collect(Collectors.toList());

        return store.getApplicationStore().getAll().stream()
                .filter(a -> myInternshipIds.contains(a.getInternship().getInternshipId()))
                .collect(Collectors.toList());
    }

    /**
     * Gets the {@link InternshipFilter} object used for the "My Internships" view.
     * This allows the UI to modify the filter's properties directly.
     *
     * @return The {@link InternshipFilter} instance used by this controller.
     */
    public InternshipFilter getuserInternshipViewfilter() {
        return userInternshipViewfilter;
    }


    /**
     * Retrieves an internship for editing, but only if it is
     * in PENDING status and belongs to the current rep.
     * Prints error messages to console if validation fails.
     *
     * @param internshipId The ID of the internship to retrieve.
     * @return The {@link Internship} object if editable, or null otherwise.
     */
    public Internship getEditableInternshipById(int internshipId) {
        Internship internship = store.getInternshipStore().getById(internshipId);

        if (internship == null) {
            System.out.println("Error: Internship with ID " + internshipId + " not found.");
            return null;
        }
        if (!isMyInternship(internship)) {
            System.out.println("Error: You do not have permission to edit this internship.");
            return null;
        }
        if (internship.getStatus() != InternshipStatus.PENDING) {
            System.out.println();
            System.out.println("Error: This internship is not in PENDING state and cannot be edited.");
            System.out.println("Its current status is: " + internship.getStatus());
            return null;
        }

        return internship;
    }



    /**
     * Updates the details of a PENDING internship.
     * Input strings of "-" are ignored.
     *
     * @param internshipId The ID of the internship to update.
     * @param titleInput The new title, or "-".
     * @param descInput The new description, or "-".
     * @param majorInput The new major, or "-".
     * @param levelInput The new level (as a String), or "-".
     * @param slotInput The new slot count (as a String), or "-".
     * @param openDateInput The new open date (as a String), or "-".
     * @param closeDateInput The new close date (as a String), or "-".
     * @return {@code true} if successful, {@code false} if validation fails
     * (e.g., bad date format, not PENDING status).
     */
    public boolean updateInternship(int internshipId, String titleInput, String descInput,
                                    String majorInput, String levelInput, String slotInput,
                                    String openDateInput, String closeDateInput) {

        Internship internship = store.getInternshipStore().getById(internshipId);
        System.out.println();
        if (internship == null) {
            System.out.println("Error: Internship with ID " + internshipId + " not found.");
            return false;
        }
        if (!isMyInternship(internship)) {
            System.out.println("Error: You do not have permission to edit this internship.");
            return false;
        }
        if (internship.getStatus() != InternshipStatus.PENDING) {
            System.out.println("Error: This internship have already been reviewed by staff and cannot be edited.");
            return false;
        }

        InternshipLevel newLevel = null;
        int newSlot = -1;
        LocalDate newOpenDate = null;
        LocalDate newCloseDate = null;

        try {
            if (!levelInput.equals("-")) {
                newLevel = InternshipLevel.valueOf(levelInput.toUpperCase());
            }
            if (!slotInput.equals("-")) { // Small bug: doesn't check for > 0 here
                newSlot = Integer.parseInt(slotInput);
                if (newSlot <= 0) {
                    System.out.println("Error: Slot must be a positive integer.");
                    return false;
                }
            }
            if (!openDateInput.equals("-")) {
                newOpenDate = LocalDate.parse(openDateInput);
            }
            if (!closeDateInput.equals("-")) {
                newCloseDate = LocalDate.parse(closeDateInput);
            }
        } catch (IllegalArgumentException | DateTimeParseException e) {
            System.out.println("Error: Invalid input. Level, Slot or Date format is incorrect.");
            System.out.println("Please ensure level is (BASIC/INTERMEDIATE/ADVANCED), slot is positive integer and dates are (yyyy-MM-dd).");
            return false;
        }

        //Apply all filters after passing checks

        if (!titleInput.equals("-")) {
            internship.setTitle(titleInput);
        }
        if (!descInput.equals("-")) {
            internship.setDescription(descInput);
        }
        if (!majorInput.equals("-")) {
            internship.setPreferredMajor(majorInput);
        }
        if (newLevel != null) {
            internship.setLevel(newLevel);
        }
        if (newSlot != -1) {
            internship.setTotalSlots(newSlot);
        }
        if (newOpenDate != null) {
            internship.setOpenDate(newOpenDate);
        }
        if (newCloseDate != null) {
            internship.setCloseDate(newCloseDate);
        }

        return true;
    }

    /**
     * Deletes an internship, but only if it is in PENDING status.
     *
     * @param internshipId The ID of the internship to delete.
     * @return {@code true} if successful, {@code false} if the internship
     * is not found or is not in PENDING status.
     */
    public boolean deletePendingInternship(int internshipId) {
        Internship internshipToDelete = getEditableInternshipById(internshipId);
        if (internshipToDelete == null) {
            // Error message is printed by getEditableInternshipById
            return false;
        }
        return store.getInternshipStore().removeById(internshipId);
    }
}
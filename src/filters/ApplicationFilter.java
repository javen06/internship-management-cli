package filters;

import placement.Application;
import placement.ApplicationStatus;
import placement.InternshipStatus;

/**
 * Implements {@link FilterCriteria} for {@link Application} objects.
 * This class holds specific criteria (like student ID, internship ID, or status)
 * and checks if a given Application object matches them.
 *
 * @version 1.0
 */
public class ApplicationFilter implements FilterCriteria<Application> {
    private String studentID;
    private Integer internshipID; //integer allows for null values
    private ApplicationStatus status;
    private boolean filledCheck;
    //private String titleContains;
    //i think only these 3 for now, can add later on if not

    /**
     * Constructs a new ApplicationFilter with specified criteria.
     * Null values for any parameter will cause that criterion to be ignored.
     *
     * @param sid The student ID to filter by, or null to allow all students.
     * @param iid The internship ID to filter by, or null to allow all internships.
     * @param s The {@link ApplicationStatus} to filter by, or null to allow all statuses.
     */
    public ApplicationFilter(String sid, Integer iid, ApplicationStatus s){
        this.studentID = sid;
        this.internshipID = iid;
        this.status = s;
        filledCheck = false;
    }

    /**
     * Checks if a given {@link Application} object matches all set criteria.
     *
     * @param item The Application object to test.
     * @return {@code true} if the item matches all non-null criteria,
     * {@code false} otherwise.
     */
    @Override
    public boolean matches(Application item) {
        if (studentID != null && !item.getStudent().getUserId().equalsIgnoreCase(studentID)) return false;
        if (internshipID != null && item.getInternship().getInternshipId() != internshipID) return false;
        if (status != null && item.getApplicationStatus() != status) return false;
        if (filledCheck == true && item.getInternship().getStatus().equals(InternshipStatus.FILLED)) return false;
        return true;
    }

    /**
     * Sets or clears the filter for application status.
     *
     * @param status The {@link ApplicationStatus} to filter for, or null to clear.
     */
    public void setApplicationStatus(ApplicationStatus status) {
        this.status = status;
    }

    /**
     * Sets whether to filter out applications for internships that are FILLED.
     *
     * @param filledCheck {@code true} to hide applications for filled internships,
     * {@code false} to include them.
     */
    public void setFilledCheck(boolean filledCheck){
        this.filledCheck = filledCheck;
    }
}
//TO IMPLEMENT!!
//CONSIDER USING java BUILDER
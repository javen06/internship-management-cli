package placement;

/**
 * Represents the lifecycle status of an {@link Internship} opportunity.
 * This status is managed by both {@link user.CompanyRep} (submission)
 * and {@link user.CareerStaff} (approval/rejection).
 *
 * @version 1.0
 */
public enum InternshipStatus {
    /**
     * The internship has been submitted by a Company Rep but not yet
     * reviewed by Career Staff.
     */
    PENDING,

    /**
     * The internship has been approved by Career Staff and is now
     * potentially visible to eligible students.
     */
    APPROVED,

    /**
     * The internship was rejected by Career Staff and is not visible.
     */
    REJECTED,

    /**
     * All available slots for the internship have been filled and
     * accepted by students.
     */
    FILLED
}
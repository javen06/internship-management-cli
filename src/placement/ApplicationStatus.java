package placement;

/**
 * Represents the status of a student's {@link Application}.
 *
 * @version 1.0
 */
public enum ApplicationStatus{
    /**
     * The application has been submitted by the student but not yet
     * reviewed by the Company Representative.
     */
    PENDING,

    /**
     * The Company Representative has approved the application,
     * and it is now an "offer" waiting for the student's final acceptance.
     */
    SUCCESSFUL,

    /**
     * The Company Representative has rejected the application.
     */
    UNSUCCESSFUL,

    /**
     * The application has been withdrawn. This can happen automatically
     * (if the student accepts another offer) or manually
     * (if the student requests withdrawal and it is approved).
     */
    WITHDRAWN,

    /**
     * The student has formally accepted a SUCCESSFUL offer.
     * This fills a slot in the internship.
     */
    ACCEPTED
}
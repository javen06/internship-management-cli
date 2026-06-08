package user;

/**
 * Represents the approval status of a Company Representative account.
 * This status is managed by the CareerStaff.
 *
 * @version 1.0
 */
public enum CompanyRepStatus {
    /**
     * The account has been registered but not yet reviewed by Career Staff.
     * The user cannot log in.
     */
    PENDING,

    /**
     * The account has been approved by Career Staff.
     * The user can now log in and access system features.
     */
    APPROVED,

    /**
     * The account has been rejected by Career Staff.
     * The user cannot log in.
     */
    REJECTED
}
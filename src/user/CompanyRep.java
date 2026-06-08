package user;

/**
 * Represents a Company Representative user.
 * This class extends {@link User} and includes details about the
 * representative's company and their account approval status ({@link CompanyRepStatus}).
 *
 * @version 1.0
 */
public class CompanyRep extends User {
    private String position;
    private String companyName;
    private String department;
    private String email;
    private CompanyRepStatus status;

    /**
     * Constructs a new Company Representative.
     * The user's ID is expected to be their email address.
     *
     * @param id The representative's ID (typically their email).
     * @param password The representative's password.
     * @param name The representative's full name.
     * @param companyName The name of the company they represent.
     * @param department The department they work in.
     * @param position Their job title or position.
     * @param email Their email address.
     * @param status The account status as a String (e.g., "PENDING"),
     * which will be converted to {@link CompanyRepStatus}.
     */
    public CompanyRep(String id, String password, String name,
                      String companyName, String department,
                      String position, String email, String status) {
        super(id, password, name);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.email = email;
        this.status = CompanyRepStatus.valueOf(status);
    }

    /**
     * Gets the company name.
     * @return The company name.
     */
    public String getCompanyName() { return companyName; }

    /**
     * Gets the representative's department.
     * @return The department name.
     */
    public String getDepartment() { return department; }

    /**
     * Gets the representative's email.
     * @return The email address.
     */
    public String getEmail() { return email; }

    /**
     * Gets the account status as a String.
     * @return The name of the enum (e.g., "PENDING", "APPROVED").
     */
    public String getStatus() { return status.name(); }

    /**
     * Sets the representative's account status from a String.
     *
     * @param status The new status as a String (e.g., "APPROVED").
     * @return {@code true} if the status was successfully parsed and set,
     * {@code false} if the String was not a valid {@link CompanyRepStatus}.
     */
    public boolean setStatus(String status) {
        try {
            this.status = CompanyRepStatus.valueOf(status);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the account status is {@link CompanyRepStatus#APPROVED}.
     *
     * @return {@code true} if the status is APPROVED, {@code false} otherwise.
     */
    public boolean isApproved() {
        return status != null && (status == CompanyRepStatus.APPROVED);
    }

    /**
     * Checks if the account status is {@link CompanyRepStatus#REJECTED}.
     *
     * @return {@code true} if the status is REJECTED, {@code false} otherwise.
     */
    public boolean isRejected() {
        return status != null && (status == CompanyRepStatus.REJECTED);
    }

    /**
     * Returns a string representation of the Company Representative for display lists.
     *
     * @return A string in the format "Name (ID) - CompanyName [Status]".
     */
    @Override
    public String toString() {
        return getUserName() + " (" + getUserId() + ") - " + companyName + " [" + status + "]";
    }
}
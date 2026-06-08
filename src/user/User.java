package user;

/**
 * An abstract base class representing a user in the system.
 * This class stores common information shared by all user types,
 * such as ID, password, and name.
 * <p>
 * Specific user types (e.g., {@link Student}, {@link CompanyRep}, {@link CareerStaff})
 * must extend this class.
 *
 * @version 1.0
 */
public abstract class User {
    private String userID;
    private String password;
    private String name;

    /**
     * Constructs a new User.
     *
     * @param userID The unique identifier for the user.
     * @param password The user's password (stored as plaintext).
     * @param name The user's full name.
     */
    public User(String userID, String password, String name) {
        this.userID = userID;
        this.password = password;
        this.name = name;
    }

    /**
     * Gets the user's unique ID.
     *
     * @return The user's ID string.
     */
    public String getUserId() { return userID; }

    /**
     * Gets the user's current password.
     *
     * @return The user's password string.
     */
    public String getPassword() { return password; }

    /**
     * Gets the user's full name.
     *
     * @return The user's name string.
     */
    public String getUserName() { return name; }

    /**
     * Updates the user's password.
     *
     * @param newPassword The new password to set.
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Returns a string representation of the user, primarily for display lists.
     *
     * @return A string in the format "userID - name".
     */
    @Override
    public String toString() {
        return userID + " - " + name;
    }
}
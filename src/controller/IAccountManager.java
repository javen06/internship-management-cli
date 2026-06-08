package controller;

import user.User;

/**
 * Interface defining the contract for account management operations.
 * This is implemented by {@link AuthController} and used by UI classes
 * to manage passwords and get session details without coupling
 * to the AuthController implementation.
 *
 * @version 1.0
 */
public interface IAccountManager {
    /**
     * Changes the password for the currently logged-in user.
     *
     * @param oldPw The user's current (old) password.
     * @param newPw The new password to set.
     * @return {@code true} if the password was successfully changed,
     * {@code false} otherwise (e.g., old password mismatch).
     */
    boolean changePassword(String oldPw, String newPw);

    /**
     * Gets the full name of the currently logged-in user.
     *
     * @return The user's name (e.g., "John Doe").
     */
    String getCurrentUserName();

    /**
     * Gets the unique ID of the currently logged-in user.
     *
     * @return The user's ID (e.g., "U1234567F" or "john@company.com").
     */
    String getCurrentUserId();

    /**
     * Gets the {@link User} object for the currently logged-in user.
     *
     * @return The {@link User} object.
     */
    User getCurrentUser();
}
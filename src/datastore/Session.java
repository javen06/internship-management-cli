package datastore;

import user.User;

/**
 * Manages the current user session.
 * This class holds a reference to the currently logged-in {@link user.User},
 * effectively managing the application's authentication state.
 *
 * @version 1.0
 */
public class Session {
    /** The user object for the currently logged-in user, or null if no one is logged in. */
    private User currentUser;

    /**
     * Sets the currently logged-in user.
     * This is typically called by the {@link controller.AuthController}
     * upon successful login.
     *
     * @param user The user who has successfully logged in.
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Gets the currently logged-in user.
     *
     * @return The {@link User} object for the current session, or null if logged out.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return {@code true} if currentUser is not null, {@code false} otherwise.
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Logs out the current user by setting the internal user reference to null.
     */
    public void logout() {
        currentUser = null;
    }
}
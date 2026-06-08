package controller;

import datastore.*;
import java.util.Scanner;
import user.*;

/**
 * Controller responsible for handling user authentication logic.
 * This class implements the {@link IAccountManager} interface, providing
 * services for login, logout, password changes, and retrieving
 * information about the currently logged-in user.
 *
 * @version 1.0
 */
public class AuthController implements IAccountManager {

    private final IDataStore store;
    private final Session session;

    /**
     * Constructs a new AuthController.
     *
     * @param store The main {@link IDataStore} containing all user data.
     * @param session The {@link Session} object to manage the current user's state.
     */
    public AuthController(
            IDataStore store,
            Session session
    ) {
        this.store = store;
        this.session = session;
    }

    // ===================================
    // LOGIN
    // ===================================

    /**
     * Authenticates a user based on their chosen role, ID, and password.
     * On success, the user is set in the {@link Session}.
     * On failure, an appropriate error message is printed.
     *
     * @param choice The user's selected role (1=Student, 2=CompanyRep, 3=CareerStaff).
     * @param id The user's ID.
     * @param password The user's plaintext password.
     * @return {@code true} if authentication is successful, {@code false} otherwise.
     */
    public boolean autheticate(int choice, String id, String password) {

        User user = null;
        String role = "";

        switch (choice) {
            case 1 -> {
                Student s = store.getStudentStore().getById(id);
                if (s != null && s.getPassword().equals(password)) {
                    user = s;
                    role = "Student";
                }
            }
            case 2 -> {
                CompanyRep c = store.getRepStore().getById(id);
                if (c != null && c.getPassword().equals(password)) {
                    if (!c.isApproved()) {
                        if (c.isRejected()) {
                            System.out.println("This company representative account has been rejected.");
                            return false;
                        }else{
                            System.out.println("This company representative account is not active (pending approval).");
                            return false;
                        }
                    }
                    user = c;
                    role = "Company Representative";
                }
            }
            case 3 -> {
                CareerStaff cs = store.getStaffStore().getById(id);
                if (cs != null && cs.getPassword().equals(password)) {
                    user = cs;
                    role = "Career Staff";
                }
            }
            default -> {
                System.out.println("Invalid choice.");
            }
        }

        if (user != null) {
            session.setCurrentUser(user);
            System.out.println("Login successful! Welcome, " + user.getUserName() + " (" + role + ")");
            return true;
        }

        System.out.println("Invalid ID or password.");
        return false;
    }

    // ===================================
    // LOGOUT
    // ===================================

    /**
     * Logs out the currently active user by clearing the session.
     */
    public void logout() {
        if (session.isLoggedIn()) {
            session.logout();
            System.out.println("Successfully logged out.");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    // ===================================
    // SHOW CURRENT USER
    // ===================================

    /**
     * Prints the details of the currently logged-in user to the console.
     */
    public void showCurrentUser() {
        if (session.isLoggedIn()) {
            User u = session.getCurrentUser();
            System.out.println("Currently logged in as: " + u.getUserName() + " (" + u.getUserId() + ")");
        } else {
            System.out.println("No user logged in.");
        }
    }

    /**
     * {@inheritDoc}
     * Changes the password for the currently logged-in user.
     *
     * @param oldPw The user's current password.
     * @param newPw The new password to set.
     * @return {@code true} if the password was changed successfully,
     * {@code false} if the user is not logged in, the old password
     * does not match, or the new password is the same as the old one.
     */
    @Override
    public boolean changePassword(String oldPw, String newPw) {

        User u = session.getCurrentUser();

        if (u == null) {
            System.out.println("You are not logged in.");
            return false;
        }

        if (!u.getPassword().equals(oldPw)) {
            System.out.println("Old password does not match.");
            return false;
        }

        if (u.getPassword().equals(newPw)) {
            System.out.println("New password is same as old password.");
            return false;
        }

        u.setPassword(newPw);
        System.out.println("Password changed successfully!");
        return true;
    }

    /**
     * {@inheritDoc}
     * @return The name of the currently logged-in user.
     */
    @Override
    public String getCurrentUserName(){
        return session.getCurrentUser().getUserName();
    }

    /**
     * {@inheritDoc}
     * @return The ID of the currently logged-in user.
     */
    @Override
    public String getCurrentUserId(){
        return session.getCurrentUser().getUserId();
    }

    /**
     * {@inheritDoc}
     * @return The {@link User} object for the currently logged-in user.
     */
    @Override
    public User getCurrentUser(){
        return session.getCurrentUser();
    }

    /**
     * Gets the {@link UserStore} for Company Representatives.
     * This is used by {@link boundary.AuthUI} to add new reps during registration.
     *
     * @return The CompanyRep user store.
     */
    public UserStore<CompanyRep>  getRepStore(){
        return store.getRepStore();
    }
}
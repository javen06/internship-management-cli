package datastore;

import java.util.ArrayList;
import java.util.List;
import user.*;

/**
 * A generic repository for storing and retrieving objects
 * that extend the {@link User} class.
 * This class provides basic CRUD (Create, Read, Update, Delete) operations
 * for user objects of a specific type (e.g., Student, CompanyRep).
 *
 * @version 1.0
 * @param <T> The specific type of {@link User} this store will manage.
 */
public class UserStore <T extends User> {

    /** The list holding all user objects of type T. */
    List<T> userList = new ArrayList<>();

    /**
     * Finds a user by their unique ID.
     * The search is case-insensitive.
     *
     * @param userId The ID of the user to find.
     * @return The user object (T) if found, or null otherwise.
     */
    public T getById(String userId){
        for (T user : userList) {
            if (user.getUserId().equalsIgnoreCase(userId)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Adds a new user to the store.
     *
     * @param user The user object (T) to add.
     */
    public void add(T user){
        userList.add(user);
    }

    /**
     * Removes a user from the store by their unique ID.
     * The search is case-insensitive.
     *
     * @param userId The ID of the user to remove.
     * @return {@code true} if a user was found and removed,
     * {@code false} otherwise.
     */
    public boolean removeById(String userId){
        for (T user : userList) {
            if (user.getUserId().equalsIgnoreCase(userId)) {
                userList.remove(user);
                return true;
            }
        }
        return false;
    }


    /**
     * Gets the internal list of all users.
     * @return The internal list of all users of type T.
     */
    public List<T> getAll() {
        return this.userList;
    }
}
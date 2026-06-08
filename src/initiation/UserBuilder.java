//package initiation;
//
//public abstract class UserBuilder<T extends UserBuilder<T>> {
//    protected String id;
//    protected String password;
//    protected String name;
//
//    @SuppressWarnings("unchecked")
//    public T setId(String id) {
//        this.id = id;
//        return (T) this;
//    }
//
//    @SuppressWarnings("unchecked")
//    public T setPassword(String password) {
//        this.password = password;
//        return (T) this;
//    }
//
//    @SuppressWarnings("unchecked")
//    public T setName(String name) {
//        this.name = name;
//        return (T) this;
//    }
//
//    public abstract User build();
//}

package initiation;

import java.util.List;
import user.User;

/**
 * An abstract generic builder for creating lists of {@link User} objects
 * from a CSV file.
 * <p>
 * This class defines the common contract for all specific user builders
 * (e.g., {@link StudentBuilder}, {@link CompanyRepBuilder}).
 *
 * @version 1.0
 * @param <T> The specific type of User this builder creates (e.g., Student, CompanyRep).
 */
public abstract class UserBuilder<T extends User> {
    /**
     * Abstract method to be implemented by concrete builders.
     * Reads a CSV file from the given path and constructs a list
     * of user objects of type T.
     *
     * @param csvPath The absolute file path to the CSV data file.
     * @return A list of {@link User} objects of the specified type T.
     */
    public abstract List<T> build(String csvPath);
}
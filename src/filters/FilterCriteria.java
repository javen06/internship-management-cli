package filters;

/**
 * A generic interface defining a contract for filter criteria classes.
 * Any class that implements this interface can be used to test if
 * an object of type T "matches" its criteria.
 *
 * @version 1.0
 * @param <T> The type of object that this filter can test.
 */
public interface FilterCriteria<T> {
    /**
     * Checks if the given item matches the criteria defined by the
     * implementing class.
     *
     * @param item The object to test.
     * @return {@code true} if the item matches the criteria, {@code false} otherwise.
     */
    boolean matches(T item);
}
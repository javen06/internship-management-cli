package filters;
import java.util.List;

/**
 * A generic interface for classes (like stores or collections)
 * that support being filtered by a {@link FilterCriteria}.
 *
 * @version 1.0
 * @param <T> The type of object contained in the filterable collection.
 */
public interface IFilterable<T> {
    /**
     * Filters the collection based on the provided criteria.
     *
     * @param fCriteria An object implementing {@link FilterCriteria}
     * which defines the matching logic.
     * @return A new {@link List} containing only the items of type T
     * that matched the criteria.
     */
    List<T> filter (FilterCriteria<T> fCriteria);
}
package datastore;
import filters.IFilterable;
import filters.FilterCriteria;

import placement.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A repository for {@link placement.Application} objects.
 * Implements {@link filters.IFilterable} to allow for filtering applications.
 * This class also handles the generation of unique IDs for new applications.
 *
 * @version 1.0
 */
public class ApplicationStore implements IFilterable<Application>{
    //attributes
    /** The list holding all Application objects. */
    private List<Application> applications = new ArrayList<Application>();
    /** The counter for generating new application IDs. */
    private int idGenerationIndex = 1;  //very smart name

    //functions
    /**
     * Sets the starting ID for the ID generator.
     * This is used during initialization to ensure new IDs do not conflict
     * with data loaded from CSV files.
     *
     * @param n The next ID to be generated will be n.
     */
    public void setStartIDforInitialization(int n){ this.idGenerationIndex = n;};

    /**
     * Generates a new, unique application ID and increments the internal counter.
     *
     * @return A unique integer ID.
     */
    public int generateId() {return idGenerationIndex++;}

    /**
     * Adds a new application to the store.
     *
     * @param app The {@link Application} object to add.
     */
    public void add(Application app) {applications.add(app);}

    /**
     * Gets a *copy* of the list of all applications.
     * Returning a copy prevents external modification of the internal store.
     *
     * @return A new {@link ArrayList} containing all applications.
     */
    public List<Application> getAll() {return new ArrayList<>(applications);} //supposedly you do this so you dont touch your actual list, the applications inside are just reference so best to make a copy

    /**
     * Finds an application by its unique ID.
     *
     * @param id The ID of the application to search for.
     * @return The matching {@link Application} object, or null if not found.
     */
    public Application getById(int id) {
        for (Application a : applications) {
            if (a.getApplicationID() == id) {
                return a;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * Filters the list of applications using a stream based on the provided criteria.
     *
     * @param fCriteria The {@link FilterCriteria} to apply.
     * @return A new {@link List} containing only the applications that match the criteria.
     */
    @Override
    public List<Application> filter(FilterCriteria<Application> fCriteria){
        return applications.stream() //iterates each applciation smth like pythons iterable
                .filter(fCriteria :: matches) // keeps application that return true in filter
                .collect(Collectors.toList()); // shoves result into a list
    }
}
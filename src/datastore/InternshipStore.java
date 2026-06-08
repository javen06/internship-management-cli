package datastore;

import filters.FilterCriteria;
import filters.IFilterable;
import placement.Application;
import placement.Internship;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An in-memory repository for {@link placement.Internship} objects.
 * Implements {@link filters.IFilterable} to allow for filtering internships.
 * This class also handles the generation of unique IDs for new internships.
 *
 * @version 1.0
 */
public class InternshipStore implements IFilterable<Internship> {
    //attributes
    /** The counter for generating new internship IDs. */
    private int idGenerationIndex = 1;  //very smart name
    /** The list holding all Internship objects. */
    private List<Internship> internships = new ArrayList<Internship>();

    //functions
    /**
     * Adds a new internship to the store.
     *
     * @param internship The {@link Internship} object to add.
     */
    public void add(Internship internship) {internships.add(internship);}

    /**
     * Gets a *copy* of the list of all internships.
     * Returning a copy prevents external modification of the internal store.
     *
     * @return A new {@link ArrayList} containing all internships.
     */
    public List<Internship> getAll() {return new ArrayList<>(internships);}

    /**
     * Generates a new, unique internship ID and increments the internal counter.
     *
     * @return A unique integer ID.
     */
    public int generateId() {return idGenerationIndex++;}

    /**
     * Finds an internship by its unique ID.
     *
     * @param id The ID of the internship to search for.
     * @return The matching {@link Internship} object, or null if not found.
     */
    public Internship getById(int id) {
        for (Internship i : internships) {
            if (i.getInternshipId() == id) {
                return i;
            }
        }
        return null;
    }

    /**
     * Sets the starting ID for the ID generator.
     * This is used during initialization to ensure new IDs do not conflict
     * with data loaded from CSV files.
     *
     * @param n The next ID to be generated will be n.
     */
    public void setStartIDforInitialization(int n){ this.idGenerationIndex = n;};

    /**
     * Removes an internship from the store by its ID.
     *
     * @param id The ID of the internship to remove.
     * @return {@code true} if an internship was found and removed,
     * {@code false} otherwise.
     */
    public boolean removeById(int id) {
        return internships.removeIf(internship -> internship.getInternshipId() == id);
    }

    /**
     * {@inheritDoc}
     * Filters the list of internships using a stream based on the provided criteria.
     *
     * @param fCriteria The {@link FilterCriteria} to apply.
     * @return A new {@link List} containing only the internships that match the criteria.
     */
    @Override
    public List<Internship> filter(FilterCriteria<Internship> fCriteria){
        return internships.stream() //iterates each applciation smth like pythons iterable
                .filter(fCriteria :: matches) // keeps application that return true in filter
                .collect(Collectors.toList()); // shoves result into a list
    }
}
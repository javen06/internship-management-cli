package datastore;

import user.Student;
import user.CompanyRep;
import user.CareerStaff;

import java.io.File;

/**
 * The main concrete implementation of the {@link IDataStore} interface.
 * Holds individual data stores (e.g., {@link UserStore}, {@link ApplicationStore}).
 *
 * @version 1.0
 */
public class DataStore implements IDataStore {

    /** Store for all {@link Student} users. */
    private final UserStore<Student> studentStore = new UserStore<Student>();
    /** Store for all {@link CompanyRep} users. */
    private final UserStore<CompanyRep> repStore = new UserStore<CompanyRep>();
    /** Store for all {@link CareerStaff} users. */
    private final UserStore<CareerStaff> staffStore = new UserStore<CareerStaff>();

    /** Store for all {@link placement.Application} objects. */
    private final ApplicationStore applicationStore = new ApplicationStore();
    /** Store for all {@link placement.Internship} objects. */
    private final InternshipStore internshipStore = new InternshipStore();

    /**
     * Constructs the main DataStore, initializing all individual stores.
     */
    public DataStore() {
        String base = System.getProperty("user.dir") + File.separator;
    }

    /**
     * {@inheritDoc}
     * @return The central store for {@link Student} objects.
     */
    @Override
    public UserStore<Student> getStudentStore() { return studentStore; }

    /**
     * {@inheritDoc}
     * @return The central store for {@link CompanyRep} objects.
     */
    @Override
    public UserStore<CompanyRep> getRepStore() { return repStore; }

    /**
     * {@inheritDoc}
     * @return The central store for {@link CareerStaff} objects.
     */
    @Override
    public UserStore<CareerStaff> getStaffStore() { return staffStore; }

    /**
     * {@inheritDoc}
     * @return The central store for {@link placement.Application} objects.
     */
    @Override
    public ApplicationStore getApplicationStore() { return applicationStore; }

    /**
     * {@inheritDoc}
     * @return The central store for {@link placement.Internship} objects.
     */
    @Override
    public InternshipStore getInternshipStore() { return internshipStore; }

}
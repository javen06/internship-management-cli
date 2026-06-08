package datastore;

import user.CareerStaff;
import user.CompanyRep;
import user.Student;

/**
 * Defines the contract for the main data store facade.
 * This interface provides a single point of access to all individual
 * data stores (e.g., Student store, Application store) within the application.
 *
 * @version 1.0
 */
public interface IDataStore {

    /**
     * Retrieves the store responsible for managing {@link Student} objects.
     *
     * @return The {@link UserStore} for Students.
     */
    UserStore<Student> getStudentStore();

    /**
     * Retrieves the store responsible for managing {@link CompanyRep} objects.
     *
     * @return The {@link UserStore} for Company Representatives.
     */
    UserStore<CompanyRep> getRepStore();

    /**
     * Retrieves the store responsible for managing {@link CareerStaff} objects.
     *
     * @return The {@link UserStore} for Career Staff.
     */
    UserStore<CareerStaff> getStaffStore();

    /**
     * Retrieves the store responsible for managing {@link placement.Application} objects.
     *
     * @return The {@link ApplicationStore}.
     */
    ApplicationStore getApplicationStore();

    /**
     * Retrieves the store responsible for managing {@link placement.Internship} objects.
     *
     * @return The {@link InternshipStore}.
     */
    InternshipStore getInternshipStore();

}
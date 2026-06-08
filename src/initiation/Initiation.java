package initiation;

import datastore.DataStore;
import java.io.File;

/**
 * Handles the initial population of the {@link DataStore} from CSV files.
 * This class uses various builder classes (e.g., {@link StudentBuilder},
 * {@link InternshipBuilder}) to read data from specified file paths
 * and load the resulting objects into the main data store.
 *
 * @version 1.0
 */
public class Initiation {
    /**
     * Initializes the DataStore by loading all data from CSV files.
     * This static method orchestrates the entire data-loading process at
     * application startup.
     *
     * @param store The central {@link DataStore} instance to be populated.
     * @param studentPath Path to the student CSV file, relative to the project root.
     * @param repPath Path to the company representative CSV file, relative to the project root.
     * @param staffPath Path to the career staff CSV file, relative to the project root.
     * @param internshipPath Path to the internship CSV file, relative to the project root.
     * @param applicationPath Path to the application CSV file, relative to the project root.
     */
    public static void initilize (DataStore store, String studentPath, String repPath, String staffPath, String internshipPath, String applicationPath) {
        System.out.println("Initialization started...");

        String base = System.getProperty("user.dir") + File.separator;

        new StudentBuilder().build(base + studentPath)
                .forEach(store.getStudentStore()::add);

        new CompanyRepBuilder().build(base + repPath)
                .forEach(store.getRepStore()::add);

        new CareerStaffBuilder().build(base + staffPath)
                .forEach(store.getStaffStore()::add);

        new InternshipBuilder().build(base + internshipPath, store)
                .forEach(store.getInternshipStore()::add);
        store.getInternshipStore().setStartIDforInitialization(store.getInternshipStore().getAll().size() + 1);

        new ApplicationBuilder().build(base + applicationPath, store)
                .forEach(store.getApplicationStore()::add);
        store.getApplicationStore().setStartIDforInitialization(store.getApplicationStore().getAll().size() + 1);

        System.out.println("Loaded Students: " + store.getStudentStore().getAll().size());
        System.out.println("Loaded Company Reps: " + store.getRepStore().getAll().size());
        System.out.println("Loaded Staff: " + store.getStaffStore().getAll().size());
        System.out.println("Loaded Internships: " + store.getInternshipStore().getAll().size());
        System.out.println("Loaded Applications: " + store.getApplicationStore().getAll().size());
        System.out.println("Initialization completed.");
    }
}
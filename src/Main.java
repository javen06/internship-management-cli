import boundary.*;
import controller.*;
import datastore.*;
import initiation.Initiation;
import user.*;

/**
 * The main entry point for the Internship Placement Management System.
 * <p>
 * This class is responsible for:
 * 1. Initializing the central {@link DataStore} and {@link Session}.
 * 2. Triggering the {@link Initiation} class to load all CSV data.
 * 3. Starting the main {@link AuthUI} to handle login or registration.
 * 4. Routing the user to the correct UI ({@link StudentUI}, {@link CompanyRepUI},
 * or {@link CareerStaffUI}) after a successful login.
 *
 * @version 1.0
 */
public class Main {
    /**
     * The main method that launches the application.
     *
     * @param args Command-line arguments (not used by this application).
     */
    public static void main(String[] args) {
        DataStore store = new DataStore();   // Loads all CSVs
        Session session = new Session();     // Tracks current logged-in user

        // Populate the DataStore from CSV files
        Initiation.initilize(store,
                "sample_student_list.csv",
                "sample_company_representative_list.csv",
                "sample_staff_list.csv",
                "sample_internship_list.csv",
                "sample_application_list.csv");

        //============================================
        AuthController authcon = new AuthController(store, session);
        AuthUI ui = new AuthUI(authcon, store);
        boolean ok = true;

        // Start the main authentication loop
        while (ok == true) {
            ok = ui.start(); // 'ok' is true if user logged in, false if they exited
            if (ok){
                User user = authcon.getCurrentUser();

                // Route to the correct UI based on the user's type
                if (user instanceof Student){
                    StudentController stuController = new StudentController(store, session);
                    StudentUI studentUI = new StudentUI(stuController, authcon);
                    studentUI.start();
                    authcon.logout();
                }
                else if (user instanceof CompanyRep){
                    CompanyRepController repController = new CompanyRepController(store, session);
                    CompanyRepUI companyRepUI = new CompanyRepUI(repController, authcon);
                    companyRepUI.start();
                    authcon.logout();
                }
                else if (user instanceof CareerStaff){
                    CareerStaffController cstaffController = new CareerStaffController(store, session);
                    new CareerStaffUI(cstaffController, authcon).start();
                    authcon.logout();

                }
                else {
                    System.out.println("Unknown user. Logging out.");
                    authcon.logout();

                }
            }

        }
    }
}
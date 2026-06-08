package boundary;

// Make sure to import your static display classes
import boundary.commonDisplays.ApplicationView;
import boundary.commonDisplays.InternshipView;
import controller.AuthController;
import controller.IAccountManager;
import controller.StudentController;
import filters.InternshipFilter;
import java.time.LocalDate;
import java.util.List;
import placement.*;
import user.Student;

/**
 * The command-line interface (CLI) for the Student user.
 * This class provides the main menu for students to view internships,
 * manage their applications (apply, withdraw, accept), and edit their
 * internship filter preferences. It interacts with the {@link StudentController}.
 *
 * @version 1.0
 */
public class StudentUI {

    private final StudentController controller;
    private final IAccountManager accountManager;

    /**
     * Constructs a new StudentUI.
     *
     * @param controller The controller for student logic.
     * @param authController The account manager for session/password functions.
     */
    public StudentUI(StudentController controller, AuthController authController) {
        this.controller = controller;
        this.accountManager = authController;
    }

    /**
     * Starts the main menu loop for the Student.
     * This loop displays available internships and menu options,
     * and continues until the user chooses to log out.
     */
    public void start() {
        int choice = 0;

        while (true) {

            this.showMenu();

            //System.out.println("\n--- Available Internships For You ---");
            List<Internship> internships = controller.getFilteredInternships();
            InternshipView.displayStudentView(internships);
            System.out.println("---------------------------------------");

            //Print out active filters
            System.out.println(controller.getStudentFilterPreferences().toString(false));

            this.showMenuOptions();

            // 4. Get input
            choice = InputHandler.getIntInput("Enter your choice:", 1, 7); // Max is 7

            switch (choice) {
                case 1 -> editStudentFilters();
                case 2 -> viewMyApplications();
                case 3 -> applyForInternship(internships);
                case 4 -> acceptInternshipOffer();
                case 5 -> withdrawApplication();
                case 6 -> {
                    if(changePassword()) {
                        System.out.println("Logging out...");
                        return; // Exit the while loop and return to Main
                    }
                }
                case 7 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }


    /**
     * Displays the welcome message and menu title.
     */
    private void showMenu() {
        System.out.println("\n===== STUDENT MENU =====");
        System.out.println("Welcome," + accountManager.getCurrentUserName() + " (Student)");
    }


    /**
     * Displays the list of numbered options for the student menu.
     */
    private void showMenuOptions() {
        System.out.println("Select an option:");
        System.out.println("""
        1. Edit 'View Internships' Filter
        2. View My Applications
        3. Apply for Internship
        4. Accept Internship Offer
        5. Withdraw Application
        6. Change Password
        7. Log Out
                        """);
    }


    /**
     * Fetches and displays all applications (all statuses)
     * for the currently logged-in student.
     */
    private void viewMyApplications() {
        System.out.println("\n--- My Applications ---");
        List<Application> applications = controller.getApplications(null,false); // Get all
        ApplicationView.displayDetailedApplications(applications);
    }


    /**
     * Guides the student through applying for an internship.
     * This method enforces business rules:
     * - A student cannot apply if they have already accepted an offer.
     * - A student cannot apply if they have 3 or more PENDING or SUCCESSFUL applications.
     *
     * @param availableInternships The list of internships currently visible to the student.
     */
    private void applyForInternship(List<Internship> availableInternships) {
        System.out.println("\n--- Apply for Internship ---");

        //Already accept an offer
        List<Application> acceptedApps = controller.getApplications(ApplicationStatus.ACCEPTED, false);
        if (!acceptedApps.isEmpty()){
            System.out.println("You have already accepted an internship offer. You cannot apply for more internships.");
            return;
        }

        // Max 3 applications rule
        List<Application> pendingApps = controller.getApplications(ApplicationStatus.PENDING, false);
        List<Application> successfulApps = controller.getApplications(ApplicationStatus.SUCCESSFUL, false);
        if ((pendingApps.size() + successfulApps.size()) >= 3) {
            System.out.println("You already have 3 pending or successful applications. You cannot apply for more.");
            return;
        }

        // If there even is any internships to apply for
        if (availableInternships.isEmpty()) {
            System.out.println("There are no internships available for you to apply for at this time.");
            return;
        }


        Boolean success = false;
        while (!success) {
            int internshipId = InputHandler.getIntInput("Enter the ID of the internship you want to apply for (or 0 to cancel):");
            if (internshipId == 0) return; // Allow user to exit loop

            success = controller.CreateApplications(internshipId);

            if (!success) {
                System.out.println("Application Unsuccessful");
            } else {
                System.out.println("Application created successfully.");
            }
        }
    }


    /**
     * Guides the student through accepting a SUCCESSFUL internship offer.
     * Displays only applications with status SUCCESSFUL and available slots.
     * Accepting an offer automatically withdraws all other applications.
     */
    private void acceptInternshipOffer() {
        System.out.println("\n--- Accept Internship Offer ---");
        List<Application> successfulApplications = controller.getApplications(ApplicationStatus.SUCCESSFUL, true);
        Boolean success = false;

        if (successfulApplications.isEmpty()) {
            System.out.println("No successful internship applications with available slots to accept.");
            return;
        }

        ApplicationView.displayApplications(successfulApplications);

        while (!success) {
            int applicationId = InputHandler.getIntInput("Enter the ID of the application you want to accept (or 0 to cancel):");
            if (applicationId == 0) return;

            int finalAppId = applicationId;
            if (successfulApplications.stream().anyMatch(a -> a.getApplicationID() == finalAppId)) {
                success = controller.AcceptApplications(applicationId);
            } else {
                System.out.println("Invalid application ID. Please try again.");
            }
        }
        System.out.println("Application accepted successfully. All other applications have been withdrawn.");
    }


    /**
     * Guides the student through submitting a request to withdraw an application.
     * The student can only request to withdraw applications that are not
     * already WITHDRAWN or UNSUCCESSFUL.
     */
    private void withdrawApplication() {
        System.out.println("\n--- Withdraw Application ---");
        // You should create this new controller method for better logic
        List<Application> applications = controller.getWithdrawableApplications();
        Boolean success = false;

        if (applications.isEmpty()) {
            System.out.println("No available applications to withdraw.");
            return;
        }

        ApplicationView.displayApplications(applications);

        while (!success) {
            int applicationId = InputHandler.getIntInput("Enter the ID of the application you want to withdraw (or 0 to cancel):");
            if (applicationId == 0) return;

            int finalAppId = applicationId;
            if (applications.stream().anyMatch(a -> a.getApplicationID() == finalAppId)) {
                success = controller.WithdrawApplication(applicationId); // BUG FIX: Correct method
                if (!success){
                    System.out.println("This application cannot be withdrawn at this time.");
                }
            } else {
                System.out.println("Invalid application ID. Please try again.");
            }
        }
        System.out.println("Application withdrawal request submitted successfully.");
    }

    /**
     * Handles the process of changing the user's password.
     *
     * @return {@code true} if the password was successfully changed,
     * {@code false} otherwise.
     */
    private boolean changePassword() {
        System.out.println("\n--- Change Password---");
        System.out.println("Note: You will be logged out after changing your password.");
        String oldPw = InputHandler.getStringInput("Enter your current password: ");
        String newPw = InputHandler.getStringInput("Enter your new password: ");

        boolean success = accountManager.changePassword(oldPw, newPw);
        if (!success) {
            System.out.println("Failed to change password.");
        }
        return success;
    }

    /**
     * Displays a menu to edit the properties of the "View Internships"
     * {@link InternshipFilter}.
     * Changes are saved directly to the filter object in the controller.
     * The "Set Level Filter" option is only shown to students in Year 3 or higher.
     */
    private void editStudentFilters( ) {
        // Get the student's saved filter preferences
        InternshipFilter studentFilter = controller.getStudentFilterPreferences();

        int studentYear = 0;
        if (accountManager.getCurrentUser() instanceof Student) {
            studentYear = ((Student) accountManager.getCurrentUser()).getYear();
        }

        while (true) {
            System.out.println("\n--- Edit 'View Internships' Filter ---");
            System.out.println("Active Filters: " + studentFilter.toString(false)); // false = don't show visibility

            int maxOption = 6;

            System.out.println("\n--- Edit Internships Filter ---");
            System.out.println("1. Back to menu");
            System.out.println("2. Reset All Filters");
            System.out.println("3. Contains Title");
            System.out.println("4. Set Closing Date Before");
            System.out.println("5. Set Closing Date After");
            System.out.println("6. Company Name");
            System.out.println("7. Set Internship Levels");



            if (studentYear >= 3) { // Only show Level filter for Year 3+
                System.out.println("7. Set Level Filter");
                maxOption += 1;
            }

            int choice = InputHandler.getIntInput("Enter choice: ", 1, maxOption);

            switch (choice) {
                case 1 -> {return;}

                case 2 -> {
                    controller.filterClear();
                    System.out.println("Your filters have been reset.");
                } //

                case 3 -> {
                    String title = InputHandler.getOptionalStringInput("Enter text to filter by title (or blank to clear): ");
                    if (title.isBlank()) {
                        studentFilter.setTitleContains(null);
                    } else {
                        studentFilter.setTitleContains(title);
                    }
                }

                case 4 -> {
                    LocalDate dateBefore = InputHandler.getDateInput("Enter closing BEFORE date (yyyy-MM-dd or blank): ", LocalDate.now());
                    studentFilter.setLatestClosingDate(dateBefore);
                }

                case 5 -> {
                    LocalDate dateAfter = InputHandler.getDateInput("Enter closing AFTER date (yyyy-MM-dd or blank): ", LocalDate.now());
                    studentFilter.setEarliestClosingDate(dateAfter);

                }
                case 6 -> {
                    String companyName = InputHandler.getOptionalStringInput("Enter text to filter by Company Name (or blank to clear): ");
                    if (companyName.isBlank()) {
                        studentFilter.setCompanyNameContains(null);
                    } else {
                        studentFilter.setCompanyNameContains(companyName);
                    }
                }
                case 7 -> { // This case is only reachable by Year 3+ students
                    while (true) {
                        String l = InputHandler.getOptionalStringInput("Enter level (BASIC/INTERMEDIATE/ADVANCED or blank): ");
                        if (l.isBlank()) { studentFilter.setLevel(null); break; }
                        try {
                            studentFilter.setLevel(InternshipLevel.valueOf(l.toUpperCase()));
                            break;
                        } catch (IllegalArgumentException e) { System.out.println("Error: '" + l + "' is not a valid level."); }
                    }
                }
            }
        }
    }
}

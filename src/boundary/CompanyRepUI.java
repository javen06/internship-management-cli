package boundary;

// Make sure these imports match your new static display class paths
import boundary.commonDisplays.ApplicationView;
import boundary.commonDisplays.InternshipView;
import controller.AuthController;
import controller.CompanyRepController;
import controller.IAccountManager;
import filters.InternshipFilter; // Restored, as requested
import java.time.LocalDate;
import java.util.List;
import placement.Application;
import placement.Internship;
import placement.InternshipLevel;
import placement.InternshipStatus;

/**
 * The command-line interface (CLI) for the Company Representative user.
 * This class provides a menu for managing internships (create, edit, delete, toggle)
 * and managing applications (view, approve, reject).
 * It interacts with the {@link CompanyRepController}.
 *
 * @version 1.0
 */
public class CompanyRepUI {

    private final CompanyRepController controller;
    private final IAccountManager accountManager; // Using Session as requested

    /**
     * Constructs a new CompanyRepUI.
     *
     * @param controller The controller for company representative logic.
     * @param authController The account manager for session/password functions.
     */
    public CompanyRepUI(CompanyRepController controller, AuthController authController) {
        this.controller = controller;
        this.accountManager = authController;
    }

    /**
     * Starts the main menu loop for the Company Representative.
     * This loop continues until the user chooses to log out.
     */
    public void start() {
        while (true) {
            displayMyInternships();
            System.out.println("\n===== COMPANY REPRESENTATIVE MENU =====");
            System.out.println("Welcome, " + accountManager.getCurrentUserName());
            System.out.println("---------------------------------------");
            System.out.println("1. Edit My Pending Internships");
            System.out.println("2. Delete My Pending Internships");
            System.out.println("3. Edit 'My Internships' filter");
            System.out.println("4. Create new internship");
            System.out.println("5. View applications for an internship");
            System.out.println("6. Approve an application");
            System.out.println("7. Reject an application");
            System.out.println("8. Toggle internship visibility");
            System.out.println("9. View all applications for all my internships");
            System.out.println("10. Change Password");
            System.out.println("11. Logout");

            int choice = InputHandler.getIntInput("Enter your choice: ", 1, 11); // Max is 11

            switch (choice) {
                case 1 -> editPendingInternship();
                case 2 -> deletePendingInternship();
                case 3 -> editMyInternshipsFilter();
                case 4 -> createNewInternship();
                case 5 -> viewApplicationsForInternship();
                case 6 -> approveApplication();
                case 7 -> rejectApplication();
                case 8 -> toggleVisibility();
                case 9 -> displayAllApplicationsForMyInternships();
                case 10 -> {
                    if(changePassword()) {
                        System.out.println("Logging out...");
                        return; // Exit the while loop and return to Main
                    }
                }
                case 11 -> {
                    System.out.println("Logging out...");
                    return; // Exit the while loop and return to Main
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    /**
     * Fetches and displays all internships associated with the current
     * representative, based on their active filter settings.
     */
    private void displayMyInternships() {
        List<Internship> internships = controller.getMyInternships();

        System.out.println("\n--- My Internships ---");
        InternshipView.displayStaffView(internships);
        System.out.println(controller.getuserInternshipViewfilter().toString(true));
    }


    /**
     * Guides the representative through the process of creating a new
     * internship, gathering all required details.
     */
    private void createNewInternship() {
        System.out.println("\n--- Create New Internship ---");

        String title = InputHandler.getStringInput("Enter internship title: ");
        String desc = InputHandler.getStringInput("Enter internship description: ");
        String major = InputHandler.getStringInput("Enter preferred major: ");


        InternshipLevel level = null;
        while (level == null) {
            String l = InputHandler.getStringInput("Enter level (BASIC/INTERMEDIATE/ADVANCED): ");
            try {
                level = InternshipLevel.valueOf(l.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid level. Please try again.");
            }
        }

        int slot = InputHandler.getIntInput("Enter available slots: ", 1, 10);
        LocalDate openDate = InputHandler.getDateInput("Enter opening date (yyyy-MM-dd): ", LocalDate.now());
        LocalDate closeDate = InputHandler.getDateInput("Enter closing date (yyyy-MM-dd): ", openDate);

        boolean created = controller.createInternship(title, desc, major, level, slot, openDate, closeDate);

        if (created) {
            System.out.println("Internship created and submitted for approval by Career Staff.");
        } else {
            // most likely hit the “max 5” rule
            System.out.println("Failed to create internship.");
        }
    }

    /**
     * Prompts for an internship ID and displays all applications
     * submitted for that internship.
     */
    private void viewApplicationsForInternship() {
        System.out.println("\n--- View Applications for an Internship ---");
        int id = InputHandler.getIntInput("Enter the internship ID: ", 1, Integer.MAX_VALUE);
        List<Application> apps = controller.getApplicationsByInternship(id);
        ApplicationView.displayDetailedApplications(apps);
    }

    /**
     * Prompts for an application ID and approves it via the controller.
     */
    private void approveApplication() {
        System.out.println("\n--- Approve Application ---");
        int appId = InputHandler.getIntInput("Enter application ID to approve: ", 1, Integer.MAX_VALUE);
        boolean ok = controller.approveApplication(appId);
        if (ok) {
            System.out.println("Application approved (status set to SUCCESSFUL).");
        } else {
            System.out.println("Could not approve application. Check that the application exists, belongs to your internship and still has available slots.");
        }
    }

    /**
     * Prompts for an application ID and rejects it via the controller.
     */
    private void rejectApplication() {
        System.out.println("\n--- Reject Application ---");
        int appId = InputHandler.getIntInput("Enter application ID to reject: ", 1, Integer.MAX_VALUE);
        boolean ok = controller.rejectApplication(appId);
        if (ok) {
            System.out.println("Application rejected (status set to UNSUCCESSFUL).");
        } else {
            System.out.println("Could not reject application. Check that the application exists and belongs to your internship.");
        }
    }

    /**
     * Prompts for an internship ID and toggles its student-facing
     * visibility via the controller.
     */
    private void toggleVisibility() {
        System.out.println("\n--- Toggle Internship Visibility ---");
        int id = InputHandler.getIntInput("Enter the internship ID: ", 1, Integer.MAX_VALUE);
        boolean ok = controller.toggleVisibility(id);
        if (ok) {
            System.out.println("Visibility toggled.");
        } else {
            System.out.println("Could not toggle visibility. Check that the internship exists and belongs to you.");
        }
    }

    /**
     * Fetches and displays all applications for all internships
     * associated with the current representative.
     */
    private void displayAllApplicationsForMyInternships() {
        System.out.println("\n--- All Applications for My Internships ---");
        List<Application> apps = controller.getAllApplicationsForMyInternships();
        ApplicationView.displayDetailedApplications(apps);
    }


    /**
     * Displays a menu to edit the properties of the "My Internships"
     * view's {@link InternshipFilter}.
     * Changes are saved directly to the filter object in the controller.
     */
    private void editMyInternshipsFilter() {
        InternshipFilter repFilter = controller.getuserInternshipViewfilter();
        while (true) {
            System.out.println(repFilter.toString(true));
            System.out.println("\n--- Edit Internships Filter ---");
            System.out.println("1. Set Status Filter (e.g., PENDING, APPROVED)");
            System.out.println("2. Set Level Filter");
            System.out.println("3. Contains Title");
            System.out.println("4. Reset All Filters");
            System.out.println("5. Back to menu");

            int choice = InputHandler.getIntInput("Enter choice: ", 1, 5);

            switch (choice) {
                case 1 -> { // Set Status
                    while (true) {
                        String s = InputHandler.getOptionalStringInput(
                                "Enter status (PENDING/APPROVED/REJECTED/FILLED or blank): ");
                        if (s.isBlank()) {
                            repFilter.setStatus(null);
                            break;
                        }
                        try {
                            repFilter.setStatus(InternshipStatus.valueOf(s.toUpperCase()));
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Error: '" + s + "' is not a valid status.");
                        }
                    }
                    break;
                }
                case 2 -> { // Set Level
                    while (true) {
                        String l = InputHandler.getOptionalStringInput(
                                "Enter level (BASIC/INTERMEDIATE/ADVANCED or blank): ");
                        if (l.isBlank()) {
                            repFilter.setLevel(null);
                            break;
                        }
                        try {
                            repFilter.setLevel(InternshipLevel.valueOf(l.toUpperCase()));
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Error: '" + l + "' is not a valid level.");
                        }
                    }
                    break;
                }
                case 3 -> { // Contains Title
                    String title = InputHandler.getOptionalStringInput("Enter text to filter by title (or blank to clear): ");
                    if (title.isBlank()) {
                        repFilter.setTitleContains(null);
                    } else {
                        repFilter.setTitleContains(title);
                    }
                    break;
                }
                case 4 -> { // Reset Filters
                    repFilter.reset();
                    System.out.println("Your filters have been reset.");
                    break;
                }
                case 5 -> { // Back to menu
                    return;
                }
            }
        }
    }

    /**
     * Guides the representative through editing an internship.
     * Only internships in PENDING status are eligible for editing.
     */
    private void editPendingInternship() {
        System.out.println("\n--- Edit a PENDING Internship ---");

        List<Internship> pendingInternships = controller.getMyInternships(InternshipStatus.PENDING);

        if (pendingInternships.isEmpty()) {
            System.out.println("You have no PENDING internships to edit.");
            return;
        }

        System.out.println("Your PENDING internships:");
        InternshipView.displayStaffView(pendingInternships);

        int internshipId = InputHandler.getIntInput("Enter the ID of the internship you want to edit (or 0 to cancel): ");
        if (internshipId == 0) return;

        Internship internshipToEdit = controller.getEditableInternshipById(internshipId);

        if (internshipToEdit == null) {
            return;
        }

        System.out.println("\nEditing Internship: " + internshipToEdit.getTitle());
        System.out.println("Enter the new details (or just press ENTER to keep current value).");

        // Get raw input for each field, showing the current value
        String title = getStringOrDash("Enter new title (current: " + internshipToEdit.getTitle() + "): ");
        String desc = getStringOrDash("Enter new description (current: " + internshipToEdit.getDescription() + "): ");
        String major = getStringOrDash("Enter new preferred major (current: " + internshipToEdit.getPreferredMajor() + "): ");
        String level = getStringOrDash("Enter new level (current: " + internshipToEdit.getLevel() + "): ");
        String slot = getStringOrDash("Enter new slot (current: " + internshipToEdit.getTotalSlots() + "): "); // this was previously set to level, i assume it should be slots
        String openDate = getStringOrDash("Enter new opening date (current: " + internshipToEdit.getOpenDate() + "): ");
        String closeDate = getStringOrDash("Enter new closing date (current: " + internshipToEdit.getCloseDate() + "): ");

        boolean success = controller.updateInternship(internshipId, title, desc, major, level, slot, openDate, closeDate);

        System.out.println(!success ? "Failed to update internship. Please try again." : "Internship " + internshipId + " updated successfully.");

    }

    /**
     * Guides the representative through deleting an internship.
     * Only internships in PENDING status are eligible for deletion.
     */
    private void deletePendingInternship() {
        System.out.println("\n--- Delete a PENDING Internship ---");

        List<Internship> pendingInternships = controller.getMyInternships(InternshipStatus.PENDING);

        if (pendingInternships.isEmpty()) {
            System.out.println("You have no PENDING internships to delete.");
            return;
        }

        System.out.println("Your PENDING internships:");
        InternshipView.displayStaffView(pendingInternships);

        int internshipId = InputHandler.getIntInput("Enter the ID of the internship you want to DELETE (or 0 to cancel): ");
        if (internshipId == 0) return;

        String confirm = InputHandler.getStringInput("Are you sure you want to delete internship " + internshipId + "? This cannot be undone. (yes/no): ");

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        boolean success = controller.deletePendingInternship(internshipId);

        if (success) {
            System.out.println("Internship " + internshipId + " has been successfully deleted.");
        } else {
            System.out.println("Failed to delete internship. It may have already been reviewed by staff or the ID was incorrect.");
        }
    }

    /**
     * A helper method for the editing process. Gets optional string input
     * from the user. If the user presses ENTER, it returns a "-" dash string.
     *
     * @param prompt The message to display to the user.
     * @return The user's input, or "-" if the input was blank.
     */
    private String getStringOrDash(String prompt) {
        String input = InputHandler.getOptionalStringInput(prompt);
        if (input.isBlank()) {
            return "-";
        }
        return input;
    }

    /**
     * Handles the process of changing the user's password.
     *
     * @return {@code true} if the password was successfully changed,
     * {@code false} otherwise.
     */
    private boolean changePassword() {
        System.out.println("\n--- Change Password ---");
        System.out.println("Note: You will be logged out after changing your password.");
        String oldPw = InputHandler.getStringInput("Enter your current password: ");
        String newPw = InputHandler.getStringInput("Enter your new password: ");

        boolean success = accountManager.changePassword(oldPw, newPw);
        if (!success) {
            System.out.println("Failed to change password.");
        }
        return success;
    }
}
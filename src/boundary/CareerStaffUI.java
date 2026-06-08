package boundary;


import boundary.commonDisplays.ApplicationView;
import boundary.commonDisplays.InternshipView;
import controller.CareerStaffController;
import controller.IAccountManager;
import filters.InternshipFilter;
import java.util.Comparator;
import java.util.List;
import placement.Application;
import placement.Internship;
import placement.InternshipLevel;
import placement.InternshipStatus;
import user.CompanyRep;

/**
 * The command-line interface (CLI) for the Career Staff user.
 * This class provides a menu of administrative actions, such as
 * approving accounts, approving internships, managing withdrawals,
 * and viewing reports. It interacts with the {@link CareerStaffController}.
 *
 * @version 1.0
 */
public class CareerStaffUI {

    private final CareerStaffController controller;
    private final IAccountManager accountManager;

    /**
     * Constructs a new CareerStaffUI.
     *
     * @param controller The controller for career staff logic.
     * @param accountManager The account manager for session/password functions.
     */
    public CareerStaffUI(CareerStaffController controller, IAccountManager accountManager) {
        this.controller = controller;
        this.accountManager = accountManager;
    }

    /**
     * Starts the main menu loop for the Career Staff.
     * This loop continues until the user chooses to log out.
     */
    public void start() {
        while (true) {
            System.out.println("\n===== CAREER STAFF MENU =====");
            System.out.println("Welcome, " + accountManager.getCurrentUserName());
            System.out.println("---------------------------------------");
            System.out.println("1. Display internship report");
            System.out.println("2. Edit internship report filter");
            System.out.println("3. View pending company reps to approve");
            System.out.println("4. Process pending company rep account approval");
            System.out.println("5. View withdrawal Requests");
            System.out.println("6. Approve student's withdrawal request");
            System.out.println("7. View pending internships to approve");
            System.out.println("8. Process pending internship opportunity approval");
            System.out.println("9. Change Password");
            System.out.println("10. Logout");

            int choice = InputHandler.getIntInput("Enter your choice: ", 1, 10);

            switch (choice) {
                case 1 -> displayReport();
                case 2 -> editReportFilter();
                case 3 -> viewPendingCompanyReps();
                case 4 -> processCompanyRepApproval();
                case 5 -> viewWithdrawalRequests();
                case 6 -> processWithdrawalRequest();
                case 7 -> viewPendingInternships();
                case 8 -> processPendingInternship();
                case 9 -> {
                    if(changePassword()) {
                        System.out.println("Logging out...");
                        return; // Exit the while loop and return to Main
                    }
                }
                case 10 -> {
                    System.out.println("Logging out...");
                    return; // Exit the while loop and return to Main
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    /**
     * Fetches and displays the comprehensive internship report based
     * on the active filters set in the controller.
     * Internships are sorted alphabetically by title.
     */
    private void displayReport() {

        List<Internship> sortedInternships = controller.generateReport();
        sortedInternships.sort(Comparator.comparing(
                Internship::getTitle,
                String.CASE_INSENSITIVE_ORDER
        ));

        System.out.println("==================================================================================");
        System.out.println("                   Comprehensive Internship Report");
        System.out.println("==================================================================================");
        System.out.println("Active Filters: " + controller.getReportFilter().toString(true));
        System.out.println("----------------------------------------------------------------------------------");

        if (sortedInternships.isEmpty()) {
            System.out.println("No internships match the filter.");
            return;
        }

        for (Internship i : sortedInternships) {
            // Print the internship
            System.out.printf(
                    "• %s (%s) | Status: %s | Visible: %s | Slots: %d/10 | Company: %s (%s) | Level: %s | Major: %s | Dates: %s to %s\n",
                    i.getTitle(),
                    i.getInternshipId(),
                    i.getStatus(),
                    i.isVisible() ? "Yes" : "No",
                    i.getFilledSlots(),
                    i.getCompanyName(),
                    i.getCompanyRep().getUserId(),
                    i.getLevel(),
                    i.getPreferredMajor(),
                    i.getOpenDate(),
                    i.getCloseDate()
            );

            List<Application> appsForThisInternship = controller.getApplicationsForInternship(i.getInternshipId());

            if (appsForThisInternship.isEmpty()) {
                System.out.println("    > No applications for this internship.");
            } else {
                System.out.println("    > Applications (" + appsForThisInternship.size() + "):");
                for (Application a : appsForThisInternship) {
                    System.out.printf("      > %s (%s) | Status: %s\n",
                            a.getStudent().getUserName(),
                            a.getStudent().getUserId(),
                            a.getApplicationStatus()
                    );
                }
            }
            System.out.println("----------------------------------------------------------------------------------");
        }
    }

    /**
     * Displays a menu to edit the properties of the report's {@link InternshipFilter}.
     * Changes are saved directly to the filter object in the controller.
     */
    private void editReportFilter() {


        InternshipFilter filter = controller.getReportFilter();

        while (true) {
            System.out.println("\n--- Edit Report Filters ---");
            System.out.println("Active Filters: " + filter.toString(true));
            System.out.println("1. Set Status Filter");
            System.out.println("2. Set Level Filter");
            System.out.println("3. Contains Title");
            System.out.println("4. Contains Company Name");
            System.out.println("5. Set Preferred Major");
            System.out.println("6. Reset All Filters");
            System.out.println("7. Back to menu");

            int choice = InputHandler.getIntInput("Enter choice: ", 1, 7);

            switch (choice) {
                case 1 -> {
                    while (true) {
                        String s = InputHandler.getOptionalStringInput(
                                "Enter status (PENDING/APPROVED/REJECTED/FILLED or blank to clear): ");
                        if (s.isBlank()) { filter.setStatus(null); break; }
                        try {
                            filter.setStatus(InternshipStatus.valueOf(s.toUpperCase()));
                            break;
                        } catch (IllegalArgumentException e) { System.out.println("Invalid status."); }
                    }
                }
                case 2 -> {
                    while (true) {
                        String l = InputHandler.getOptionalStringInput(
                                "Enter level (BASIC/INTERMEDIATE/ADVANCED or blank to clear): ");
                        if (l.isBlank()) { filter.setLevel(null); break; }
                        try {
                            filter.setLevel(InternshipLevel.valueOf(l.toUpperCase()));
                            break;
                        } catch (IllegalArgumentException e) { System.out.println("Invalid level."); }
                    }
                }
                case 3 -> {
                    String title = InputHandler.getOptionalStringInput("Enter text for title (or blank to clear): ");
                    filter.setTitleContains(title.isBlank() ? null : title);
                }
                case 4 -> {
                    String co = InputHandler.getOptionalStringInput("Enter text for company (or blank to clear): ");
                    filter.setCompanyNameContains(co.isBlank() ? null : co);
                }
                case 5 -> {
                    String major = InputHandler.getOptionalStringInput("Enter preferred major (or blank to clear): ");
                    filter.setPreferredMajor(major.isBlank() ? null : major);
                }
                case 6 -> {
                    filter.reset();
                    System.out.println("All report filters have been reset.");
                }
                case 7 -> { return; }
            }
        }
    }

    /**
     * Fetches and displays a list of all Company Representative
     * accounts that are currently PENDING approval.
     */
    private void viewPendingCompanyReps() {
        List<CompanyRep> pendingCompanyReps = controller.getPendingCompanyReps();
        int num = 0;
        System.out.println("\n--- Pending Company Rep Accounts ---");
        System.out.println("-------------------------------------");
        System.out.println("  Company Rep Name (id: email) - Company name [Status]");
        System.out.println("  Description: ");
        System.out.println("-------------------------------------");
        for (CompanyRep rep : pendingCompanyReps) {
            num += 1;
            System.out.print(num + ") ");
            System.out.println(rep.toString());
        }
        System.out.println("-------------------------------------");
    }

    /**
     * Guides the staff member through approving or rejecting a
     * pending Company Representative account.
     */
    private void processCompanyRepApproval() {
        String repId = InputHandler.getStringInput("\n Insert ID of company rep to process: ");
        String choice = InputHandler.getStringInput("Approve internship? (y/n - type anything else to cancel): ");
        switch (choice) {
            case "y" -> {
                boolean success = controller.approveCompanyRep(repId);
                if (success) {System.out.println("Successful approval");}
                else {System.out.println("Error: Company Rep doesn't exist");}
            }
            case "n" -> {
                boolean success = controller.rejectCompanyRep(repId);
                if (success) {System.out.println("Successful rejection");}
                else {System.out.println("Error: Company Rep doesn't exist");}
            }
            default -> System.out.println("Cancelling process, account remains as pending...");
        }
    }

    /**
     * Fetches and displays all applications for which a student
     * has requested withdrawal.
     */
    private void viewWithdrawalRequests() {
        List<Application> withdrawalRequests = controller.getWithdrawalRequests();
        System.out.println("==== List of withdrawable applications ====");
        ApplicationView.displayDetailedApplications(withdrawalRequests);
    }

    /**
     * Guides the staff member through approving a student's
     * withdrawal request. (Note: Rejection is not currently an option
     * in this UI method).
     */
    private void processWithdrawalRequest() {
        int applicationId = InputHandler.getIntInput("\n Insert application ID of withdrawal request to process: ");
        if (controller.getWithdrawalRequests().stream().noneMatch(availableApp -> availableApp.getApplicationID() == applicationId)) {
            System.out.println("Error: Withdrawal Request doesn't exist");
            return;
        }
        String choice = InputHandler.getOptionalStringInput("Approve withdrawal request? (y to approve): ");
        switch (choice.toLowerCase()) {
            case "y" -> {
                boolean success = controller.approveWithdrawal(applicationId);
                if (success) {System.out.println("Successful approval");}
                else {System.out.println("Error: Withdrawal Request doesn't exist");}
            }
            default -> System.out.println("Cancelling process, withdrawal request remains unprocessed");
        }
    }

    /**
     * Fetches and displays all internships that are
     * currently PENDING approval.
     */
    private void viewPendingInternships() {
        List<Internship> pendingInternships = controller.getPendingInternships();
        InternshipView.displayStaffView(pendingInternships);
    }

    /**
     * Guides the staff member through approving or rejecting a
     * pending internship opportunity.
     */
    private void processPendingInternship() {
        int internshipId = InputHandler.getIntInput("\n Insert ID of internship to process: ");
        String choice = InputHandler.getStringInput("Approve internship? (y/n - type anything else to cancel): ");
        switch (choice) {
            case "y" -> {
                boolean success = controller.approveInternship(internshipId);
                if (success) {System.out.println("Successful approval");}
                else {System.out.println("Error: Internship doesn't exist");}
            }
            case "n" -> {
                boolean success = controller.rejectInternship(internshipId);
                if (success) {System.out.println("Successful rejection");}
                else {System.out.println("Error: Internship doesn't exist");}
            }
            default -> System.out.println("Cancelling process, internship remains as pending...");
        }
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


}
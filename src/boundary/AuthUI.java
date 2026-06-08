package boundary;

import controller.*;
import datastore.DataStore;
import user.*;
/**
 * The main boundary class for handling user authentication.
 * It provides the initial command-line interface (CLI) menu for users to
 * either log in or register for a new Company Representative account.
 * This class orchestrates the authentication flow by capturing user input
 * and passing it to the {@link AuthController}.
 *
 * @version 1.0
 */
public class AuthUI {

    private final AuthController authController;
    /**
     * Constructs a new AuthUI with a reference to the authentication controller.
     *
     * @param authController The controller responsible for authentication logic.
     */
    public AuthUI(AuthController authController, DataStore store) {
        this.authController = authController;
    }


    /**
     * Starts the main authentication loop.
     * Displays the Auth Menu (Login, Register, Exit) and prompts the user for a choice.
     * This loop continues until the user successfully logs in (returns true)
     * or chooses to exit the application (returns false).
     *
     * @return {@code true} if the user successfully logged in, {@code false} if the user exited.
     */
    public boolean start() {

        while (true) {

            System.out.println("\n======= AUTH MENU =======");
            System.out.println("1. Login");
            System.out.println("2. Register as Company Representative");
            System.out.println("3. Exit");

            int choice = InputHandler.getIntInput("Enter choice: ", 1, 3);

            switch (choice) {
                case 1 -> {
                    if (login()) {
                        return true;
                    }
                }

                case 2 -> this.registerCompanyRep();

                case 3 -> {
                    System.out.println("Exiting...");
                    return false;
                }

                default -> System.out.println("Try again.");
            }
        }
    }


    /**
     * Handles the user login process.
     * Prompts the user to select their role (Student, Company Rep, Staff)
     * and then enter their ID and password.
     *
     * @return {@code true} if authentication was successful, {@code false} otherwise.
     */
    public boolean login() {
        System.out.println("==== LOGIN ====");
        System.out.println("Select user type:");
        System.out.println("1. Student");
        System.out.println("2. Company Representative");
        System.out.println("3. Career Staff");
        int choice = InputHandler.getIntInput("Enter choice: ", 1, 3);

        String id = InputHandler.getStringInput("Enter ID: ");
        String password = InputHandler.getStringInput("Enter password: ");

        return(authController.autheticate(choice, id, password));
    }

    /**
     * Handles the registration process for a new Company Representative.
     * Gathers all required details from the user via the console,
     * creates a new {@link CompanyRep} object with a "PENDING" status,
     * and adds it to the representative store via the {@link AuthController}.
     * <p>
     * The new user's ID is set to their email address.
     */
    public void registerCompanyRep() {
        System.out.println("=== Register as Company Representative ===");

        String password, name, company, dept, position, email;
        
        
        email = InputHandler.getStringInput("Enter email: ");
        password = InputHandler.getStringInput("Enter password: ");
        name = InputHandler.getStringInput("Enter your full name: ");
        company = InputHandler.getStringInput("Enter company name: ");
        dept = InputHandler.getStringInput("Enter department: ");
        position = InputHandler.getStringInput("Enter position: ");
        
        CompanyRep rep = new CompanyRep(
                email, // using email as ID
                password,
                name,
                company,
                dept,
                position,
                email,
                "PENDING"    // new reps start as PENDING
        );

        rep.setStatus("PENDING"); // ensure not active yet

        authController.getRepStore().add(rep);

        System.out.println("Registration submitted. Career Staff must approve before login.");
    }
}
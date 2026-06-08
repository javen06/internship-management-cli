package boundary;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * A static utility class for handling and validating console input.
 * This class provides methods to reliably get integers, strings, dates,
 * and booleans from the user, with built-in validation and re-prompting.
 * <p>
 * This class is not intended to be instantiated.
 *
 * @version 1.0
 */
public class InputHandler {
    private final static Scanner scanner = new Scanner(System.in);

    /**
     * Prompts the user for an integer within a specified range [min, max].
     * It will re-prompt the user until a valid integer in that range is entered.
     *
     * @param prompt The message to display to the user.
     * @param min The minimum inclusive integer value.
     * @param max The maximum inclusive integer value.
     * @return The validated integer from the user.
     */
    public static int getIntInput(String prompt, int min, int max) {
        int input = -1;
        while (input < min || input > max) {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input < min || input > max) {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        return input;
    }

    /**
     * Prompts the user for a non-empty string.
     * It will re-prompt the user until they enter at least one non-whitespace character.
     *
     * @param prompt The message to display to the user.
     * @return The validated, non-empty, trimmed string from the user.
     */
    public static String getStringInput(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Please input a String.");
        }
    }

    /**
     * Prompts the user for a string, which can be empty.
     *
     * @param prompt The message to display to the user.
     * @return The string entered by the user (can be empty).
     */
    public static String getOptionalStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }


    /**
     * Prompts the user for any non-negative integer (0 or greater).
     * It will re-prompt the user until a valid non-negative integer is entered.
     *
     * @param prompt The message to display to the user.
     * @return The validated, non-negative integer from the user.
     */
    public static int getIntInput(String prompt) {
        int input = -1;
        while (input < 0) {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input < 0) {
                    System.out.println("Please enter only positive number ");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        return input;
    }

    /**
     * Prompts the user for a boolean value.
     * It only accepts "true" or "false" (case-insensitive) and will
     * re-prompt until valid input is received.
     *
     * @param prompt The message to display to the user.
     * @return {@code true} if the user enters "true", {@code false} if they enter "false".
     */
    public static boolean getBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("true")) {
                return true;
            }
            if (input.equals("false")) {
                return false;
            }
            System.out.println("Invalid input. Please type 'true' or 'false'.");
        }
    }

    /**
     * Prompts the user for a date in "yyyy-MM-dd" format.
     * It will re-prompt the user until a valid date format is entered
     * that is also not before the specified minimum date.
     *
     * @param prompt The message to display to the user.
     * @param minDate The earliest allowable date. If the user enters a date
     * before this, they will be re-prompted.
     * @return The validated {@link LocalDate} from the user.
     */
    public static LocalDate getDateInput(String prompt, LocalDate minDate) {
        LocalDate date = null;
        while (date == null) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            try {
                date = LocalDate.parse(input);
                if (minDate != null && date.isBefore(minDate)) {
                    System.out.println("Date must not be before " + minDate.toString() + ". Please try again.");
                    date = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
        return date;
    }

}
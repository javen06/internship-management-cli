
package boundary.commonDisplays;
import java.util.List;
import placement.Internship;
/**
 * Provides static utility methods for displaying lists of Internship objects
 * in different formats based on the user type.
 * <p>
 * This class is not intended to be instantiated. All methods are static.
 *
 * @version 1.0
 */
public class InternshipView {

    /**
     * Displays a list of internships from the perspective of a Student.
     * This view includes details relevant to an applicant, such as title,
     * description, level, major, dates, company, and slot availability.
     * <p>
     * If the list is empty, it prints a message indicating no internships
     * are available.
     *
     * @param internships The list of {@link Internship} objects to display.
     */    public static void displayStudentView(List<Internship> internships) {
        System.out.println("==================================================================================");
        System.out.println("                       Available Internships");
        System.out.println("==================================================================================");

        if (internships.isEmpty()) {
            System.out.println("No internships are currently available that match your profile.");
            return;
        }

        for (Internship i : internships) {
            System.out.printf("""
                            - %s (%s)
                              | %s
                              Level: %s
                              Major: %s
                              Open: %s | Close: %s
                              Company: %s
                              Slots: %d/%d
                            
                            """,
                    i.getTitle(), i.getInternshipId(),
                    i.getDescription(),
                    i.getLevel(),
                    i.getPreferredMajor(),
                    i.getOpenDate(),
                    i.getCloseDate(),
                    i.getCompanyName(),
                    i.getFilledSlots(),
                    i.getTotalSlots()
            );
        }
    }

    /**
     * Displays a comprehensive list of internships from the perspective of an
     * admin or anyone who can see visibility status.
     * This view includes all student-facing details plus administrative
     * information like visibility and status.
     * <p>
     * If the list is empty, it prints a message indicating no internships
     * match the filter.
     *
     * @param internships The list of {@link Internship} objects to display.
     */
    public static void displayStaffView(List<Internship> internships) {
        System.out.println("==================================================================================");
        System.out.println("                 Internships");
        System.out.println("==================================================================================");

        if (internships.isEmpty()) {
            System.out.println("No internships match the filter.");
            return;
        }
        for (Internship i : internships) {
            System.out.printf("""
                            - %s (%s)
                              | %s
                              Level: %s
                              Major: %s
                              Open: %s | Close: %s
                              Company: %s (%s)
                              Slots: %d/%d
                              Visible: %s
                              Status: %s
                            
                            """,
                    i.getTitle(),
                    i.getInternshipId(),
                    i.getDescription(),
                    i.getLevel(),
                    i.getPreferredMajor(),
                    i.getOpenDate(),
                    i.getCloseDate(),
                    i.getCompanyName(),
                    i.getCompanyRep().getUserId(),
                    i.getFilledSlots(),
                    i.getTotalSlots(),
                    i.isVisible() ? "Yes" : "No",
                    i.getStatus()
            );
        }
    }


}
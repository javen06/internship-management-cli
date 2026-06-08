package boundary.commonDisplays;

import java.util.List;
import placement.Application;
/**
 * Provides static utility methods for displaying lists of Application objects
 * in a consistent format to the console.
 * <p>
 * This class is not intended to be instantiated. All methods are static.
 *
 * @version 1.0
 */
public class ApplicationView {


    /**
     * Displays a simple, numbered list of applications with basic details.
     * Used for quick overviews where full detail is not required, such as in the
     * student's "withdraw application" menu.
     *
     * @param list The list of {@link Application} objects to display.
     */
    public static void displayApplications(List<Application> list) {
        int num = 0;
        System.out.println("== Application List ==");
        System.out.println("-------------------------------------");
        System.out.println("  Application ID, Title, Company, Application Status, Date Applied");
        System.out.println("  Description: ");
        System.out.println("-------------------------------------");
        for (Application application : list) {
            num += 1;
            System.out.println(num + ") " +
                    application.getApplicationID() +
                    ", " + application.getInternship().getTitle() +
                    ", " + application.getInternship().getCompanyName() +
                    ", " + application.getStatus()+
                    ", " + application.getSubmittedAt());
            System.out.println("Description: " + application.getInternship().getDescription());
        }
        System.out.println("-------------------------------------");
    }

    /**
     * Displays a detailed, multi-line formatted view of each application.
     * This view includes student, internship, and company representative details.
     * If the provided list is null or empty, it prints a "No applications found." message.
     *
     * @param apps The list of {@link Application} objects to display in detail.
     */
    public static void displayDetailedApplications(List<Application> apps) {
        if (apps == null || apps.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }
        for (Application a : apps) {
            System.out.printf("""
                    Application ID: %d
                    Student: %s (%s)
                    Internship: %s (ID: %d) 
                    Company Represented: %s
                    Company representative: %s
                    Status: %s
                    Submitted at: %s
                    ----------------------------------
                    """,
                    a.getApplicationID(),
                    a.getStudent().getUserName(),
                    a.getStudent().getUserId(),
                    a.getInternship().getTitle(),
                    a.getInternship().getInternshipId(),
                    a.getInternship().getCompanyName(),
                    a.getInternship().getCompanyRep().getUserName(),
                    a.getApplicationStatus(),
                    a.getSubmittedAt()
            );
        }
    }
}

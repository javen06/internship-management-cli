package initiation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import placement.Application;
import placement.ApplicationStatus;
import placement.Internship;
import user.*;

/**
 * Builds a list of {@link Application} objects from a CSV data file.
 * This class reads application data and links it to existing {@link Student}
 * and {@link Internship} objects from the provided {@link datastore.DataStore}.
 *
 * @version 1.0
 */
public class ApplicationBuilder {
    /**
     * Parses the application CSV file and creates a list of Application objects.
     *
     * @param csvPath The file path to the applications CSV file.
     * @param store The main {@link datastore.DataStore} instance, used to
     * retrieve the Student and Internship objects associated with each application.
     * @return A list of {@link Application} objects populated from the CSV.
     */
    public List<Application> build(String csvPath, datastore.DataStore store) {
        List<Application> applications = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                //ApplicationID,StudentID,InternshipID,Status,SubmittedAt,AcceptedByStudent,StudentWithdrawalRequested,CareerStaffWithdrawalAccepted
                int id = Integer.parseInt(data[0].trim());
                Student stud= store.getStudentStore().getById(data[1].trim());
                Internship intern = store.getInternshipStore().getById(Integer.parseInt(data[2].trim()));
                ApplicationStatus status = ApplicationStatus.valueOf(data[3].trim());
                LocalDateTime submittedAt = LocalDateTime.parse(data[4].trim());
                boolean Accepted = data[5].trim().equalsIgnoreCase("true");
                boolean WithdrawalRequest = data[6].trim().equalsIgnoreCase("true");
                boolean WithdrawalAString  = data[7].trim().equalsIgnoreCase("true");


                applications.add(new Application(
                        id,
                        stud,
                        intern,
                        status,
                        submittedAt,
                        Accepted,
                        WithdrawalRequest,
                        WithdrawalAString
                ));
                if (status == ApplicationStatus.ACCEPTED){
                    intern.addFilledSlot();
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading applications: " + e.getMessage());
        }

        return applications;
    }
}
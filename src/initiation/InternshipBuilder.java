package initiation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import placement.Internship;
import placement.InternshipLevel;
import user.CompanyRep;

/**
 * Builds a list of {@link Internship} objects from a CSV data file.
 * This class reads internship data and links it to existing {@link CompanyRep}
 * objects from the provided {@link datastore.DataStore}.
 *
 * @version 1.0
 */
public class InternshipBuilder {
    /**
     * Parses the internship CSV file and creates a list of Internship objects.
     * It also sets the initial approval and visibility status based on the CSV data.
     *
     * @param csvPath The file path to the internships CSV file.
     * @param store The main {@link datastore.DataStore} instance, used to
     * retrieve the {@link CompanyRep} object associated with each internship.
     * @return A list of {@link Internship} objects populated from the CSV.
     */
    public List<Internship> build(String csvPath, datastore.DataStore store) {
        List<Internship> intern = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                //id,title,description,requiredDegree,level,companyRepId,companyName,startDate,endDate,isApproved,isVisible
                int id = Integer.parseInt(data[0].trim());
                String title = data[1].trim();
                String desc = data[2].trim();
                String major = data[3].trim();
                InternshipLevel level = InternshipLevel.valueOf(data[4].trim());
                CompanyRep rep = store.getRepStore().getById(data[5].trim());
                String companyName =  data[6].trim();
                LocalDateTime startDate = LocalDateTime.parse(data[7].trim());
                LocalDateTime endDate = LocalDateTime.parse(data[8].trim());
                boolean IsApproved = data[9].trim().equalsIgnoreCase("true");
                boolean IsVisible = data[10].trim().equalsIgnoreCase("true");
                int totalSlot = Integer.parseInt(data[11].trim());

                //int id, String title, String desc, String major, InternshipLevel level,CompanyRep rep, String company, LocalDate open, LocalDate close
                Internship temp = new Internship(id,
                        title,
                        desc,
                        major,
                        level,
                        rep,
                        companyName,
                        totalSlot,
                        startDate.toLocalDate(),
                        endDate.toLocalDate()
                );
                if(IsVisible)
                    temp.toggleVisibility();
                if(IsApproved)
                    temp.markApproved();
                intern.add(temp);
            }

        } catch (Exception e) {
            System.out.println("⚠ Error reading internships: " + e.getMessage());
        }

        return intern;
    }
}
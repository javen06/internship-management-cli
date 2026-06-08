//package initiation;
//
//public class CompanyRepBuilder extends UserBuilder<CompanyRepBuilder> {
//    private String companyName;
//    private String department;
//    private String position;
//    private String email;
//    private String status;
//
//    public CompanyRepBuilder setCompanyName(String companyName) {
//        this.companyName = companyName;
//        return this;
//    }
//
//    public CompanyRepBuilder setDepartment(String department) {
//        this.department = department;
//        return this;
//    }
//
//    public CompanyRepBuilder setPosition(String position) {
//        this.position = position;
//        return this;
//    }
//
//    public CompanyRepBuilder setEmail(String email) {
//        this.email = email;
//        return this;
//    }
//
//    public CompanyRepBuilder setStatus(String status) {
//        this.status = status;
//        return this;
//    }
//
//    @Override
//    public CompanyRep build() {
//        return new CompanyRep(id, password, name, companyName, department, position, email, status);
//    }
//}


package initiation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import user.CompanyRep;

/**
 * Builds a list of {@link CompanyRep} objects from a CSV data file.
 * This class extends the abstract {@link UserBuilder}.
 *
 * @version 1.0
 */
public class CompanyRepBuilder extends UserBuilder<CompanyRep> {

    /**
     * Parses the company representative CSV file and creates a list of CompanyRep objects.
     * All users are assigned a default password "password".
     * If status is not specified in the CSV, it defaults to "PENDING".
     *
     * @param csvPath The file path to the company representative CSV file.
     * @return A list of {@link CompanyRep} objects.
     */
    @Override
    public List<CompanyRep> build(String csvPath) {
        List<CompanyRep> reps = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");

                // CSV must match your file:
                // ID,Name,Company,Department,Position,Email,Status
                String id = data[0].trim();
                String name = data[1].trim();
                String company = data[2].trim();
                String department = data[3].trim();
                String position = data[4].trim();
                String email = data[5].trim();
                String status = data.length > 6 ? data[6].trim() : "PENDING";

                reps.add(new CompanyRep(
                        id,
                        "password",
                        name,
                        company,
                        department,
                        position,
                        email,
                        status
                ));
            }

        } catch (Exception e) {
            System.out.println("Error reading company reps: " + e.getMessage());
        }

        return reps;
    }
}
//package initiation;
//
//public class CareerStaffBuilder extends UserBuilder<CareerStaffBuilder> {
//    private String email;
//    private String staffDepartment;
//
//    public CareerStaffBuilder setEmail(String email) {
//        this.email = email;
//        return this;
//    }
//
//    public CareerStaffBuilder setStaffDepartment(String staffDepartment) {
//        this.staffDepartment = staffDepartment;
//        return this;
//    }
//
//    @Override
//    public CareerStaff build() {
//        return new CareerStaff(id, password, name, email, staffDepartment);
//    }
//}

package initiation;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import user.CareerStaff;

/**
 * Builds a list of {@link CareerStaff} objects from a CSV data file.
 * This class extends the abstract {@link UserBuilder}.
 *
 * @version 1.0
 */
public class CareerStaffBuilder extends UserBuilder<CareerStaff> {
    /**
     * Parses the career staff CSV file and creates a list of CareerStaff objects.
     * All users are assigned a default password "password".
     *
     * @param csvPath The file path to the career staff CSV file.
     * @return A list of {@link CareerStaff} objects.
     */
    @Override
    public List<CareerStaff> build(String csvPath) {
        List<CareerStaff> staff = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");

                String id = data[0].trim();
                String name = data[1].trim();
                //String role = data[2].trim();
                String department = data[3].trim();
                String email = data[4].trim();

                staff.add(new CareerStaff(
                        id,
                        "password",
                        name,
                        department
                ));
            }

        } catch (Exception e) {
            System.out.println("Error reading career staffs: " + e.getMessage());
        }

        return staff;
    }
}
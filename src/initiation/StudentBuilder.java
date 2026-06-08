//package initiation;
//
//public class StudentBuilder extends UserBuilder<StudentBuilder> {
//    private String major;
//    private String year;
//
//    public StudentBuilder setMajor(String major) {
//        this.major = major;
//        return this;
//    }
//
//    public StudentBuilder setYear(String year) {
//        this.year = year;
//        return this;
//    }
//
//    @Override
//    public Student build() {
//        return new Student(id, password, name, major, year);
//    }
//}

package initiation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import user.Student;

/**
 * Builds a list of {@link Student} objects from a CSV data file.
 * This class extends the abstract {@link UserBuilder}.
 *
 * @version 1.0
 */
public class StudentBuilder extends UserBuilder<Student> {
    /**
     * Parses the student CSV file and creates a list of Student objects.
     * All users are assigned a default password "password".
     *
     * @param csvPath The file path to the student CSV file.
     * @return A list of {@link Student} objects.
     */
    @Override
    public List<Student> build(String csvPath) {
        List<Student> stud = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");

                String id = data[0].trim();
                String name = data[1].trim();
                String major = data[2].trim();
                String year = data[3].trim();
                //String email = data[4].trim();

                stud.add(new Student(
                        id,
                        "password",
                        name,
                        major,
                        year//,
                        //email
                ));
            }

        } catch (Exception e) {
            System.out.println("⚠ Error reading students: " + e.getMessage());
        }

        return stud;
    }
}
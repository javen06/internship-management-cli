package user;

/**
 * Represents a Career Center Staff user.
 * This class extends {@link User} and adds staff-specific
 * information such as their department. This user type has
 * administrative privileges, such as approving accounts and internships.
 *
 * @version 1.0
 */
public class CareerStaff extends User {
    private String department;
    //private String email; // This field is commented out in the original code

    /**
     * Constructs a new CareerStaff user.
     *
     * @param id The staff's ID (typically their NTU account).
     * @param password The staff's password.
     * @param name The staff's full name.
     * @param department The department the staff belongs to (e.g., "Internship Office").
     */
    public CareerStaff(String id, String password, String name, String department) {
        super(id, password, name);
        //this.email = email;
        this.department = department;
    }

    //public String getEmail() { return email; }

    /**
     * Gets the staff's department.
     *
     * @return The staff's department name.
     */
    public String getDepartment() { return department; }
}
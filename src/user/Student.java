package user;

/**
 * Represents a Student user in the system.
 * This class extends {@link User} and adds student-specific
 * information such as major and year of study.
 *
 * @version 1.0
 */
public class Student extends User {
    private String major;
    private int year;

    /**
     * Constructs a new Student.
     * The year of study is converted from String to int during construction.
     *
     * @param id The student's unique ID (e.g., U2345123F).
     * @param password The student's password.
     * @param name The student's full name.
     * @param major The student's major (e.g., CSC, EEE).
     * @param year The student's year of study as a String (e.g., "1", "2", "3", "4").
     */
    public Student(String id, String password, String name, String major, String year) {
        super(id, password, name);
        this.major = major;
        this.year = Integer.parseInt(year.trim());
    }

    /**
     * Gets the student's major.
     *
     * @return The student's major (e.g., "CSC").
     */
    public String getMajor() { return major; }

    /**
     * Gets the student's year of study.
     *
     * @return The student's year as an integer.
     */
    public int getYear() { return year; }
}
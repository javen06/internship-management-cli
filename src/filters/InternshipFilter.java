package filters;

import java.time.LocalDate;
import placement.Internship;
import placement.InternshipLevel;
import placement.InternshipStatus;

/**
 * Implements {@link FilterCriteria} for {@link Internship} objects.
 * This class holds a wide range of criteria (e.g., status, major, level, dates)
 * and checks if a given Internship object matches them.
 *
 * @version 1.0
 */
public class InternshipFilter implements FilterCriteria<Internship>{
    private InternshipStatus status;
    private String preferredMajor;
    private InternshipLevel level;
    private LocalDate latestClosingDate;    // Show internships closing before this
    private LocalDate earliestClosingDate;
    private LocalDate latestOpeningDate;
    private Boolean studentVisibility;
    private String companyName;
    private String companyRepId;
    private String titleContains;
    private String companyNameContains;

    /**
     * Empty constructor. Creates a filter with no criteria set.
     */
    public InternshipFilter() {}

    /**
     * Constructs a new InternshipFilter with specified criteria.
     * Null values for any parameter will cause that criterion to be ignored.
     *
     * @param status The {@link InternshipStatus} to filter by.
     * @param preferredMajor The preferred major to filter by.
     * @param level The {@link InternshipLevel} to filter by.
     * @param latestClosingDate Filter for internships closing on or before this date.
     * @param earliestClosingDate Filter for internships closing on or after this date.
     * @param latestOpeningDate Filter for internships opening on or before this date.
     * @param studentVisibility Filter for visibility status (true/false).
     * @param companyName Filter for an exact company name match.
     * @param companyRepId Filter for an exact company rep ID match.
     */
    public InternshipFilter(InternshipStatus status, String preferredMajor, InternshipLevel level, LocalDate latestClosingDate, LocalDate earliestClosingDate, LocalDate latestOpeningDate , Boolean studentVisibility, String companyName, String companyRepId) {

        this.status = status;
        this.preferredMajor = preferredMajor;
        this.level = level;
        this.latestClosingDate = latestClosingDate;
        this.earliestClosingDate = earliestClosingDate;
        this.latestOpeningDate = latestOpeningDate;
        this.studentVisibility = studentVisibility;
        this.companyName = companyName;
        this.companyRepId = companyRepId;
    }

    /**
     * Checks if a given {@link Internship} object matches all set criteria.
     *
     * @param item The Internship object to test.
     * @return {@code true} if the item matches all non-null criteria,
     * {@code false} otherwise.
     */
    @Override
    public boolean matches(Internship item) {
        if (status != null && item.getStatus() != status) return false;
        if (preferredMajor != null && !item.getPreferredMajor().equalsIgnoreCase(preferredMajor)) return false;
        if (level != null && item.getLevel() != level) return false;
        if (latestClosingDate != null && item.getCloseDate().isAfter(latestClosingDate)) return false;
        if (earliestClosingDate != null && item.getCloseDate().isBefore(earliestClosingDate)) return false;
        if (latestOpeningDate != null && item.getOpenDate().isAfter(latestOpeningDate)) return false;
        if (studentVisibility != null && studentVisibility && !item.isVisible()) return false;
        if (companyName != null && !item.getCompanyName().equalsIgnoreCase(companyName)) return false;
        if (companyRepId != null && !item.getCompanyRep().getUserId().equalsIgnoreCase(companyRepId)) return false;
        if (titleContains != null && !item.getTitle().toLowerCase().contains(titleContains.toLowerCase())) return false;
        if (companyNameContains != null && !item.getCompanyName().toLowerCase().contains(companyNameContains.toLowerCase())) return false;
        return true;
    }

    /**
     * Resets all filter criteria to null, effectively clearing the filter.
     */
    public void reset() {
        this.status = null;
        this.preferredMajor = null;
        this.level = null;
        this.latestClosingDate = null;
        this.earliestClosingDate = null;
        this.studentVisibility = null;
        this.companyName = null;
        this.companyRepId = null;
        this.titleContains = null;
        this.companyNameContains = null;
    }

    /** @return The current status filter, or null if not set. */
    public InternshipStatus getStatus() { return status; }
    /** @param status The status to filter by, or null to clear. */
    public void setStatus(InternshipStatus status) { this.status = status; }

    /** @return The current preferred major filter, or null if not set. */
    public String getPreferredMajor() { return preferredMajor; }
    /** @param preferredMajor The major to filter by, or null to clear. */
    public void setPreferredMajor(String preferredMajor) { this.preferredMajor = preferredMajor; }

    /** @return The current level filter, or null if not set. */
    public InternshipLevel getLevel() { return level; }
    /** @param level The level to filter by, or null to clear. */
    public void setLevel(InternshipLevel level) { this.level = level; }

    /** @return The current latest closing date filter, or null if not set. */
    public LocalDate getLatestClosingDate() { return latestClosingDate; }
    /** @param latestClosingDate The date to filter by, or null to clear. */
    public void setLatestClosingDate(LocalDate latestClosingDate) { this.latestClosingDate = latestClosingDate; }

    /** @return The current earliest closing date filter, or null if not set. */
    public LocalDate getEarliestClosingDate() { return earliestClosingDate; }
    /** @param earliestClosingDate The date to filter by, or null to clear. */
    public void setEarliestClosingDate(LocalDate earliestClosingDate) { this.earliestClosingDate = earliestClosingDate; }

    /** @return The current latest opening date filter, or null if not set. */
    public LocalDate getLatestOpeningDate() { return latestOpeningDate; }
    /** @param earliestOpeningDate The date to filter by, or null to clear. */
    public void setLatestOpeningDate(LocalDate earliestOpeningDate) { this.latestOpeningDate = earliestOpeningDate; }

    /** @return The current visibility filter, or null if not set. */
    public Boolean getStudentVisibility() { return studentVisibility; }
    /** @param studentVisibility The visibility to filter by, or null to clear. */
    public void setStudentVisibility(Boolean studentVisibility) { this.studentVisibility = studentVisibility; }

    /** @return The current company name filter, or null if not set. */
    public String getCompanyName() { return companyName; }
    /** @param companyName The company name to filter by, or null to clear. */
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    /** @return The current company name "contains" filter, or null if not set. */
    public String getCompanyNameContains() { return companyName; }
    /** @param companyName The company name text to filter by, or null to clear. */
    public void setCompanyNameContains(String companyName) { this.companyName = companyName; }

    /** @return The current company rep ID filter, or null if not set. */
    public String getCompanyRepId() { return companyRepId; }
    /** @param companyRepId The rep ID to filter by, or null to clear. */
    public void setCompanyRepId(String companyRepId) { this.companyRepId = companyRepId; }

    /** @return The current title "contains" filter, or null if not set. */
    public String getTitleContains() { return titleContains; }
    /** @param titleContains The title text to filter by, or null to clear. */
    public void setTitleContains(String titleContains) { this.titleContains = titleContains; }


    /**
     * Generates a human-readable string representation of all active filters.
     *
     * @param withVisibility {@code true} to include visibility-related filters
     * in the string, {@code false} to hide them.
     * @return A formatted string listing all currently set filter criteria.
     */
    public String toString(boolean withVisibility) {
        StringBuilder sb = new StringBuilder();
        sb.append("Active Internship Filters:\n");

        if (status != null)
            sb.append(" • Status = ").append(status).append("\n");

        if (preferredMajor != null)
            sb.append(" • Preferred Major = ").append(preferredMajor).append("\n");

        if (level != null)
            sb.append(" • Level = ").append(level).append("\n");

        if (latestClosingDate != null)
            sb.append(" • Closing Before = ").append(latestClosingDate).append("\n");

        if (earliestClosingDate != null)
            sb.append(" • Closing After = ").append(earliestClosingDate).append("\n");

        if (latestOpeningDate != null && withVisibility)
            sb.append(" • Opening Before  = ").append(latestOpeningDate).append("\n");

        if (studentVisibility != null && withVisibility)
            sb.append(" • Visible Only = ").append(studentVisibility ? "Yes" : "No").append("\n");

        if (companyName != null)
            sb.append(" • Company Name = ").append(companyName).append("\n");

        if (companyRepId != null)
            sb.append(" • Company Rep ID = ").append(companyRepId).append("\n");
        if (titleContains != null)
            sb.append(" • Title Contains = ").append(titleContains).append("\n");
        if (companyNameContains != null)
            sb.append(" • Company Name Contains = ").append(companyNameContains).append("\n");
        // If no filters active
        if (sb.toString().equals("Active Internship Filters:\n"))
            sb.append(" > (No filters applied)");

        return sb.toString();
    }


    /**
     * Copy constructor.
     * Creates a new InternshipFilter instance that is a deep copy
     * of another filter.
     *
     * @param other The {@link InternshipFilter} to copy.
     */
    public InternshipFilter(InternshipFilter other) {
        this.status = other.status;
        this.preferredMajor = other.preferredMajor;
        this.level = other.level;
        this.earliestClosingDate = other.earliestClosingDate;
        this.latestClosingDate = other.latestClosingDate;
        this.latestOpeningDate = other.latestOpeningDate;
        this.studentVisibility = other.studentVisibility;
        this.companyName = other.companyName;
        this.companyRepId = other.companyRepId;
        this.titleContains = other.titleContains;
        this.companyNameContains = other.companyNameContains;
    }
}
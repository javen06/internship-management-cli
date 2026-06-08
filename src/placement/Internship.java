package placement;

import java.time.LocalDate;
import user.CompanyRep;

/**
 * Represents an internship opportunity posted by a {@link CompanyRep}.
 * This class holds all details about the internship, manages its status
 * ({@link InternshipStatus}), and tracks available slots.
 *
 * @version 1.0
 */
public class Internship {
    private final int internshipId;
    private String title;
    private String description;
    private String preferredMajor;
    private InternshipLevel level;
    private CompanyRep companyRep;
    private String companyName;       //we no company object we just use company name for now
    private LocalDate openDate;
    private LocalDate closeDate;
    private InternshipStatus status;
    private boolean visible;
    private int totalSlots;
    private int filledSlots;

    /**
     * Constructs a new Internship.
     * By default, status is set to PENDING and visibility is false.
     *
     * @param id The unique ID for the internship, from {@link datastore.InternshipStore}.
     * @param title The title of the internship.
     * @param desc A description of the role.
     * @param major The preferred major for applicants.
     * @param level The {@link InternshipLevel} (BASIC, INTERMEDIATE, ADVANCED).
     * @param rep The {@link CompanyRep} who created this internship.
     * @param company The name of the company.
     * @param slot The total number of available slots.
     * @param open The date applications open.
     * @param close The date applications close.
     */
    public Internship(int id, String title, String desc, String major, InternshipLevel level,
                      CompanyRep rep, String company, int slot, LocalDate open, LocalDate close) {
        this.internshipId = id;
        this.title = title;
        this.description = desc;
        this.preferredMajor = major;
        this.level = level;
        this.companyRep = rep;
        this.companyName = company;
        this.openDate = open;
        this.closeDate = close;
        this.totalSlots = slot;
        this.filledSlots = 0;
        this.visible = false;
        this.status = InternshipStatus.PENDING;
    }

    //generic standard gets
    /** @return The unique internship ID. */
    public int getInternshipId() { return internshipId; }
    /** @return The internship title. */
    public String getTitle() { return title; }
    /** @return The internship description. */
    public String getDescription() {return description; }
    /** @return The preferred major. */
    public String getPreferredMajor() { return preferredMajor; }
    /** @return The {@link InternshipLevel}. */
    public InternshipLevel getLevel() { return level; }
    /** @return The {@link InternshipStatus}. */
    public InternshipStatus getStatus() { return status; }
    /** @return {@code true} if the internship is visible to students, {@code false} otherwise. */
    public boolean isVisible() { return visible; }
    /** @return The number of slots that have been accepted by students. */
    public int getFilledSlots() { return filledSlots; }
    /** @return The application opening date. */
    public LocalDate getOpenDate() {return openDate;}
    /** @return The application closing date. */
    public LocalDate getCloseDate() {return closeDate;}
    /** @return The company's name. */
    public String getCompanyName() {return companyName;}
    /** @return The {@link CompanyRep} responsible for this internship. */
    public CompanyRep getCompanyRep() {return companyRep;}
    /** @return The total number of slots available. */
    public int getTotalSlots() { return totalSlots; }


    //generic sets
    /** @param title The new title. */
    public void setTitle(String title) {this.title = title;}
    /** @param description The new description. */
    public void setDescription(String description) {this.description = description;}
    /** @param preferredMajor The new preferred major. */
    public void setPreferredMajor(String preferredMajor) {this.preferredMajor = preferredMajor;}
    /** @param level The new {@link InternshipLevel}. */
    public void setLevel(InternshipLevel level) {this.level = level;}
    /** @param companyRep The new {@link CompanyRep}. */
    public void setCompanyRep(CompanyRep companyRep) {this.companyRep = companyRep;}
    /** @param totalSlots The new total slot count. */
    public void setTotalSlots(int totalSlots) {this.totalSlots = totalSlots;}
    /** @param openDate The new opening date. */
    public void setOpenDate(LocalDate openDate) {this.openDate = openDate;}
    /** @param closeDate The new closing date. */
    public void setCloseDate(LocalDate closeDate) {this.closeDate = closeDate;}
    /** @param companyName The new company name. */
    public void setCompanyName(String companyName) {this.companyName = companyName;}

    //setters with certain restrictions
    //Career staff related actions

    /** Sets the status to APPROVED. Called by {@link user.CareerStaff}. */
    public void markApproved() { this.status = InternshipStatus.APPROVED; }
    /** Sets the status to REJECTED. Called by {@link user.CareerStaff}. */
    public void markRejected() { this.status = InternshipStatus.REJECTED; }

    //career staff and company rep
    /** Toggles the visibility of the internship to students. */
    public void toggleVisibility() { this.visible = !visible; }

    //automatic
    /** Sets the status to FILLED. Called automatically when all slots are filled. */
    public void markFilled() { this.status = InternshipStatus.FILLED; }

    /**
     * Increments the filled slot count.
     * Called when a student accepts an application for this internship.
     * Automatically marks the internship as FILLED if all slots are taken.
     */
    public void addFilledSlot() {   //this should likely be called at the student controller when the student accepts
        if (filledSlots < totalSlots) filledSlots++;
        if (filledSlots == totalSlots) markFilled();
    }

    /**
     * Decrements the filled slot count.
     * Called when a student's accepted application is withdrawn and approved.
     * Automatically marks the internship as APPROVED if it was previously FILLED.
     */
    public void removeFilledSlot(){
        if (filledSlots > 0) filledSlots--;
        if (filledSlots < totalSlots && this.status == InternshipStatus.FILLED) markApproved(); // Re-open it
    }
}
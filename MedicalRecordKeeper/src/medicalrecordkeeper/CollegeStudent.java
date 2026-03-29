package medicalrecordkeeper;

import java.io.Serializable;
import java.time.LocalDateTime;

/** College student data, extends Person. Serializable. */
public class CollegeStudent extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String department;     // Department (e.g., Engineering)
    private String course;         // Course (e.g., BS Computer Science)
    private String yearLevel;      // Year level (e.g., 2nd Year)

    /** Sets type to College. */
    public CollegeStudent() {
        super();
        this.type = "College";
    }

    public String getDepartment() { return department; }
    /** Sets department, updates last modified. */
    public void setDepartment(String department) { this.department = department; this.lastModified = LocalDateTime.now(); }
    public String getCourse() { return course; }
    /** Sets course, updates last modified. */
    public void setCourse(String course) { this.course = course; this.lastModified = LocalDateTime.now(); }
    public String getYearLevel() { return yearLevel; }
    /** Sets year level, updates last modified. */
    public void setYearLevel(String yearLevel) { this.yearLevel = yearLevel; this.lastModified = LocalDateTime.now(); }
}
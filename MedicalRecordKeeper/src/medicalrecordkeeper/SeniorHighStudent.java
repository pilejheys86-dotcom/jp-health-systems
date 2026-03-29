package medicalrecordkeeper;

import java.io.Serializable;
import java.time.LocalDateTime;

/** Senior high student data, extends Person. Serializable. */
public class SeniorHighStudent extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String strand;         // Academic strand (e.g., STEM)
    private String gradeLevel;     // Grade level (e.g., 11)

    /** Sets type to Senior High School. */
    public SeniorHighStudent() {
        super();
        this.type = "Senior High School";
    }

    public String getStrand() { return strand; }
    /** Sets strand, updates last modified. */
    public void setStrand(String strand) { this.strand = strand; this.lastModified = LocalDateTime.now(); }
    public String getGradeLevel() { return gradeLevel; }
    /** Sets grade level, updates last modified. */
    public void setGradeLevel(String gradeLevel) { this.gradeLevel = gradeLevel; this.lastModified = LocalDateTime.now(); }
}
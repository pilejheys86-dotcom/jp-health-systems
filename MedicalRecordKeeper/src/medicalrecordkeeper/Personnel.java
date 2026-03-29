package medicalrecordkeeper;

import java.io.Serializable;
import java.time.LocalDateTime;

/** Personnel data, extends Person. Serializable. */
public class Personnel extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean isFaculty;     // Faculty status flag

    /** Sets type to Personnel. */
    public Personnel() {
        super();
        this.type = "Personnel";
    }

    public boolean isFaculty() { return isFaculty; }
    /** Sets faculty status, updates last modified. */
    public void setFaculty(boolean faculty) { isFaculty = faculty; this.lastModified = LocalDateTime.now(); }
    /** Returns role based on faculty status. */
    public String getRole() { return isFaculty ? "Faculty" : "Non-teaching Personnel"; }
}
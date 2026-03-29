
package medicalrecordkeeper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Logs user activity with timestamp and details. Serializable. */
public class ActivityLogEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDateTime timestamp;
    private String personName;
    private String personType;
    private String action;

    public ActivityLogEntry(LocalDateTime timestamp, String personName, String personType, String action) {
        this.timestamp = timestamp;
        this.personName = personName;
        this.personType = personType;
        this.action = action;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getPersonName() { return personName; }
    public String getPersonType() { return personType; }
    public String getAction() { return action; }
    public String getFormattedDate() { return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); }
    public String getFormattedTime() { return timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss")); }
}
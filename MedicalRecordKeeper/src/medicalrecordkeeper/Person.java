package medicalrecordkeeper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Patient data with personal info, health conditions, and exam history. Serializable. */
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String surname;                       // Last name
    protected String givenName;                    // First name
    protected String middleName;                   // Middle name
    protected String idNumber;                     // Unique ID
    protected String barangay;                     // Barangay address
    protected String municipality;                 // Municipality address
    protected String province;                     // Province address
    protected String country;                      // Country address
    protected String contactNumber;                // Phone number
    protected LocalDate birthday;                  // Date of birth
    protected int age;                             // Current age
    protected String sex;                          // Gender
    protected String status;                       // Marital status
    protected String religion;                     // Religion
    protected String emergencyContactSurname;      // Emergency contact last name
    protected String emergencyContactGivenName;    // Emergency contact first name
    protected String emergencyContactMiddleName;   // Emergency contact middle name
    protected String emergencyContactNumber;       // Emergency contact phone
    protected String covidStatus;                  // COVID-19 status
    protected String vaccineType;                  // Vaccine received

    protected boolean hasAllergy;                  // Allergy flag
    protected boolean hasAsthma;                   // Asthma flag
    protected boolean hasTuberculosis;             // TB flag
    protected boolean hasDiabetes;                 // Diabetes flag
    protected boolean hasHeartAilment;             // Heart condition flag
    protected boolean hasHypertension;             // Hypertension flag
    protected boolean hasKidneyDisease;            // Kidney disease flag
    protected boolean hasGyneIssues;               // Gynecological issues flag
    protected boolean isSmoker;                    // Smoking flag
    protected boolean isAlcoholicDrinker;          // Alcohol use flag
    protected boolean hasPreviousHospitalization;  // Past hospitalization flag

    protected List<PhysicalExamEntry> examHistory = new ArrayList<>();  // Exam records

    protected String type;                         // Person type (e.g., patient)

    protected LocalDateTime createdAt;             // Creation timestamp
    protected LocalDateTime lastModified;          // Last update timestamp

    /** Sets creation and last modified to now. */
    public Person() {
        this.createdAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
    }

    /** Returns full name (surname, givenName middleName). */
    public String getFullName() {
        return surname + ", " + givenName + " " + (middleName != null ? middleName : "");
    }

    /** Calculates age from birthday. */
    public void calculateAge() {
        if (birthday != null) {
            this.age = Period.between(birthday, LocalDate.now()).getYears();
        }
    }

    /** Returns copy of exam history. */
    public List<PhysicalExamEntry> getExamHistory() {
        return new ArrayList<>(examHistory);
    }

    /** Adds exam entry, updates last modified. */
    public void addExamEntry(PhysicalExamEntry entry) {
        examHistory.add(entry);
        this.lastModified = LocalDateTime.now();
    }
    
    /** Removes exam entry by index, updates last modified. */
    public void removeExamEntry(int index) {
        if (index >= 0 && index < examHistory.size()) {
            examHistory.remove(index);
            this.lastModified = LocalDateTime.now();
        }
    }

    public String getSurname() { return surname; }
    /** Sets surname, updates last modified. */
    public void setSurname(String surname) { this.surname = surname; this.lastModified = LocalDateTime.now(); }
    public String getGivenName() { return givenName; }
    /** Sets given name, updates last modified. */
    public void setGivenName(String givenName) { this.givenName = givenName; this.lastModified = LocalDateTime.now(); }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastModified() { return lastModified; }
    public String getType() { return type; }
    public String getIdNumber() { return idNumber; }
}
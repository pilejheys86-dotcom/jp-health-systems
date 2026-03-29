package medicalrecordkeeper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Manages medical records and activity logs with singleton pattern. */
public class DatabaseManager {
    private static DatabaseManager instance;
    private List<Person> records;
    private List<ActivityLogEntry> activityLog;
    private static final String DATA_FILE = "medical_records.dat";

    private DatabaseManager() {
        records = new ArrayList<>();
        activityLog = new ArrayList<>();
        loadData();
    }

    /** Returns singleton instance. */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /** Adds person to records and logs action. */
    public void addRecord(Person person) {
        records.add(person);
        logActivity(person.getFullName(), person.getType(), "Added record");
        saveData();
    }

    /** Updates record with specific action and logs it. */
    public void updateRecord(Person person, String specificAction) {
        logActivity(person.getFullName(), person.getType(), specificAction);
        saveData();
    }

    /** Deletes record by ID and logs action. */
    public void deleteRecord(Person person) {
        records.removeIf(p -> p.getIdNumber().equals(person.getIdNumber()));
        logActivity(person.getFullName(), person.getType(), "Deleted record");
        saveData();
    }

    // Overloaded method for backward compatibility
    public void updateRecord(Person person) {
        updateRecord(person, "Modified record"); // Default action
    }

    public List<Person> getAllRecords() { return new ArrayList<>(records); }

    /** Filters records by type. */
    public List<Person> getRecordsByType(String type) {
        List<Person> filteredRecords = new ArrayList<>();
        for (Person person : records) {
            if (person.getType().equals(type)) {
                filteredRecords.add(person);
            }
        }
        return filteredRecords;
    }

    /** Logs an activity entry. */
    private void logActivity(String personName, String personType, String action) {
        ActivityLogEntry entry = new ActivityLogEntry(LocalDateTime.now(), personName, personType, action);
        activityLog.add(entry);
    }

    /** Returns sorted activity log (newest first). */
    public List<ActivityLogEntry> getActivityLog() {
        List<ActivityLogEntry> sortedLog = new ArrayList<>(activityLog);
        sortedLog.sort(Comparator.comparing(ActivityLogEntry::getTimestamp).reversed());
        return sortedLog;
    }

    public void clearActivityLog() {
        activityLog.clear();
        saveData();
    }

    /** Saves records and logs to file. */
    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(records);
            oos.writeObject(activityLog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Loads data from file or initializes empty lists. */
    private void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
                records = (List<Person>) ois.readObject();
                activityLog = (List<ActivityLogEntry>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            records = new ArrayList<>();
            activityLog = new ArrayList<>();
        }
    }
}
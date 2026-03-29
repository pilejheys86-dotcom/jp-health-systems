# JP Health Systems - Medical Record Keeper

A Java Swing desktop application for managing medical records in educational institutions. It supports three client types — Senior High School students, College students, and Personnel — with full CRUD operations, physical examination tracking, and activity logging.

## Features

### Authentication
- Login screen with username and password fields
- Show/hide password toggle
- Branded login panel with institutional logo

### Dashboard
- Left-side navigation panel with quick access to Activity Log, View Records, and Add Record
- Card-based layout that switches between views seamlessly

### Activity Log
- Tracks all record operations (create, update, delete) with timestamps
- Displays date, time, person name, client type, and action performed
- Supports refreshing and clearing the log

### Record Management
- **View Records** — Browse all records or filter by tab: Senior High School, College, or Personnel
- **Add Record** — Create new records using type-specific forms
- **Edit Record** — Update existing records with all fields pre-filled
- **Delete Record** — Remove records with confirmation
- Records are displayed as tiles sorted alphabetically by surname

### Client Types

| Type | Specific Fields |
|------|----------------|
| **Senior High School** | Strand (ABM, HUMSS, STEM), Grade Level (11–12) |
| **College** | Department (SEAT, SEAS, STHM, SBA), Program (14 courses), Year Level (1st–4th) |
| **Personnel** | Faculty or Non-teaching classification |

### Record Form (4 Tabs)

**1. Personal Information**
- Full name, ID number, address (barangay, municipality, province, country)
- Contact number, birthday with auto-calculated age, sex, marital status, religion
- Emergency contact details
- COVID-19 vaccination status and vaccine type

**2. Medical History**
- Checkbox-based tracking for 11 conditions: allergies, asthma, tuberculosis, diabetes, heart ailment, hypertension, kidney disease, gynecological/obstetrical issues, smoking, alcohol use, and previous hospitalization
- Gynecological checkbox auto-disabled for male clients

**3. Physical Examination**
- Record vitals: blood pressure (systolic/diastolic), pulse rate, oxygen saturation, weight, temperature
- Text fields for chief complaint, medical attendant, diagnosis, and treatment

**4. Examination History**
- Table view of all past physical exams for a client
- Ability to delete individual exam entries

### Data Validation
- Regex-based validation on names, ID numbers, contact numbers, and other fields
- ID format enforcement per client type (e.g., `20XX-XXXXXXX` for students, `22-XXXX` for personnel)
- Contact number format: `XXX-XXX-XXXX`
- Required field checks with descriptive error dialogs

## Technical Details

| Aspect | Detail |
|--------|--------|
| Language | Java 22 |
| GUI Framework | Swing |
| Build System | Ant (NetBeans project) |
| Persistence | Java Object Serialization (`medical_records.dat`) |
| Entry Point | `medicalrecordkeeper.MedicalRecordKeeper` |
| Source Files | 12 classes |

### Architecture
- **Singleton** — `DatabaseManager` ensures a single instance manages all data access
- **Inheritance** — `Person` base class extended by `CollegeStudent`, `SeniorHighStudent`, and `Personnel`
- **Abstract Form** — `BaseRecordForm` provides shared form logic; subclasses add type-specific fields
- **Serialization** — Records and activity logs are persisted as serialized Java objects to a local `.dat` file

### Project Structure

```
MedicalRecordKeeper/
├── src/medicalrecordkeeper/
│   ├── MedicalRecordKeeper.java      # Login UI + BaseRecordForm abstract class
│   ├── Dashboard.java                # Main dashboard with navigation and views
│   ├── DatabaseManager.java          # Singleton data manager (serialization)
│   ├── Person.java                   # Base class for all client types
│   ├── CollegeStudent.java           # College student model
│   ├── SeniorHighStudent.java        # Senior high student model
│   ├── Personnel.java                # Personnel model
│   ├── PhysicalExamEntry.java        # Physical exam data model
│   ├── ActivityLogEntry.java         # Activity log entry model
│   ├── CollegeStudentForm.java       # College student record form
│   ├── SeniorHighSchoolForm.java     # Senior high student record form
│   ├── PersonnelForm.java           # Personnel record form
│   └── resources/                    # Logo images
├── build.xml                         # Ant build configuration
└── medical_records.dat               # Serialized data file
```

## How to Run

1. Open the project in NetBeans or any IDE that supports Ant builds
2. Build and run `medicalrecordkeeper.MedicalRecordKeeper` as the main class
3. Alternatively, run the built JAR from `dist/MedicalRecordKeeper.jar`

PileJTK

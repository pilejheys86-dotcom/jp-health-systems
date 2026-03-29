package medicalrecordkeeper;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static medicalrecordkeeper.BaseRecordForm.COLLEGE_ID_REGEX;


/** Form for College student records. */
public class CollegeStudentForm extends BaseRecordForm {
    private JComboBox<String> departmentComboBox;
    private JComboBox<String> programComboBox;
    private JComboBox<String> yearLevelComboBox;

    public CollegeStudentForm(Dashboard parent, CollegeStudent student) {
        super(parent, student);
    }

    @Override
    protected void createPersonInstance() {
        this.person = new CollegeStudent();
    }

    @Override
    protected void addSpecificFields(JPanel panel, GridBagConstraints gbc, int startRow) {
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        
        idNumberField.setToolTipText("20XX-XXXXXXX (e.g., 2023-1234567)");

        gbc.gridx = 0; gbc.gridy = startRow;
        gbc.weightx = 0;
        panel.add(new JLabel("Department:"), gbc);

        gbc.gridx = 1; gbc.gridy = startRow;
        gbc.weightx = 0;
        gbc.insets = new Insets(10, 5, 10, 10);
        departmentComboBox = new JComboBox<>(new String[] {"SEAT", "SEAS", "STHM", "SBA"});
        departmentComboBox.setPreferredSize(new Dimension(100, 25));
        departmentComboBox.addActionListener(e -> updateProgramComboBox());
        panel.add(departmentComboBox, gbc);

        gbc.gridx = 2; gbc.gridy = startRow;
        gbc.weightx = 0;
        gbc.insets = new Insets(10, 20, 10, 20);
        panel.add(Box.createHorizontalStrut(30), gbc);

        gbc.gridx = 3; gbc.gridy = startRow;
        gbc.weightx = 0;
        gbc.insets = new Insets(10, 10, 10, 5);
        panel.add(new JLabel("Program:"), gbc);

        gbc.gridx = 4; gbc.gridy = startRow;
        gbc.weightx = 0;
        gbc.insets = new Insets(10, 5, 10, 10);
        programComboBox = new JComboBox<>();
        programComboBox.setPreferredSize(new Dimension(200, 25));
        panel.add(programComboBox, gbc);

        gbc.gridx = 5; gbc.gridy = startRow;
        gbc.weightx = 0;
        gbc.insets = new Insets(10, 20, 10, 20);
        panel.add(Box.createHorizontalStrut(30), gbc);

        gbc.gridx = 6; gbc.gridy = startRow;
        gbc.weightx = 0;
        gbc.insets = new Insets(10, 10, 10, 5);
        panel.add(new JLabel("Year Level:"), gbc);

        gbc.gridx = 7; gbc.gridy = startRow;
        gbc.weightx = 0;
        gbc.insets = new Insets(10, 5, 10, 10);
        yearLevelComboBox = new JComboBox<>(new String[] {"1st Year", "2nd Year", "3rd Year", "4th Year"});
        yearLevelComboBox.setPreferredSize(new Dimension(100, 25));
        panel.add(yearLevelComboBox, gbc);
    }

    /** Updates program options based on selected department. */
    private void updateProgramComboBox() {
        String department = (String) departmentComboBox.getSelectedItem();
        String[] programs = getProgramsForDepartment(department);
        programComboBox.setModel(new DefaultComboBoxModel<>(programs));
    }

    /** Returns programs for a given department. */
    private String[] getProgramsForDepartment(String department) {
        if (department == null) return new String[] {};
        switch (department) {
            case "SEAT": return new String[] {
                "BSIT - Bachelor of Science in Information Technology",
                "BS ARCH - Bachelor of Science in Architecture",
                "BSCE - Bachelor of Science in Civil Engineering",
                "BSCpE - Bachelor of Science in Computer Engineering"
            };
            case "SEAS": return new String[] {
                "BPED - Bachelor of Physical Education",
                "BSPSY - Bachelor of Science in Psychology",
                "AB BELS - Bachelor of Arts in English Language Studies",
                "AB ECONOMICS - Bachelor of Arts in Economics"
            };
            case "SBA": return new String[] {
                "BS Accountancy - Bachelor of Science in Accountancy",
                "BSBA - MktgMgt - Bachelor of Science in Marketing Management",
                "BSBA - FinMgt - Bachelor of Science in Financial Management",
                "BSA IS - Bachelor of Science in Information System"
            };
            case "STHM": return new String[] {
                "BSTM - Bachelor of Science in Tourism Management",
                "BSHM - Bachelor of Science in Hospitality Management"
            };
            default: return new String[] {};
        }
    }

    @Override
    protected int getLastRowIndex() {
        return 5;
    }

    @Override
    protected void setSpecificFormData() {
        if (person instanceof CollegeStudent) {
            CollegeStudent student = (CollegeStudent) person;
            student.setDepartment((String) departmentComboBox.getSelectedItem());
            student.setCourse((String) programComboBox.getSelectedItem());
            student.setYearLevel((String) yearLevelComboBox.getSelectedItem());
        }
    }

    @Override
    protected void populateSpecificFields() {
        if (person instanceof CollegeStudent) {
            CollegeStudent student = (CollegeStudent) person;
            departmentComboBox.setSelectedItem(student.getDepartment());
            updateProgramComboBox();
            programComboBox.setSelectedItem(student.getCourse());
            yearLevelComboBox.setSelectedItem(student.getYearLevel());
        }
    }

    /** Validates College-specific fields. */
    @Override
    protected boolean validateSpecificFields() {
        if (departmentComboBox.getSelectedItem() == null) {
            showValidationError("Department is required");
            return false;
        }

        if (programComboBox.getSelectedItem() == null) {
            showValidationError("Program is required");
            return false;
        }

        if (yearLevelComboBox.getSelectedItem() == null) {
            showValidationError("Year Level is required");
            return false;
        }

        String idNumber = idNumberField.getText().trim();
        if (!idNumber.matches(COLLEGE_ID_REGEX)) {
            showValidationError("College ID must follow format 20XX-XXXXXXX (e.g., 2023-1234567)");
            return false;
        }

        return true;
    }
}
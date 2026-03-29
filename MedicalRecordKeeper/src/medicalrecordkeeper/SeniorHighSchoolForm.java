package medicalrecordkeeper;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static medicalrecordkeeper.BaseRecordForm.SENIOR_ID_REGEX;

/** Form for Senior High School student records. */
public class SeniorHighSchoolForm extends BaseRecordForm {
    private JComboBox<String> strandComboBox;
    private JComboBox<String> gradeLevelComboBox;

    public SeniorHighSchoolForm(Dashboard parent, SeniorHighStudent student) {
        super(parent, student);
    }

    @Override
    protected void createPersonInstance() {
        this.person = new SeniorHighStudent();
    }

    @Override
    protected void addSpecificFields(JPanel panel, GridBagConstraints gbc, int startRow) {
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        
        idNumberField.setToolTipText("20XX-XXXXXXX (e.g., 2023-1234567)");

        gbc.gridx = 0; gbc.gridy = startRow;
        gbc.weightx = 0;
        panel.add(new JLabel("Strand:"), gbc);

        gbc.gridx = 1; gbc.gridy = startRow;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        strandComboBox = new JComboBox<>(new String[] {
            "ABM - Accountancy, Business, and Management",
            "HUMSS - Humanities and Social Sciences",
            "STEM - Science, Technology, Engineering, and Mathematics"
        });
        strandComboBox.setPreferredSize(new Dimension(200, 25));
        panel.add(strandComboBox, gbc);

        gbc.gridx = 2; gbc.gridy = startRow;
        gbc.weightx = 0;
        panel.add(Box.createHorizontalStrut(20), gbc);

        gbc.gridx = 3; gbc.gridy = startRow;
        gbc.weightx = 0;
        panel.add(new JLabel("Grade Level:"), gbc);

        gbc.gridx = 4; gbc.gridy = startRow;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gradeLevelComboBox = new JComboBox<>(new String[] {"Grade 11", "Grade 12"});
        gradeLevelComboBox.setPreferredSize(new Dimension(100, 25));
        panel.add(gradeLevelComboBox, gbc);
    }

    @Override
    protected int getLastRowIndex() {
        return 3;
    }

    @Override
    protected void setSpecificFormData() {
        if (person instanceof SeniorHighStudent) {
            SeniorHighStudent student = (SeniorHighStudent) person;
            student.setStrand((String) strandComboBox.getSelectedItem());
            student.setGradeLevel((String) gradeLevelComboBox.getSelectedItem());
        }
    }

    @Override
    protected void populateSpecificFields() {
        if (person instanceof SeniorHighStudent) {
            SeniorHighStudent student = (SeniorHighStudent) person;
            strandComboBox.setSelectedItem(student.getStrand());
            gradeLevelComboBox.setSelectedItem(student.getGradeLevel());
        }
    }

    /** Validates Senior High-specific fields. */
    @Override
    protected boolean validateSpecificFields() {
        if (strandComboBox.getSelectedItem() == null) {
            showValidationError("Strand is required");
            return false;
        }

        if (gradeLevelComboBox.getSelectedItem() == null) {
            showValidationError("Grade Level is required");
            return false;
        }

        String idNumber = idNumberField.getText().trim();
        if (!idNumber.matches(SENIOR_ID_REGEX)) {
            showValidationError("Senior High School ID must follow format 20XX-XXXXXXX (e.g., 2023-1234567)");
            return false;
        }

        return true;
    }
}
package medicalrecordkeeper;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import static medicalrecordkeeper.BaseRecordForm.PERSONNEL_ID_REGEX;

/** Form for Personnel records. */
public class PersonnelForm extends BaseRecordForm {
    private JRadioButton facultyRadioButton;
    private JRadioButton nonTeachingRadioButton;
    private ButtonGroup roleButtonGroup;

    public PersonnelForm(Dashboard parent, Personnel personnel) {
        super(parent, personnel);
    }

    @Override
    protected void createPersonInstance() {
        this.person = new Personnel();
    }

    @Override
    protected void addSpecificFields(JPanel panel, GridBagConstraints gbc, int startRow) {
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        
        idNumberField.setToolTipText("22-XXXX (e.g., 22-1234)");

        gbc.gridx = 0; gbc.gridy = startRow;
        gbc.weightx = 0;
        panel.add(new JLabel("Role:"), gbc);

        roleButtonGroup = new ButtonGroup();

        gbc.gridx = 1; gbc.gridy = startRow;
        gbc.weightx = 0;
        gbc.insets = new Insets(10, 5, 10, 5);
        facultyRadioButton = new JRadioButton("Faculty");
        facultyRadioButton.setFont(new Font("Arial", Font.PLAIN, 12));
        roleButtonGroup.add(facultyRadioButton);
        panel.add(facultyRadioButton, gbc);

        gbc.gridx = 2; gbc.gridy = startRow;
        gbc.weightx = 0;
        gbc.insets = new Insets(10, 5, 10, 10);
        nonTeachingRadioButton = new JRadioButton("Non-teaching Personnel");
        nonTeachingRadioButton.setFont(new Font("Arial", Font.PLAIN, 12));
        roleButtonGroup.add(nonTeachingRadioButton);
        panel.add(nonTeachingRadioButton, gbc);
    }

    @Override
    protected int getLastRowIndex() {
        return 3;
    }

    @Override
    protected void setSpecificFormData() {
        if (person instanceof Personnel) {
            Personnel personnel = (Personnel) person;
            personnel.setFaculty(facultyRadioButton.isSelected());
        }
    }

    @Override
    protected void populateSpecificFields() {
        if (person instanceof Personnel) {
            Personnel personnel = (Personnel) person;
            facultyRadioButton.setSelected(personnel.isFaculty());
            nonTeachingRadioButton.setSelected(!personnel.isFaculty());
        }
    }

    /** Validates Personnel-specific fields. */
    @Override
    protected boolean validateSpecificFields() {
        if (!facultyRadioButton.isSelected() && !nonTeachingRadioButton.isSelected()) {
            showValidationError("Role (Faculty or Non-teaching) is required");
            return false;
        }

        String idNumber = idNumberField.getText().trim();
        if (!idNumber.matches(PERSONNEL_ID_REGEX)) {
            showValidationError("Personnel ID must follow format 22-XXXX (e.g., 22-1234)");
            return false;
        }

        return true;
    }
}
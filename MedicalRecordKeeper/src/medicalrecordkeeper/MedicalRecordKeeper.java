package medicalrecordkeeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.YearMonth; 
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.text.MaskFormatter;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

//Submitted by: Jheys A. Pile ITE242

public class MedicalRecordKeeper extends JFrame {

    private static final String USERNAME_PLACEHOLDER = "Username";
    private static final String PASSWORD_PLACEHOLDER = "Password";
    private static final Color DEFAULT_BUTTON_COLOR = new Color(0, 120, 215);
    private static final Color HOVER_BUTTON_COLOR = new Color(0, 140, 255);
    private static final Color CANCEL_DEFAULT_COLOR = new Color(200, 200, 200);
    private static final Color CANCEL_HOVER_COLOR = new Color(220, 220, 220);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckbox;
    private JButton okButton;
    private JButton cancelButton;

    private static final String VALID_USERNAME = "JeisuKeisu";
    private static final String VALID_PASSWORD = "Roku022504";

    public MedicalRecordKeeper() {
        setTitle("Login - Medical Record Keeper");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        createLoginForm(mainPanel);
        createPhotoPanel(mainPanel);
    }

    // Constructs the login form with input fields and buttons
    private void createLoginForm(JPanel mainPanel) {
        JPanel loginPanel = new JPanel(null);
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBounds(0, 0, 400, 400);
        mainPanel.add(loginPanel);

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBounds(50, 30, 400, 40);
        loginPanel.add(titleLabel);

        usernameField = createTextField(USERNAME_PLACEHOLDER, 50, 100, loginPanel);
        passwordField = createPasswordField(PASSWORD_PLACEHOLDER, 50, 160, loginPanel);
        
        // Checkbox to toggle password visibility
        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
        showPasswordCheckbox.setBounds(50, 210, 150, 30);
        showPasswordCheckbox.setBackground(Color.WHITE);
        showPasswordCheckbox.addActionListener(e -> togglePasswordVisibility());
        loginPanel.add(showPasswordCheckbox);

        okButton = createButton("OK", DEFAULT_BUTTON_COLOR, HOVER_BUTTON_COLOR, 50, 270, loginPanel, e -> performLogin());
        cancelButton = createButton("Cancel", CANCEL_DEFAULT_COLOR, CANCEL_HOVER_COLOR, 230, 270, loginPanel, e -> System.exit(0));
    }
    
    // Creates a styled text field with placeholder functionality
    private JTextField createTextField(String placeholder, int x, int y, JPanel panel) {
        JTextField textField = new JTextField(placeholder);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setForeground(Color.GRAY);
        textField.setBounds(x, y, 300, 40);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        addFocusEffect(textField, placeholder);
        panel.add(textField);
        return textField;
    }

    // Creates a password field with placeholder and visibility toggle support
    private JPasswordField createPasswordField(String placeholder, int x, int y, JPanel panel) {
        JPasswordField passwordField = new JPasswordField(placeholder);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setForeground(Color.GRAY);
        passwordField.setBounds(x, y, 300, 40);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        // Initially show placeholder as plain text
        passwordField.setEchoChar((char) 0);
        addFocusEffect(passwordField, placeholder);
        panel.add(passwordField);
        return passwordField;
    }
    
    // Creates a button with hover effect and specified action
    private JButton createButton(String text, Color defaultColor, Color hoverColor, int x, int y, JPanel panel, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBounds(x, y, 120, 40);
        button.setBackground(defaultColor);
        button.setForeground(defaultColor == CANCEL_DEFAULT_COLOR ? Color.BLACK : Color.WHITE);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButtonHoverEffect(button, defaultColor, hoverColor);
        button.addActionListener(actionListener);
        panel.add(button);
        return button;
    }
    
    // Adds an image or fallback text to the right side of the login window
    private void createPhotoPanel(JPanel mainPanel) {
        JPanel photoPanel = new JPanel(new BorderLayout());
        photoPanel.setBounds(400, 0, 400, 400);
        photoPanel.setBackground(Color.WHITE);

        JLabel photoLabel = new JLabel();
        photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        photoLabel.setVerticalAlignment(SwingConstants.CENTER);

        ImageIcon imageIcon = null;
        try {
            // Load image from resources folder
            imageIcon = new ImageIcon(getClass().getResource("/medicalrecordkeeper/resources/JP SYSTEMS.png"));
            if (imageIcon == null || imageIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                throw new Exception("Image not found or failed to load");
            }
            // Scale image if it exceeds panel dimensions
            if (imageIcon.getIconWidth() > 400 || imageIcon.getIconHeight() > 400) {
                int newWidth = 400;
                int newHeight = (int) ((double) 400 * imageIcon.getIconHeight() / imageIcon.getIconWidth());
                if (newHeight > 400) {
                    newHeight = 400;
                    newWidth = (int) ((double) 400 * imageIcon.getIconWidth() / imageIcon.getIconHeight());
                }
                Image scaledImage = imageIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(scaledImage);
            }
            photoLabel.setIcon(imageIcon);
        } catch (Exception e) {
            // Fallback to plain TXT if image loading fails
            photoLabel.setText("         JP \nHealth Systems");
            photoLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
            photoLabel.setForeground(Color.BLACK);
        }

        photoPanel.add(photoLabel, BorderLayout.CENTER);
        photoPanel.add(Box.createVerticalGlue(), BorderLayout.NORTH);
        photoPanel.add(Box.createVerticalGlue(), BorderLayout.SOUTH);

        mainPanel.add(photoPanel);
    }
    
    // Adds focus listener to handle placeholder text behavior
    private void addFocusEffect(JTextField field, String placeholder) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    if (field instanceof JPasswordField) {
                        ((JPasswordField) field).setEchoChar('•');
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                    if (field instanceof JPasswordField) {
                        ((JPasswordField) field).setEchoChar((char) 0);
                    }
                }
            }
        });
    }
    
    // Applies hover effect to buttons for visual feedback
    private void addButtonHoverEffect(JButton button, Color defaultColor, Color hoverColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultColor);
            }
        });
    }
    
    // Toggles password visibility based on checkbox state
    private void togglePasswordVisibility() {
        if (showPasswordCheckbox.isSelected()) {
            passwordField.setEchoChar((char) 0);
        } else {
            if (!passwordField.getText().equals(PASSWORD_PLACEHOLDER)) {
                passwordField.setEchoChar('•');
            }
        }
    }
    
    // Validates login credentials and opens dashboard on success
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.equals(USERNAME_PLACEHOLDER) || password.equals(PASSWORD_PLACEHOLDER) || username.isEmpty() || password.isEmpty()) {
            showErrorDialog("Field cannot be empty");
        } else if (!username.equals(VALID_USERNAME) || !password.equals(VALID_PASSWORD)) {
            showErrorDialog("Wrong username or password");
        } else {
            showSuccessDialog();
            dispose();
            SwingUtilities.invokeLater(() -> {
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
            });
        }
    }

    private void showSuccessDialog() {
        JOptionPane.showMessageDialog(this, "You have signed in successfully to JP Health Systems", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MedicalRecordKeeper login = new MedicalRecordKeeper();
            login.setVisible(true);
        });
    }
}

/** Base form for managing person records with tabs for personal, medical, and exam data. */
abstract class BaseRecordForm {
    protected static final Color PRIMARY_COLOR = new Color(0, 120, 215);
    protected static final Color SECONDARY_COLOR = Color.WHITE;
    protected static final Color TEXT_COLOR = Color.BLACK;
    protected static final Color BORDER_COLOR = new Color(200, 200, 200);
    protected static final Color BUTTON_HOVER_COLOR = new Color(0, 140, 255);
    protected static final Color DELETE_COLOR = new Color(220, 53, 69);
    protected static final Color DELETE_HOVER_COLOR = new Color(240, 73, 89);
    protected static final Color CANCEL_COLOR = new Color(200, 200, 200);
    protected static final Color CANCEL_HOVER_COLOR = new Color(220, 220, 220);

    protected JPanel mainPanel;
    protected JTabbedPane tabbedPane;
    protected Dashboard parentDashboard;
    protected DatabaseManager dbManager;
    protected Person person;
    protected boolean isNewRecord;

    protected JTextField surnameField;
    protected JTextField givenNameField;
    protected JTextField middleNameField;
    protected JTextField idNumberField;
    protected JTextField barangayField;
    protected JTextField municipalityField;
    protected JTextField provinceField;
    protected JTextField countryField;
    protected JFormattedTextField contactField;
    protected JComboBox<String> monthComboBox;
    protected JComboBox<Integer> dayComboBox;
    protected JComboBox<Integer> yearComboBox;
    protected JTextField ageField;
    protected JComboBox<String> sexComboBox;
    protected JComboBox<String> statusComboBox;
    protected JTextField religionField;
    protected JTextField emergencySurnameField;
    protected JTextField emergencyGivenNameField;
    protected JTextField emergencyMiddleNameField;
    protected JFormattedTextField emergencyContactField;
    protected JComboBox<String> covidStatusComboBox;
    protected JTextField vaccineTypeField;

    protected JCheckBox allergyCheckBox;
    protected JCheckBox asthmaCheckBox;
    protected JCheckBox tuberculosisCheckBox;
    protected JCheckBox diabetesCheckBox;
    protected JCheckBox heartAilmentCheckBox;
    protected JCheckBox hypertensionCheckBox;
    protected JCheckBox kidneyDiseaseCheckBox;
    protected JCheckBox gyneCheckBox;
    protected JCheckBox smokerCheckBox;
    protected JCheckBox alcoholicDrinkerCheckBox;
    protected JCheckBox previousHospitalizationCheckBox;

    protected JTextField bpSystolicField;
    protected JTextField bpDiastolicField;
    protected JTextField pulseRateField;
    protected JTextField oxygenSaturationField;
    protected JTextField weightField;
    protected JTextField temperatureField;
    protected JTextArea chiefComplaintArea;
    protected JTextArea diagnosisArea;
    protected JTextArea treatmentArea;
    protected JTextField medicalAttendantField;
    
    private static final String NAME_REGEX = "^([A-Z][a-z]*)([ '-][A-Z][a-z]*)*$";
    static final String SENIOR_ID_REGEX = "^20\\d{2}-\\d{7}$";
    static final String COLLEGE_ID_REGEX = "^20\\d{2}-\\d{7}$";
    static final String PERSONNEL_ID_REGEX = "^22-\\d{4}$";
    private static final String CONTACT_REGEX = "^\\d{3}-\\d{3}-\\d{4}$";
    private static final String RELIGION_REGEX = "^[a-zA-Z\\s]*$";
    private static final String VACCINE_TYPE_REGEX = "^[a-zA-Z\\s]*$";
    private static final String MEDICAL_ATTENDANT_REGEX = "^[a-zA-Z\\s\\.,]*$";

    private JTable examHistoryTable;
    private DefaultTableModel examHistoryTableModel;
    
    /** Clears physical exam fields. */
    private void clearPhysicalExamFields() {
        bpSystolicField.setText("");
        bpDiastolicField.setText("");
        pulseRateField.setText("");
        oxygenSaturationField.setText("");
        weightField.setText("");
        temperatureField.setText("");
        chiefComplaintArea.setText("");
        diagnosisArea.setText("");
        treatmentArea.setText("");
        medicalAttendantField.setText("");
    }

    public BaseRecordForm(Dashboard parent, Person person) {
        this.parentDashboard = parent;
        this.dbManager = DatabaseManager.getInstance();
        this.person = person;
        this.isNewRecord = (person == null);
        initializeUI();
        if (!isNewRecord) {
            populateFields();
        }
    }
    
    /** Updates day combo box based on month and year. */
    private void updateDayComboBox() {
        String month = (String) monthComboBox.getSelectedItem();
        Integer year = (Integer) yearComboBox.getSelectedItem();
        Integer selectedDay = (Integer) dayComboBox.getSelectedItem();

        if (month != null && year != null) {
            int monthIndex = java.util.Arrays.asList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            ).indexOf(month) + 1;
            YearMonth yearMonth = YearMonth.of(year, monthIndex);
            int daysInMonth = yearMonth.lengthOfMonth();

            Integer[] days = new Integer[daysInMonth];
            for (int i = 0; i < daysInMonth; i++) {
                days[i] = i + 1;
            }

            dayComboBox.setModel(new DefaultComboBoxModel<>(days));

            if (selectedDay != null && selectedDay <= daysInMonth) {
                dayComboBox.setSelectedItem(selectedDay);
            } else {
                dayComboBox.setSelectedIndex(daysInMonth - 1);
            }
        } else {
            Integer[] days = new Integer[31];
            for (int i = 0; i < 31; i++) {
                days[i] = i + 1;
            }
            dayComboBox.setModel(new DefaultComboBoxModel<>(days));
            if (selectedDay != null && selectedDay <= 31) {
                dayComboBox.setSelectedItem(selectedDay);
            }
        }
    }
    
    protected void initializeUI() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(SECONDARY_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setPreferredSize(new Dimension(500, 700));

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 12));
        tabbedPane.addTab("Personal Info", createPersonalInfoPanel());
        tabbedPane.addTab("Medical History", createMedicalHistoryPanel());
        tabbedPane.addTab("Physical Exam", createPhysicalExamPanel());
        tabbedPane.addTab("Exam History", createExamHistoryPanel());

        JScrollPane scrollPane = new JScrollPane(tabbedPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setBackground(SECONDARY_COLOR);

        JButton saveButton = createStyledButton("Save", PRIMARY_COLOR, BUTTON_HOVER_COLOR, e -> saveRecord());
        JButton cancelButton = createStyledButton("Cancel", CANCEL_COLOR, CANCEL_HOVER_COLOR, e -> parentDashboard.clearRightPanel());

        if (!isNewRecord) {
            JButton deleteButton = createStyledButton("Delete", DELETE_COLOR, DELETE_HOVER_COLOR, e -> deleteRecord());
            buttonPanel.add(deleteButton);
        }

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.setVisible(true);
    }

    public JPanel getContentPanel() {
        return mainPanel;
    }

    protected JButton createStyledButton(String text, Color defaultColor, Color hoverColor, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(defaultColor == CANCEL_COLOR ? Color.BLACK : Color.WHITE);
        button.setBackground(defaultColor);
        button.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { button.setBackground(hoverColor); }
            @Override
            public void mouseExited(MouseEvent e) { button.setBackground(defaultColor); }
        });
        button.addActionListener(actionListener);
        return button;
    }

    protected JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);

        JPanel mainFormPanel = new JPanel(new GridBagLayout());
        mainFormPanel.setBackground(SECONDARY_COLOR);
        mainFormPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JPanel personalPanel = createSectionPanel("Personal Details");
        personalPanel.setLayout(new GridBagLayout());
        GridBagConstraints personalGbc = new GridBagConstraints();
        personalGbc.insets = new Insets(5, 5, 5, 5);
        personalGbc.anchor = GridBagConstraints.WEST;
        personalGbc.fill = GridBagConstraints.HORIZONTAL;

        surnameField = new JTextField(15);
        givenNameField = new JTextField(15);
        middleNameField = new JTextField(15);
        idNumberField = new JTextField(10);
        idNumberField.setToolTipText("Enter ID number (format varies by record type)");

        int row = 0;
        addFieldRow(personalPanel, personalGbc, row++, "Surname*", surnameField);
        addFieldRow(personalPanel, personalGbc, row++, "Given Name*", givenNameField);
        addFieldRow(personalPanel, personalGbc, row++, "Middle Name", middleNameField);
        addFieldRow(personalPanel, personalGbc, row++, "ID Number*", idNumberField);

        JPanel specificPanel = createSectionPanel("Specific Details");
        specificPanel.setLayout(new GridBagLayout());
        GridBagConstraints specificGbc = new GridBagConstraints();
        specificGbc.insets = new Insets(5, 5, 5, 5);
        specificGbc.anchor = GridBagConstraints.WEST;
        specificGbc.fill = GridBagConstraints.HORIZONTAL;
        addSpecificFields(specificPanel, specificGbc, 0);

        JPanel addressPanel = createSectionPanel("Address");
        addressPanel.setLayout(new GridBagLayout());
        GridBagConstraints addressGbc = new GridBagConstraints();
        addressGbc.insets = new Insets(5, 5, 5, 5);
        addressGbc.anchor = GridBagConstraints.WEST;
        addressGbc.fill = GridBagConstraints.HORIZONTAL;
        barangayField = new JTextField(15);
        municipalityField = new JTextField(15);
        provinceField = new JTextField(15);
        countryField = new JTextField(15);

        row = 0;
        addFieldRow(addressPanel, addressGbc, row++, "Barangay*", barangayField);
        addFieldRow(addressPanel, addressGbc, row++, "Municipality/City*", municipalityField);
        addFieldRow(addressPanel, addressGbc, row++, "Province*", provinceField);
        addFieldRow(addressPanel, addressGbc, row++, "Country*", countryField);

        JPanel contactPanel = createSectionPanel("Contact Information");
        contactPanel.setLayout(new GridBagLayout());
        GridBagConstraints contactGbc = new GridBagConstraints();
        contactGbc.insets = new Insets(5, 5, 5, 5);
        contactGbc.anchor = GridBagConstraints.WEST;
        contactGbc.fill = GridBagConstraints.HORIZONTAL;
        contactGbc.weightx = 1.0;

        try {
            MaskFormatter contactFormatter = new MaskFormatter("###-###-####");
            contactFormatter.setPlaceholderCharacter('_');
            contactField = new JFormattedTextField(contactFormatter);
            contactField.setColumns(10);
            contactField.setPreferredSize(new Dimension(150, 25));
            contactField.setToolTipText("XXX-XXX-XXXX (e.g., 123-456-7890)");
        } catch (ParseException e) {
            contactField = new JFormattedTextField();
            contactField.setPreferredSize(new Dimension(150, 25));
            contactField.setToolTipText("XXX-XXX-XXXX (e.g., 123-456-7890)");
        }

        String[] months = {"January", "February", "March", "April", "May", "June", 
                           "July", "August", "September", "October", "November", "December"};
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setPreferredSize(new Dimension(120, 25));

        Integer[] days = new Integer[31];
        for (int i = 0; i < 31; i++) {
            days[i] = i + 1;
        }
        dayComboBox = new JComboBox<>(days);
        dayComboBox.setPreferredSize(new Dimension(60, 25));

        int currentYear = LocalDate.now().getYear();
        Integer[] years = new Integer[currentYear - 1900 + 1];
        for (int i = 0; i < years.length; i++) {
            years[i] = currentYear - i;
        }
        yearComboBox = new JComboBox<>(years);
        yearComboBox.setPreferredSize(new Dimension(80, 25));

        ageField = new JTextField(3);
        ageField.setEditable(false);
        ageField.setPreferredSize(new Dimension(40, 25));

        ActionListener dateUpdater = e -> {
            updateDayComboBox();
            calculateAgeFromBirthday();
        };
        monthComboBox.addActionListener(dateUpdater);
        yearComboBox.addActionListener(dateUpdater);
        dayComboBox.addActionListener(e -> calculateAgeFromBirthday());

        sexComboBox = new JComboBox<>(new String[] {"Male", "Female"});
        sexComboBox.setPreferredSize(new Dimension(150, 25));

        statusComboBox = new JComboBox<>(new String[] {"Single", "Married", "Widowed", "Legally Separated"});
        statusComboBox.setPreferredSize(new Dimension(150, 25));

        religionField = new JTextField(15);
        religionField.setPreferredSize(new Dimension(150, 25));

        row = 0;
        contactGbc.gridy = row;
        contactGbc.gridx = 0;
        contactGbc.weightx = 0;
        contactPanel.add(new JLabel("Contact No.*"), contactGbc);
        contactGbc.gridx = 1;
        contactGbc.weightx = 1;
        contactGbc.fill = GridBagConstraints.HORIZONTAL;
        contactGbc.anchor = GridBagConstraints.NORTH;
        contactPanel.add(contactField, contactGbc);

        row++;
        contactGbc.gridy = row;
        contactGbc.gridx = 0;
        contactGbc.weightx = 0;
        contactPanel.add(new JLabel("Birthday"), contactGbc);
        contactGbc.gridx = 1;
        contactGbc.weightx = 1;
        contactGbc.fill = GridBagConstraints.HORIZONTAL;
        contactGbc.anchor = GridBagConstraints.NORTH;
        JPanel birthdaySubPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        birthdaySubPanel.setBackground(SECONDARY_COLOR);
        birthdaySubPanel.add(monthComboBox);
        birthdaySubPanel.add(dayComboBox);
        birthdaySubPanel.add(yearComboBox);
        birthdaySubPanel.add(Box.createHorizontalStrut(10));
        birthdaySubPanel.add(new JLabel("Age:"));
        birthdaySubPanel.add(ageField);
        contactPanel.add(birthdaySubPanel, contactGbc);

        row++;
        contactGbc.gridy = row;
        contactGbc.gridx = 0;
        contactGbc.weightx = 0;
        contactPanel.add(new JLabel("Sex"), contactGbc);
        contactGbc.gridx = 1;
        contactGbc.weightx = 1;
        contactGbc.fill = GridBagConstraints.HORIZONTAL;
        contactGbc.anchor = GridBagConstraints.NORTH;
        contactPanel.add(sexComboBox, contactGbc);

        row++;
        contactGbc.gridy = row;
        contactGbc.gridx = 0;
        contactGbc.weightx = 0;
        contactPanel.add(new JLabel("Marital Status"), contactGbc);
        contactGbc.gridx = 1;
        contactGbc.weightx = 1;
        contactGbc.fill = GridBagConstraints.HORIZONTAL;
        contactGbc.anchor = GridBagConstraints.NORTH;
        contactPanel.add(statusComboBox, contactGbc);

        row++;
        contactGbc.gridy = row;
        contactGbc.gridx = 0;
        contactGbc.weightx = 0;
        contactPanel.add(new JLabel("Religion"), contactGbc);
        contactGbc.gridx = 1;
        contactGbc.weightx = 1;
        contactGbc.fill = GridBagConstraints.HORIZONTAL;
        contactGbc.anchor = GridBagConstraints.NORTH;
        contactPanel.add(religionField, contactGbc);

        JPanel emergencyPanel = createSectionPanel("Emergency Contact");
        emergencyPanel.setLayout(new GridBagLayout());
        GridBagConstraints emergencyGbc = new GridBagConstraints();
        emergencyGbc.insets = new Insets(5, 5, 5, 5);
        emergencyGbc.anchor = GridBagConstraints.WEST;
        emergencyGbc.fill = GridBagConstraints.HORIZONTAL;
        emergencySurnameField = new JTextField(15);
        emergencyGivenNameField = new JTextField(15);
        emergencyMiddleNameField = new JTextField(15);
        try {
            MaskFormatter emergencyContactFormatter = new MaskFormatter("###-###-####");
            emergencyContactFormatter.setPlaceholderCharacter('_');
            emergencyContactField = new JFormattedTextField(emergencyContactFormatter);
            emergencyContactField.setColumns(10);
            emergencyContactField.setPreferredSize(new Dimension(150, 25));
            emergencyContactField.setToolTipText("XXX-XXX-XXXX (e.g., 123-456-7890)");
        } catch (ParseException e) {
            emergencyContactField = new JFormattedTextField();
            emergencyContactField.setPreferredSize(new Dimension(150, 25));
            emergencyContactField.setToolTipText("XXX-XXX-XXXX (e.g., 123-456-7890)");
        }

        row = 0;
        addFieldRow(emergencyPanel, emergencyGbc, row++, "Surname*", emergencySurnameField);
        addFieldRow(emergencyPanel, emergencyGbc, row++, "Given Name*", emergencyGivenNameField);
        addFieldRow(emergencyPanel, emergencyGbc, row++, "Middle Name", emergencyMiddleNameField);
        addFieldRow(emergencyPanel, emergencyGbc, row++, "Contact No.*", emergencyContactField);

        JPanel covidPanel = createSectionPanel("COVID-19 Information");
        covidPanel.setLayout(new GridBagLayout());
        GridBagConstraints covidGbc = new GridBagConstraints();
        covidGbc.insets = new Insets(5, 5, 5, 5);
        covidGbc.anchor = GridBagConstraints.WEST;
        covidGbc.fill = GridBagConstraints.HORIZONTAL;
        covidStatusComboBox = new JComboBox<>(new String[] {"Fully Vaccinated", "Unvaccinated", "Fully Vaccinated with Booster Shots"});
        covidStatusComboBox.setPreferredSize(new Dimension(150, 25));
        vaccineTypeField = new JTextField(10);
        vaccineTypeField.setPreferredSize(new Dimension(150, 25));

        covidStatusComboBox.addActionListener(e -> {
            vaccineTypeField.setEnabled(!"Unvaccinated".equals(covidStatusComboBox.getSelectedItem()));
            if ("Unvaccinated".equals(covidStatusComboBox.getSelectedItem())) {
                vaccineTypeField.setText("");
            }
        });

        row = 0;
        addFieldRow(covidPanel, covidGbc, row++, "Vaccination Status*", covidStatusComboBox);
        addFieldRow(covidPanel, covidGbc, row++, "Vaccine Type", vaccineTypeField);

        row = 0;
        gbc.gridy = row++; gbc.weighty = 0;
        mainFormPanel.add(personalPanel, gbc);
        gbc.gridy = row++; gbc.weighty = 0;
        mainFormPanel.add(specificPanel, gbc);
        gbc.gridy = row++; gbc.weighty = 0;
        mainFormPanel.add(addressPanel, gbc);
        gbc.gridy = row++; gbc.weighty = 0;
        mainFormPanel.add(contactPanel, gbc);
        gbc.gridy = row++; gbc.weighty = 0;
        mainFormPanel.add(emergencyPanel, gbc);
        gbc.gridy = row++; gbc.weighty = 0;
        mainFormPanel.add(covidPanel, gbc);

        gbc.gridy = row; gbc.weighty = 1.0;
        mainFormPanel.add(Box.createVerticalGlue(), gbc);

        panel.add(mainFormPanel, BorderLayout.CENTER);

        JLabel noteLabel = new JLabel("* Required fields");
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        noteLabel.setForeground(TEXT_COLOR);
        panel.add(noteLabel, BorderLayout.SOUTH);

        return panel;
    }

    protected JPanel createMedicalHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(SECONDARY_COLOR);

        JPanel mainFormPanel = new JPanel(new GridBagLayout());
        mainFormPanel.setBackground(SECONDARY_COLOR);
        mainFormPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JPanel conditionsPanel = createSectionPanel("Medical Conditions");
        conditionsPanel.setLayout(new GridBagLayout());
        GridBagConstraints conditionsGbc = new GridBagConstraints();
        conditionsGbc.insets = new Insets(5, 5, 5, 5);
        conditionsGbc.anchor = GridBagConstraints.WEST;
        conditionsGbc.fill = GridBagConstraints.HORIZONTAL;

        allergyCheckBox = new JCheckBox("Allergy");
        asthmaCheckBox = new JCheckBox("Asthma");
        tuberculosisCheckBox = new JCheckBox("Tuberculosis");
        diabetesCheckBox = new JCheckBox("Diabetes Mellitus");
        heartAilmentCheckBox = new JCheckBox("Heart Ailment");
        hypertensionCheckBox = new JCheckBox("Hypertension");
        kidneyDiseaseCheckBox = new JCheckBox("Kidney Disease");
        gyneCheckBox = new JCheckBox("Gyne / Obstetrical");
        smokerCheckBox = new JCheckBox("Smoker");
        alcoholicDrinkerCheckBox = new JCheckBox("Alcoholic Drinker");
        previousHospitalizationCheckBox = new JCheckBox("Previous Serious Illness/Hospitalization/Injury");

        int row = 0;
        addCheckBoxRow(conditionsPanel, conditionsGbc, row++, allergyCheckBox);
        addCheckBoxRow(conditionsPanel, conditionsGbc, row++, asthmaCheckBox);
        addCheckBoxRow(conditionsPanel, conditionsGbc, row++, tuberculosisCheckBox);
        addCheckBoxRow(conditionsPanel, conditionsGbc, row++, diabetesCheckBox);
        addCheckBoxRow(conditionsPanel, conditionsGbc, row++, heartAilmentCheckBox);
        addCheckBoxRow(conditionsPanel, conditionsGbc, row++, hypertensionCheckBox);
        addCheckBoxRow(conditionsPanel, conditionsGbc, row++, kidneyDiseaseCheckBox);
        addCheckBoxRow(conditionsPanel, conditionsGbc, row++, gyneCheckBox);
        addCheckBoxRow(conditionsPanel, conditionsGbc, row++, smokerCheckBox);
        addCheckBoxRow(conditionsPanel, conditionsGbc, row++, alcoholicDrinkerCheckBox);
        addCheckBoxRow(conditionsPanel, conditionsGbc, row++, previousHospitalizationCheckBox);

        sexComboBox.addActionListener(e -> {
            String sex = (String) sexComboBox.getSelectedItem();
            gyneCheckBox.setEnabled("Female".equals(sex));
            if ("Male".equals(sex)) {
                gyneCheckBox.setSelected(false);
            }
        });

        gbc.gridy = 0; gbc.weighty = 0;
        mainFormPanel.add(conditionsPanel, gbc);

        gbc.gridy = 1; gbc.weighty = 1.0;
        mainFormPanel.add(new JLabel(), gbc);

        JScrollPane scrollPane = new JScrollPane(mainFormPanel);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    protected JPanel createPhysicalExamPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(SECONDARY_COLOR);

        JPanel mainFormPanel = new JPanel(new GridBagLayout());
        mainFormPanel.setBackground(SECONDARY_COLOR);
        mainFormPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JPanel vitalsPanel = createSectionPanel("Vital Signs");
        vitalsPanel.setLayout(new GridBagLayout());
        GridBagConstraints vitalsGbc = new GridBagConstraints();
        vitalsGbc.insets = new Insets(5, 5, 5, 5);
        vitalsGbc.anchor = GridBagConstraints.WEST;
        vitalsGbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel dateTimeLabel = new JLabel("Date & Time: " +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dateTimeLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        bpSystolicField = new JTextField(3);
        bpDiastolicField = new JTextField(3);
        pulseRateField = new JTextField(3);
        oxygenSaturationField = new JTextField(3);
        weightField = new JTextField(5);
        temperatureField = new JTextField(5);

        applyStrictNumericFilter(bpSystolicField, 3, false);
        applyStrictNumericFilter(bpDiastolicField, 3, false);
        applyStrictNumericFilter(pulseRateField, 3, false);
        applyStrictNumericFilter(oxygenSaturationField, 3, false);
        applyStrictNumericFilter(weightField, 5, true);
        applyStrictNumericFilter(temperatureField, 5, true);

        weightField.setToolTipText("Weight in kg (e.g., 0.0–999.9, one decimal place)");
        temperatureField.setToolTipText("Temperature in °C (e.g., 0.0–999.9, one decimal place)");

        int row = 0;
        addFieldRow(vitalsPanel, vitalsGbc, row++, "Date & Time", dateTimeLabel);
        addFieldRow(vitalsPanel, vitalsGbc, row++, "BP (mmHg)", createBPPanel());
        addFieldRow(vitalsPanel, vitalsGbc, row++, "Pulse Rate (bpm)", pulseRateField);
        addFieldRow(vitalsPanel, vitalsGbc, row++, "SpO2 (%)", oxygenSaturationField);
        addFieldRow(vitalsPanel, vitalsGbc, row++, "Weight (kg)", weightField);
        addFieldRow(vitalsPanel, vitalsGbc, row++, "Temperature (°C)", temperatureField);

        JPanel examDetailsPanel = createSectionPanel("Examination Details");
        examDetailsPanel.setLayout(new GridBagLayout());
        GridBagConstraints examGbc = new GridBagConstraints();
        examGbc.insets = new Insets(5, 5, 5, 5);
        examGbc.anchor = GridBagConstraints.WEST;
        examGbc.fill = GridBagConstraints.HORIZONTAL;
        chiefComplaintArea = new JTextArea(2, 20);
        chiefComplaintArea.setLineWrap(true);
        diagnosisArea = new JTextArea(2, 20);
        diagnosisArea.setLineWrap(true);
        treatmentArea = new JTextArea(2, 20);
        treatmentArea.setLineWrap(true);
        medicalAttendantField = new JTextField(15);

        row = 0;
        addFieldRow(examDetailsPanel, examGbc, row++, "Chief Complaint", new JScrollPane(chiefComplaintArea));
        addFieldRow(examDetailsPanel, examGbc, row++, "Diagnosis", new JScrollPane(diagnosisArea));
        addFieldRow(examDetailsPanel, examGbc, row++, "Treatment/Remarks", new JScrollPane(treatmentArea));
        addFieldRow(examDetailsPanel, examGbc, row++, "Medical Attendant", medicalAttendantField);

        row = 0;
        gbc.gridy = row++; gbc.weighty = 0;
        mainFormPanel.add(vitalsPanel, gbc);
        gbc.gridy = row++; gbc.weighty = 0;
        mainFormPanel.add(examDetailsPanel, gbc);

        gbc.gridy = row; gbc.weighty = 1.0;
        mainFormPanel.add(new JLabel(), gbc);

        JScrollPane scrollPane = new JScrollPane(mainFormPanel);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /** Filters text field input for numbers, optionally with decimals. */
    private void applyStrictNumericFilter(JTextField textField, int maxLength, boolean allowDecimal) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + string + currentText.substring(offset);
                
                if (isValidInput(newText, maxLength, allowDecimal)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                
                if (isValidInput(newText, maxLength, allowDecimal)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }

            private boolean isValidInput(String text, int maxLength, boolean allowDecimal) {
                if (text.isEmpty()) return true;

                text = text.trim();
                
                if (allowDecimal) {
                    String pattern = "^(\\d{0," + (maxLength - 2) + "}|\\d{0," + (maxLength - 2) + "}\\.)?(\\d{0,1})?$";
                    return text.matches(pattern) && 
                           (text.isEmpty() || (parseDecimal(text) <= 999.9 && parseDecimal(text) >= 0.0));
                } else {
                    String pattern = "^\\d{0," + maxLength + "}$";
                    return text.matches(pattern) && 
                           (text.isEmpty() || Integer.parseInt(text) <= 999 && Integer.parseInt(text) >= 0);
                }
            }

            private double parseDecimal(String text) {
                try {
                    return Double.parseDouble(text);
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        });
    }

    protected JPanel createExamHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(SECONDARY_COLOR);

        JPanel mainFormPanel = new JPanel(new GridBagLayout());
        mainFormPanel.setBackground(SECONDARY_COLOR);
        mainFormPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        examHistoryTableModel = new DefaultTableModel(
            new Object[] {"Date and Time", "Chief Complaint", "Medical Attendant"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        examHistoryTable = new JTable(examHistoryTableModel);
        examHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        examHistoryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = examHistoryTable.getSelectedRow();
                    if (row >= 0) {
                        showExamDetails(row);
                    }
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(examHistoryTable);
        tableScrollPane.setPreferredSize(new Dimension(450, 200));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteButton = createStyledButton("Delete Selected", DELETE_COLOR, DELETE_HOVER_COLOR,
            e -> deleteSelectedExam());
        buttonPanel.add(deleteButton);

        gbc.gridy = 0;
        mainFormPanel.add(tableScrollPane, gbc);

        gbc.gridy = 1; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainFormPanel.add(buttonPanel, gbc);

        gbc.gridy = 2; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        mainFormPanel.add(new JLabel(), gbc);

        JScrollPane scrollPane = new JScrollPane(mainFormPanel);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        refreshExamHistoryTable();
        return panel;
    }

    /** Refreshes exam history table with person's exams. */
    private void refreshExamHistoryTable() {
        examHistoryTableModel.setRowCount(0);
        if (person != null && person.getExamHistory() != null) {
            for (PhysicalExamEntry entry : person.getExamHistory()) {
                examHistoryTableModel.addRow(new Object[]{
                    entry.getFormattedDateTime(),
                    entry.getChiefComplaint(),
                    entry.getMedicalAttendant()
                });
            }
        }
    }

    /** Shows details of selected exam in a dialog. */
    private void showExamDetails(int rowIndex) {
        if (person == null || rowIndex < 0 || rowIndex >= person.getExamHistory().size()) {
            return;
        }

        PhysicalExamEntry entry = person.getExamHistory().get(rowIndex);
        JDialog detailsDialog = new JDialog(parentDashboard, "Examination Details", true);
        detailsDialog.setSize(400, 400);
        detailsDialog.setLocationRelativeTo(parentDashboard);

        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        detailsPanel.add(new JLabel("Date & Time:"));
        detailsPanel.add(new JLabel(entry.getFormattedDateTime()));
        detailsPanel.add(new JLabel("Chief Complaint:"));
        detailsPanel.add(new JLabel(entry.getChiefComplaint()));
        detailsPanel.add(new JLabel("BP (mmHg):"));
        detailsPanel.add(new JLabel(entry.getBpSystolic() + "/" + entry.getBpDiastolic()));
        detailsPanel.add(new JLabel("Pulse Rate (bpm):"));
        detailsPanel.add(new JLabel(entry.getPulseRate()));
        detailsPanel.add(new JLabel("SpO2 (%):"));
        detailsPanel.add(new JLabel(entry.getOxygenSaturation()));
        detailsPanel.add(new JLabel("Weight (kg):"));
        detailsPanel.add(new JLabel(String.valueOf(entry.getWeight())));
        detailsPanel.add(new JLabel("Temperature (°C):"));
        detailsPanel.add(new JLabel(String.valueOf(entry.getTemperature())));
        detailsPanel.add(new JLabel("Diagnosis:"));
        detailsPanel.add(new JLabel(entry.getDiagnosis()));
        detailsPanel.add(new JLabel("Treatment:"));
        detailsPanel.add(new JLabel(entry.getTreatment()));
        detailsPanel.add(new JLabel("Medical Attendant:"));
        detailsPanel.add(new JLabel(entry.getMedicalAttendant()));

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> detailsDialog.dispose());

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
        contentPanel.add(closeButton, BorderLayout.SOUTH);

        detailsDialog.setContentPane(contentPanel);
        detailsDialog.setVisible(true);
    }

    /** Deletes selected exam with confirmation. */
    private void deleteSelectedExam() {
        int selectedRow = examHistoryTable.getSelectedRow();
        if (selectedRow < 0 || person == null) {
            JOptionPane.showMessageDialog(parentDashboard, "Please select an exam record to delete.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
            parentDashboard,
            "Are you sure you want to delete this examination record?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            person.removeExamEntry(selectedRow);
            dbManager.updateRecord(person, "Deleted physical examination");
            refreshExamHistoryTable();
            parentDashboard.refreshAllViews();
        }
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            title,
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 12),
            TEXT_COLOR
        ));
        return panel;
    }

    private void addFieldRow(JPanel panel, GridBagConstraints gbc, int row, String label, Component field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private void addCheckBoxRow(JPanel panel, GridBagConstraints gbc, int row, JCheckBox checkBox) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        checkBox.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(checkBox, gbc);
        gbc.gridwidth = 1;
    }

    private JPanel createBPPanel() {
        JPanel bpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        bpPanel.setBackground(SECONDARY_COLOR);
        bpPanel.add(bpSystolicField);
        JLabel slashLabel = new JLabel("/");
        slashLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        bpPanel.add(slashLabel);
        bpPanel.add(bpDiastolicField);
        return bpPanel;
    }

    /** Calculates age from selected birthday. */
    private void calculateAgeFromBirthday() {
        String month = (String) monthComboBox.getSelectedItem();
        Integer day = (Integer) dayComboBox.getSelectedItem();
        Integer year = (Integer) yearComboBox.getSelectedItem();

        if (month != null && day != null && year != null) {
            try {
                int monthIndex = java.util.Arrays.asList(
                    "January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"
                ).indexOf(month) + 1;
                LocalDate birthDate = LocalDate.of(year, monthIndex, day);
                int age = Period.between(birthDate, LocalDate.now()).getYears();
                ageField.setText(String.valueOf(age));
            } catch (Exception e) {
                ageField.setText("");
                showValidationError("Invalid date selected. Please choose a valid date.");
            }
        } else {
            ageField.setText("");
        }
    }

    protected void getFormData() {
        if (person == null) {
            createPersonInstance();
        }

        person.surname = surnameField.getText().trim();
        person.givenName = givenNameField.getText().trim();
        person.middleName = middleNameField.getText().trim();
        person.idNumber = idNumberField.getText().trim();
        person.barangay = barangayField.getText().trim();
        person.municipality = municipalityField.getText().trim();
        person.province = provinceField.getText().trim();
        person.country = countryField.getText().trim();
        person.contactNumber = contactField.getText().trim();

        String month = (String) monthComboBox.getSelectedItem();
        Integer day = (Integer) dayComboBox.getSelectedItem();
        Integer year = (Integer) yearComboBox.getSelectedItem();
        if (month != null && day != null && year != null) {
            try {
                int monthIndex = java.util.Arrays.asList(
                    "January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"
                ).indexOf(month) + 1;
                person.birthday = LocalDate.of(year, monthIndex, day);
                person.calculateAge();
            } catch (Exception e) {
                person.birthday = null;
            }
        } else {
            person.birthday = null;
        }

        person.sex = (String) sexComboBox.getSelectedItem();
        person.status = (String) statusComboBox.getSelectedItem();
        person.religion = religionField.getText().trim();

        person.emergencyContactSurname = emergencySurnameField.getText().trim();
        person.emergencyContactGivenName = emergencyGivenNameField.getText().trim();
        person.emergencyContactMiddleName = emergencyMiddleNameField.getText().trim();
        person.emergencyContactNumber = emergencyContactField.getText().trim();

        person.covidStatus = (String) covidStatusComboBox.getSelectedItem();
        person.vaccineType = vaccineTypeField.getText().trim();

        person.hasAllergy = allergyCheckBox.isSelected();
        person.hasAsthma = asthmaCheckBox.isSelected();
        person.hasTuberculosis = tuberculosisCheckBox.isSelected();
        person.hasDiabetes = diabetesCheckBox.isSelected();
        person.hasHeartAilment = heartAilmentCheckBox.isSelected();
        person.hasHypertension = hypertensionCheckBox.isSelected();
        person.hasKidneyDisease = kidneyDiseaseCheckBox.isSelected();
        person.hasGyneIssues = gyneCheckBox.isSelected();
        person.isSmoker = smokerCheckBox.isSelected();
        person.isAlcoholicDrinker = alcoholicDrinkerCheckBox.isSelected();
        person.hasPreviousHospitalization = previousHospitalizationCheckBox.isSelected();

        PhysicalExamEntry newExam = new PhysicalExamEntry();
        newExam.setBpSystolic(bpSystolicField.getText().trim().isEmpty() ? "0" : bpSystolicField.getText().trim());
        newExam.setBpDiastolic(bpDiastolicField.getText().trim().isEmpty() ? "0" : bpDiastolicField.getText().trim());
        newExam.setPulseRate(pulseRateField.getText().trim().isEmpty() ? "0" : pulseRateField.getText().trim());
        newExam.setOxygenSaturation(oxygenSaturationField.getText().trim().isEmpty() ? "0" : oxygenSaturationField.getText().trim());
        try {
            String weightText = weightField.getText().trim();
            newExam.setWeight(weightText.isEmpty() ? 0.0 : Double.parseDouble(weightText));
        } catch (NumberFormatException e) {
            newExam.setWeight(0.0);
        }
        try {
            String tempText = temperatureField.getText().trim();
            newExam.setTemperature(tempText.isEmpty() ? 0.0 : Double.parseDouble(tempText));
        } catch (NumberFormatException e) {
            newExam.setTemperature(0.0);
        }
        newExam.setChiefComplaint(chiefComplaintArea.getText().trim());
        newExam.setDiagnosis(diagnosisArea.getText().trim());
        newExam.setTreatment(treatmentArea.getText().trim());
        newExam.setMedicalAttendant(medicalAttendantField.getText().trim());

        if (!newExam.getChiefComplaint().isEmpty() || !newExam.getMedicalAttendant().isEmpty()) {
            person.addExamEntry(newExam);
        }

        setSpecificFormData();
    }

    protected abstract void createPersonInstance();
    protected abstract void setSpecificFormData();

    protected void populateFields() {
        if (person == null) return;

        surnameField.setText(person.surname != null ? person.surname : "");
        givenNameField.setText(person.givenName != null ? person.givenName : "");
        middleNameField.setText(person.middleName != null ? person.middleName : "");
        idNumberField.setText(person.idNumber != null ? person.idNumber : "");
        barangayField.setText(person.barangay != null ? person.barangay : "");
        municipalityField.setText(person.municipality != null ? person.municipality : "");
        provinceField.setText(person.province != null ? person.province : "");
        countryField.setText(person.country != null ? person.country : "");
        contactField.setText(person.contactNumber != null ? person.contactNumber : "");

        if (person.birthday != null) {
            int monthIndex = person.birthday.getMonthValue() - 1;
            monthComboBox.setSelectedIndex(monthIndex);
            dayComboBox.setSelectedItem(person.birthday.getDayOfMonth());
            yearComboBox.setSelectedItem(person.birthday.getYear());
            ageField.setText(String.valueOf(person.age));
        } else {
            monthComboBox.setSelectedIndex(-1);
            dayComboBox.setSelectedIndex(-1);
            yearComboBox.setSelectedIndex(-1);
            ageField.setText("");
        }

        if (person.sex != null) sexComboBox.setSelectedItem(person.sex);
        if (person.status != null) statusComboBox.setSelectedItem(person.status);
        religionField.setText(person.religion != null ? person.religion : "");

        emergencySurnameField.setText(person.emergencyContactSurname != null ? person.emergencyContactSurname : "");
        emergencyGivenNameField.setText(person.emergencyContactGivenName != null ? person.emergencyContactGivenName : "");
        emergencyMiddleNameField.setText(person.emergencyContactMiddleName != null ? person.emergencyContactMiddleName : "");
        emergencyContactField.setText(person.emergencyContactNumber != null ? person.emergencyContactNumber : "");

        if (person.covidStatus != null) covidStatusComboBox.setSelectedItem(person.covidStatus);
        vaccineTypeField.setText(person.vaccineType != null ? person.vaccineType : "");

        allergyCheckBox.setSelected(person.hasAllergy);
        asthmaCheckBox.setSelected(person.hasAsthma);
        tuberculosisCheckBox.setSelected(person.hasTuberculosis);
        diabetesCheckBox.setSelected(person.hasDiabetes);
        heartAilmentCheckBox.setSelected(person.hasHeartAilment);
        hypertensionCheckBox.setSelected(person.hasHypertension);
        kidneyDiseaseCheckBox.setSelected(person.hasKidneyDisease);
        gyneCheckBox.setSelected(person.hasGyneIssues);
        smokerCheckBox.setSelected(person.isSmoker);
        alcoholicDrinkerCheckBox.setSelected(person.isAlcoholicDrinker);
        previousHospitalizationCheckBox.setSelected(person.hasPreviousHospitalization);

        refreshExamHistoryTable();
        populateSpecificFields();
    }

    protected abstract void populateSpecificFields();

    protected boolean validateForm() {
        List<String> emptyFields = new ArrayList<>();
        List<String> invalidFields = new ArrayList<>();

        if (surnameField.getText().trim().isEmpty()) {
            emptyFields.add("Surname");
        } else if (!surnameField.getText().trim().matches(NAME_REGEX)) {
            invalidFields.add("Surname (only uppercase letters first, spaces, hyphens, or apostrophes allowed)");
        }

        if (givenNameField.getText().trim().isEmpty()) {
            emptyFields.add("Given Name");
        } else if (!givenNameField.getText().trim().matches(NAME_REGEX)) {
            invalidFields.add("Given Name (only uppercase letters first, spaces, hyphens, or apostrophes allowed)");
        }

        if (!middleNameField.getText().trim().isEmpty() && 
            !middleNameField.getText().trim().matches(NAME_REGEX)) {
            invalidFields.add("Middle Name (only uppercase letters first, spaces, hyphens, or apostrophes allowed)");
        }

        if (idNumberField.getText().trim().isEmpty()) {
            emptyFields.add("ID Number");
        } 
        String newIdNumber = idNumberField.getText().trim();
        List<Person> allRecords = dbManager.getAllRecords();
        for (Person record : allRecords) {
            if (record.getIdNumber() != null && record.getIdNumber().equals(newIdNumber)) {
                if (!isNewRecord && person != null && person.getIdNumber().equals(newIdNumber)) {
                    continue;
                }
                invalidFields.add("ID Number (duplicate: '" + newIdNumber + "')");
                break;
            }
        }

        if (contactField.getText().trim().isEmpty()) {
            emptyFields.add("Contact Number");
        } else if (!contactField.getText().trim().matches(CONTACT_REGEX)) {
            invalidFields.add("Contact Number (must follow XXX-XXX-XXXX format)");
        }

        if (barangayField.getText().trim().isEmpty()) {
            emptyFields.add("Barangay");
        } else if (!barangayField.getText().trim().matches(NAME_REGEX)) {
            invalidFields.add("Barangay (only uppercase letters first, spaces, hyphens, or apostrophes allowed)");
        }

        if (municipalityField.getText().trim().isEmpty()) {
            emptyFields.add("Municipality");
        } else if (!municipalityField.getText().trim().matches(NAME_REGEX)) {
            invalidFields.add("City (only uppercase letters first, hyphens, or apostrophes allowed)");
        }

        if (provinceField.getText().trim().isEmpty()) {
            emptyFields.add("Province");
        } else if (!provinceField.getText().trim().matches(NAME_REGEX)) {
            invalidFields.add("Province (only uppercase letters first, spaces, hyphens, or apostrophes allowed)");
        }

        if (countryField.getText().trim().isEmpty()) {
            emptyFields.add("Country");
        } else if (!countryField.getText().trim().matches(NAME_REGEX)) {
            invalidFields.add("Country (only uppercase letters first, spaces, hyphens, or apostrophes allowed)");
        }

        if (emergencySurnameField.getText().trim().isEmpty()) {
            emptyFields.add("Emergency Contact Surname");
        } else if (!emergencySurnameField.getText().trim().matches(NAME_REGEX)) {
            invalidFields.add("Emergency Contact Surname (only uppercase letters first, spaces, hyphens, or apostrophes allowed)");
        }

        if (emergencyGivenNameField.getText().trim().isEmpty()) {
            emptyFields.add("Emergency Contact Given Name");
        } else if (!emergencyGivenNameField.getText().trim().matches(NAME_REGEX)) {
            invalidFields.add("Emergency Contact Given Name (only uppercase letters first, spaces, hyphens, or apostrophes allowed)");
        }

        if (!emergencyMiddleNameField.getText().trim().isEmpty() && 
            !emergencyMiddleNameField.getText().trim().matches(NAME_REGEX)) {
            invalidFields.add("Emergency Contact Middle Name (only uppercase letters first, spaces, hyphens, or apostrophes allowed)");
        }

        if (emergencyContactField.getText().trim().isEmpty()) {
            emptyFields.add("Emergency Contact Number");
        } else if (!emergencyContactField.getText().trim().matches(CONTACT_REGEX)) {
            invalidFields.add("Emergency Contact Number (must follow XXX-XXX-XXXX format)");
        }

        if (!religionField.getText().trim().isEmpty() && 
            !religionField.getText().trim().matches(RELIGION_REGEX)) {
            invalidFields.add("Religion (only letters and spaces allowed)");
        }

        String month = (String) monthComboBox.getSelectedItem();
        Integer day = (Integer) dayComboBox.getSelectedItem();
        Integer year = (Integer) yearComboBox.getSelectedItem();
        if (month == null || day == null || year == null) {
            emptyFields.add("Birthday");
        } else {
            try {
                int monthIndex = java.util.Arrays.asList(
                    "January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"
                ).indexOf(month) + 1;
                LocalDate.of(year, monthIndex, day);
            } catch (Exception e) {
                invalidFields.add("Birthday (must be a valid date)");
            }
        }

        boolean hasPhysicalExamData = false;

        if (!bpSystolicField.getText().trim().isEmpty()) {
            try {
                int value = Integer.parseInt(bpSystolicField.getText().trim());
                if (value < 0 || value > 999) {
                    invalidFields.add("Blood Pressure Systolic (0–999)");
                }
            } catch (NumberFormatException e) {
                invalidFields.add("Blood Pressure Systolic (must be an integer)");
            }
            hasPhysicalExamData = true;
        }

        if (!bpDiastolicField.getText().trim().isEmpty()) {
            try {
                int value = Integer.parseInt(bpDiastolicField.getText().trim());
                if (value < 0 || value > 999) {
                    invalidFields.add("Blood Pressure Diastolic (0–999)");
                }
            } catch (NumberFormatException e) {
                invalidFields.add("Blood Pressure Diastolic (must be an integer)");
            }
            hasPhysicalExamData = true;
        }

        if (!pulseRateField.getText().trim().isEmpty()) {
            try {
                int value = Integer.parseInt(pulseRateField.getText().trim());
                if (value < 0 || value > 999) {
                    invalidFields.add("Pulse Rate (0–999)");
                }
            } catch (NumberFormatException e) {
                invalidFields.add("Pulse Rate (must be an integer)");
            }
            hasPhysicalExamData = true;
        }

        if (!oxygenSaturationField.getText().trim().isEmpty()) {
            try {
                int value = Integer.parseInt(oxygenSaturationField.getText().trim());
                if (value < 0 || value > 100) {
                    invalidFields.add("SpO2 (0–100)");
                }
            } catch (NumberFormatException e) {
                invalidFields.add("SpO2 (must be an integer)");
            }
            hasPhysicalExamData = true;
        }

        if (!weightField.getText().trim().isEmpty()) {
            try {
                double value = Double.parseDouble(weightField.getText().trim());
                if (value < 0.0 || value > 999.9) {
                    invalidFields.add("Weight (0.0–999.9, one decimal)");
                }
            } catch (NumberFormatException e) {
                invalidFields.add("Weight (must be a number with one decimal, e.g., 123.4)");
            }
            hasPhysicalExamData = true;
        }

        if (!temperatureField.getText().trim().isEmpty()) {
            try {
                double value = Double.parseDouble(temperatureField.getText().trim());
                if (value < 0.0 || value > 999.9) {
                    invalidFields.add("Temperature (0.0–999.9, one decimal)");
                }
            } catch (NumberFormatException e) {
                invalidFields.add("Temperature (must be a number with one decimal, e.g., 36.5)");
            }
            hasPhysicalExamData = true;
        }

        if (!chiefComplaintArea.getText().trim().isEmpty()) {
            hasPhysicalExamData = true;
        }

        if (!diagnosisArea.getText().trim().isEmpty()) {
            hasPhysicalExamData = true;
        }

        if (!treatmentArea.getText().trim().isEmpty()) {
            hasPhysicalExamData = true;
        }

        if (hasPhysicalExamData) {
            if (medicalAttendantField.getText().trim().isEmpty()) {
                invalidFields.add("Medical Attendant (required with exam data)");
            } else if (!medicalAttendantField.getText().trim().matches(MEDICAL_ATTENDANT_REGEX)) {
                invalidFields.add("Medical Attendant (only uppercase letters first, spaces, periods, or commas)");
            }
        }

        if (!medicalAttendantField.getText().trim().isEmpty() && !hasPhysicalExamData) {
            invalidFields.add("Medical Attendant (not allowed without exam data)");
        }

        if (!medicalAttendantField.getText().trim().isEmpty() && 
            !medicalAttendantField.getText().trim().matches(MEDICAL_ATTENDANT_REGEX)) {
            invalidFields.add("Medical Attendant (only uppercase letters first, spaces, periods, or commas)");
        }

        if (covidStatusComboBox.getSelectedItem() == null) {
            emptyFields.add("Vaccination Status");
        }
        if (!vaccineTypeField.getText().trim().isEmpty() && 
            !vaccineTypeField.getText().trim().matches(VACCINE_TYPE_REGEX)) {
            invalidFields.add("Vaccine Type (only letters and spaces allowed)");
        }

        if (!validateSpecificFields()) {
            return false;
        }

        int requiredFieldCount = 12;
        if (!emptyFields.isEmpty()) {
            if (emptyFields.size() == requiredFieldCount) {
                showValidationError("Required fields cannot be blank.");
                return false;
            } else if (emptyFields.size() > 1) {
                showValidationError("Multiple required fields are empty.");
                return false;
            } else {
                showValidationError("Required field '" + emptyFields.get(0) + "' is empty.");
                return false;
            }
        }

        if (!invalidFields.isEmpty()) {
            if (invalidFields.size() > 1) {
                showValidationError("Multiple invalid inputs detected.");
                return false;
            } else {
                showValidationError("Invalid input in " + invalidFields.get(0) + ".");
                return false;
            }
        }
        return true;
    }
    
    protected abstract boolean validateSpecificFields();

    protected void showValidationError(String message) {
        JOptionPane.showMessageDialog(parentDashboard, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Saves record with validation and updates dashboard. */
    protected void saveRecord() {
        if (!validateForm()) {
            return;
        }

        try {
            if (isNewRecord) {
                getFormData();
                dbManager.addRecord(person);
            } else {
                int originalExamCount = person.getExamHistory().size();
                getFormData();
                if (originalExamCount < person.getExamHistory().size()) {
                    dbManager.updateRecord(person, "Added physical examination");
                } else {
                    dbManager.updateRecord(person, "Modified personal information");
                }
            }
            clearPhysicalExamFields();
            JOptionPane.showMessageDialog(parentDashboard, 
                "Record saved successfully.", 
                "Save Successful", 
                JOptionPane.INFORMATION_MESSAGE);
            parentDashboard.refreshAllViews();
            parentDashboard.clearRightPanel();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentDashboard, "Error saving record: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Deletes record with confirmation. */
    protected void deleteRecord() {
        int confirm = JOptionPane.showConfirmDialog(
            parentDashboard,
            "Are you sure you want to delete this record?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dbManager.deleteRecord(person);
                parentDashboard.refreshAllViews();
                parentDashboard.clearRightPanel();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(parentDashboard, "Error deleting record: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    protected abstract void addSpecificFields(JPanel panel, GridBagConstraints gbc, int startRow);
    protected abstract int getLastRowIndex();
}
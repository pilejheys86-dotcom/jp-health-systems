/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package medicalrecordkeeper;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Comparator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** Main UI for managing medical records and activity logs. */
public class Dashboard extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel activityLogPanel;
    private JPanel recordsViewPanel;
    private JPanel contentPanel; // Holds center content
    private JTable activityTable;
    private DefaultTableModel activityTableModel;
    private DatabaseManager dbManager;

    private static final Color DEFAULT_BUTTON_COLOR = new Color(0, 120, 215);
    private static final Color HOVER_BUTTON_COLOR = new Color(0, 140, 255);
    private static final Color CANCEL_DEFAULT_COLOR = new Color(200, 200, 200);
    private static final Color CANCEL_HOVER_COLOR = new Color(220, 220, 220);

    public Dashboard() {
        this.dbManager = DatabaseManager.getInstance();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Medical Record Keeper");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                    Dashboard.this,
                    "Are you sure you want to exit the application?",
                    "Exit Application",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }
            }
        });

        setLayout(new BorderLayout());

        JPanel navPanel = createNavigationPanel();
        add(navPanel, BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        activityLogPanel = createActivityLogPanel();
        recordsViewPanel = createRecordsViewPanel();
        mainPanel.add(activityLogPanel, "ACTIVITY_LOG");
        mainPanel.add(recordsViewPanel, "RECORDS_VIEW");
        
        contentPanel.add(mainPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "ACTIVITY_LOG");

        refreshAllViews();
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(0, 100, 200));
        navPanel.setPreferredSize(new Dimension(200, 700));

        JLabel titleLabel = new JLabel("JP Health Systems");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton activityLogBtn = createNavButton("Activity Log", "ACTIVITY_LOG");
        JButton viewRecordsBtn = createNavButton("View Records", "RECORDS_VIEW");
        JButton addRecordBtn = createNavButton("Add Record", null);
        addRecordBtn.addActionListener(e -> showAddRecordForm());

        navPanel.add(titleLabel);
        navPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        navPanel.add(activityLogBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(viewRecordsBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(addRecordBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        return navPanel;
    }

    private JButton createNavButton(String text, String cardName) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(DEFAULT_BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        if (cardName != null) {
            button.addActionListener(e -> {
                contentPanel.removeAll();
                contentPanel.add(mainPanel, BorderLayout.CENTER);
                cardLayout.show(mainPanel, cardName);
                contentPanel.revalidate();
                contentPanel.repaint();
            });
        }

        addButtonHoverEffect(button, DEFAULT_BUTTON_COLOR, HOVER_BUTTON_COLOR);

        return button;
    }

    private JPanel createActivityLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        activityTableModel = new DefaultTableModel();
        activityTableModel.addColumn("Date");
        activityTableModel.addColumn("Time");
        activityTableModel.addColumn("Name");
        activityTableModel.addColumn("Type");
        activityTableModel.addColumn("Action");

        activityTable = new JTable(activityTableModel);
        JScrollPane scrollPane = new JScrollPane(activityTable);

        JLabel headerLabel = new JLabel("Activity Log");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        panel.add(headerLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton refreshButton = createButton("Refresh", DEFAULT_BUTTON_COLOR, HOVER_BUTTON_COLOR, e -> refreshActivityLog());
        refreshButton.setPreferredSize(new Dimension(120, 40));
        buttonPanel.add(refreshButton);

        JButton deleteLogButton = createButton("Delete Log", new Color(220, 53, 69), new Color(240, 73, 89), e -> deleteActivityLog());
        deleteLogButton.setPreferredSize(new Dimension(120, 40));
        buttonPanel.add(deleteLogButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /** Deletes all activity log entries with confirmation. */
    private void deleteActivityLog() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete all activity log entries? This action cannot be undone.",
            "Confirm Delete Log",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dbManager.clearActivityLog();
            refreshActivityLog();
            JOptionPane.showMessageDialog(
                this,
                "Activity log has been deleted.",
                "Log Cleared",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private JPanel createRecordsViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("View Records");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("All Records", createRecordViewTab("All"));
        tabbedPane.addTab("Senior High School", createRecordViewTab("Senior High School"));
        tabbedPane.addTab("College", createRecordViewTab("College"));
        tabbedPane.addTab("Personnel", createRecordViewTab("Personnel"));

        panel.add(headerLabel, BorderLayout.NORTH);
        panel.add(tabbedPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRecordViewTab(String type) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel tilesPanel = new JPanel();
        tilesPanel.setLayout(new BoxLayout(tilesPanel, BoxLayout.Y_AXIS));
        tilesPanel.setName("tiles-" + type);

        List<Person> records = "All".equals(type) ?
            dbManager.getAllRecords() :
            dbManager.getRecordsByType(type);

        for (Person person : records) {
            tilesPanel.add(createPersonTile(person));
            tilesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        tilesPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(tilesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JButton refreshButton = createButton("Refresh", DEFAULT_BUTTON_COLOR, HOVER_BUTTON_COLOR, e -> refreshRecordTiles(type));
        refreshButton.setPreferredSize(new Dimension(120, 40));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.add(refreshButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshActivityLog() {
        activityTableModel.setRowCount(0);

        List<ActivityLogEntry> log = dbManager.getActivityLog();
        for (ActivityLogEntry entry : log) {
            activityTableModel.addRow(new Object[]{
                entry.getFormattedDate(),
                entry.getFormattedTime(),
                entry.getPersonName(),
                entry.getPersonType(),
                entry.getAction()
            });
        }
    }

    /** Refreshes record tiles for given type, sorting by surname. */
    private void refreshRecordTiles(String type) {
        if (recordsViewPanel == null) return;

        for (Component component : recordsViewPanel.getComponents()) {
            if (component instanceof JTabbedPane) {
                JTabbedPane tabbedPane = (JTabbedPane) component;
                for (Component tab : tabbedPane.getComponents()) {
                    if (tab instanceof JPanel) {
                        JPanel panel = (JPanel) tab;
                        Component[] components = panel.getComponents();
                        for (Component comp : components) {
                            if (comp instanceof JScrollPane) {
                                JScrollPane scrollPane = (JScrollPane) comp;
                                JPanel tilesPanel = (JPanel) scrollPane.getViewport().getView();
                                if (tilesPanel.getName().equals("tiles-" + type)) {
                                    tilesPanel.removeAll();

                                    List<Person> records = "All".equals(type) ?
                                        dbManager.getAllRecords() :
                                        dbManager.getRecordsByType(type);

                                    records.sort(Comparator.comparing(Person::getSurname));

                                    for (Person person : records) {
                                        tilesPanel.add(createPersonTile(person));
                                        tilesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                                    }

                                    tilesPanel.revalidate();
                                    tilesPanel.repaint();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private JPanel createPersonTile(Person person) {
        JPanel tile = new JPanel(new BorderLayout(5, 5));
        tile.setBackground(new Color(240, 240, 250));
        tile.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(240, 240, 250));

        JLabel nameLabel = new JLabel(person.getFullName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel typeLabel = new JLabel(person.getType());
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(nameLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(typeLabel);

        JButton viewButton = createButton("View", DEFAULT_BUTTON_COLOR, HOVER_BUTTON_COLOR, e -> showPersonDetailsForm(person));
        viewButton.setFont(new Font("Arial", Font.BOLD, 12));
        viewButton.setPreferredSize(new Dimension(80, 30));
        viewButton.setFocusPainted(false);

        tile.add(leftPanel, BorderLayout.CENTER);
        tile.add(viewButton, BorderLayout.EAST);

        tile.setPreferredSize(new Dimension(350, 70));
        tile.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        tile.setMinimumSize(new Dimension(350, 70));

        return tile;
    }

    /** Shows form to add a new record based on selected type. */
    private void showAddRecordForm() {
        String[] options = {"Senior High School", "College", "Personnel"};
        int choice = JOptionPane.showOptionDialog(
            this,
            "Select client type to add:",
            "Add New Record",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice != JOptionPane.CLOSED_OPTION) {
            JPanel formPanel;
            switch (choice) {
                case 0: formPanel = new SeniorHighSchoolForm(this, null).getContentPanel(); break;
                case 1: formPanel = new CollegeStudentForm(this, null).getContentPanel(); break;
                case 2: formPanel = new PersonnelForm(this, null).getContentPanel(); break;
                default: return;
            }

            contentPanel.removeAll();
            contentPanel.add(formPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    /** Shows details form for a person based on type. */
    private void showPersonDetailsForm(Person person) {
        JPanel formPanel;
        switch (person.getType()) {
            case "Senior High School":
                formPanel = new SeniorHighSchoolForm(this, (SeniorHighStudent) person).getContentPanel();
                break;
            case "College":
                formPanel = new CollegeStudentForm(this, (CollegeStudent) person).getContentPanel();
                break;
            case "Personnel":
                formPanel = new PersonnelForm(this, (Personnel) person).getContentPanel();
                break;
            default:
                return;
        }

        contentPanel.removeAll();
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    public void refreshAllViews() {
        refreshActivityLog();
        refreshRecordTiles("All");
        refreshRecordTiles("Senior High School");
        refreshRecordTiles("College");
        refreshRecordTiles("Personnel");
    }

    public void clearRightPanel() {
        contentPanel.removeAll();
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JButton createButton(String text, Color defaultColor, Color hoverColor, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(defaultColor);
        button.setForeground(defaultColor == CANCEL_DEFAULT_COLOR ? Color.BLACK : Color.WHITE);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButtonHoverEffect(button, defaultColor, hoverColor);
        button.addActionListener(actionListener);
        return button;
    }

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
}
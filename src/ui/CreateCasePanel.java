package ui;

import managers.CaseRecordManager;

import javax.swing.*;
import java.awt.*;

public class CreateCasePanel extends JPanel {

    // Reference to MainView
    private MainView mainView;

    // Manager
    private CaseRecordManager caseRecordManager;

    // Form fields
    private JTextField defendantNameField;
    private JTextField defendantDobField;
    private JTextField caseTypeField;
    private JTextField hearingDateField;

    // Error label
    private JLabel errorLabel;

    // Constructor
    public CreateCasePanel(MainView mainView) {
        this.mainView = mainView;
        this.caseRecordManager = mainView.getCaseRecordManager();
        initializePanel();
    }

    // Initialize the panel
    private void initializePanel() {
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Create Case Record", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Defendant Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Defendant Name:"), gbc);
        gbc.gridx = 1;
        defendantNameField = new JTextField(20);
        formPanel.add(defendantNameField, gbc);

        // Defendant DOB
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Defendant DOB (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        defendantDobField = new JTextField(20);
        formPanel.add(defendantDobField, gbc);

        // Case Type
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Case Type:"), gbc);
        gbc.gridx = 1;
        caseTypeField = new JTextField(20);
        formPanel.add(caseTypeField, gbc);

        // Hearing Date
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Hearing Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        hearingDateField = new JTextField(20);
        formPanel.add(hearingDateField, gbc);

        // Error label
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        formPanel.add(errorLabel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Submit button
        JButton submitBtn = new JButton("Submit");
        submitBtn.addActionListener(e -> handleSubmit());
        buttonPanel.add(submitBtn);

        // Back button
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            clearForm();
            mainView.showPanel(MainView.MAIN_PANEL);
        });
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Handle form submission
    private void handleSubmit() {
        String defendantName = defendantNameField.getText().trim();
        String defendantDob = defendantDobField.getText().trim();
        String caseType = caseTypeField.getText().trim();
        String hearingDate = hearingDateField.getText().trim();

        // Validate fields
        if (defendantName.isEmpty() || defendantDob.isEmpty() || 
            caseType.isEmpty() || hearingDate.isEmpty()) {
            errorLabel.setText("All fields are required.");
            return;
        }

        // Create case record
        boolean success = caseRecordManager.createCaseRecord(
            defendantName, defendantDob, caseType, hearingDate
        );

        if (success) {
            clearForm();
            mainView.loadCaseRecords(); // refresh table
            mainView.showPanel(MainView.MAIN_PANEL);
        } else {
            errorLabel.setText("Error creating case record. Please try again.");
        }
    }

    // Clear form fields
    private void clearForm() {
        defendantNameField.setText("");
        defendantDobField.setText("");
        caseTypeField.setText("");
        hearingDateField.setText("");
        errorLabel.setText("");
    }
}
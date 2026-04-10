package ui;

import managers.CaseRecordManager;
import model.CaseRecord;

import javax.swing.*;
import java.awt.*;

public class DisplayCasePanel extends JPanel {

    // Reference to MainView
    private MainView mainView;

    // Manager
    private CaseRecordManager caseRecordManager;

    // Current case record
    private CaseRecord currentCase;
    private JTextField caseIdField;

    // Form fields
    private JTextField defendantNameField;
    private JTextField defendantDobField;
    private JTextField caseTypeField;
    private JTextField hearingDateField;
    private JTextField caseStatusField;

    // Buttons
    private JButton saveBtn;
    private JButton resolveBtn;
    private JButton removeBtn;
    private JButton addSummaryBtn;
    private JButton backBtn;

    // Error label
    private JLabel errorLabel;

    // Constructor
    public DisplayCasePanel(MainView mainView) {
        this.mainView = mainView;
        this.caseRecordManager = mainView.getCaseRecordManager();
        initializePanel();
    }

    // Initialize the panel
    private void initializePanel() {
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Case Record Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Case ID (read only always)
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Case ID:"), gbc);
        gbc.gridx = 1;
        caseIdField = new JTextField(20);
        caseIdField.setEditable(false);
        caseIdField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(caseIdField, gbc);

        // Defendant Name
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Defendant Name:"), gbc);
        gbc.gridx = 1;
        defendantNameField = new JTextField(20);
        formPanel.add(defendantNameField, gbc);

        // Defendant DOB
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Defendant DOB:"), gbc);
        gbc.gridx = 1;
        defendantDobField = new JTextField(20);
        formPanel.add(defendantDobField, gbc);

        // Case Type
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Case Type:"), gbc);
        gbc.gridx = 1;
        caseTypeField = new JTextField(20);
        formPanel.add(caseTypeField, gbc);

        // Hearing Date
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Hearing Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        hearingDateField = new JTextField(20);
        formPanel.add(hearingDateField, gbc);

        // Case Status (read only always)
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Case Status:"), gbc);
        gbc.gridx = 1;
        caseStatusField = new JTextField(20);
        caseStatusField.setEditable(false);
        caseStatusField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(caseStatusField, gbc);

        // Error label
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        formPanel.add(errorLabel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Save button
        saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> handleSave());
        buttonPanel.add(saveBtn);

        // Resolve button
        resolveBtn = new JButton("Resolve");
        resolveBtn.addActionListener(e -> handleResolve());
        buttonPanel.add(resolveBtn);

        // Remove button
        removeBtn = new JButton("Remove");
        removeBtn.addActionListener(e -> handleRemove());
        buttonPanel.add(removeBtn);

        // Add Summary button
        addSummaryBtn = new JButton("Add Summary");
        addSummaryBtn.addActionListener(e -> handleAddSummary());
        buttonPanel.add(addSummaryBtn);

        // Back button
        backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            mainView.showPanel(MainView.MAIN_PANEL);
        });
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Load case record into fields
    public void loadCase(CaseRecord caseRecord) {
        this.currentCase = caseRecord;

        // Populate fields
        caseIdField.setText(String.valueOf(caseRecord.getCaseId()));
        defendantNameField.setText(caseRecord.getDefendantName());
        defendantDobField.setText(caseRecord.getDefendantDob());
        caseTypeField.setText(caseRecord.getCaseType());
        hearingDateField.setText(caseRecord.getHearingDate());
        caseStatusField.setText(caseRecord.getCaseStatus());

        // Lock fields if archived
        boolean isArchived = caseRecord.getCaseStatus().equals("Archived");
        defendantNameField.setEditable(!isArchived);
        defendantDobField.setEditable(!isArchived);
        caseTypeField.setEditable(!isArchived);
        hearingDateField.setEditable(!isArchived);

        // Update buttons based on status
        saveBtn.setVisible(!isArchived);
        resolveBtn.setVisible(!isArchived);
        removeBtn.setVisible(!isArchived);
        addSummaryBtn.setVisible(!isArchived);
        removeBtn.setText(isArchived ? "Delete" : "Remove");

        errorLabel.setText("");
    }

    // Handle save
    private void handleSave() {
        String defendantName = defendantNameField.getText().trim();
        String defendantDob = defendantDobField.getText().trim();
        String caseType = caseTypeField.getText().trim();
        String hearingDate = hearingDateField.getText().trim();

        if (defendantName.isEmpty() || defendantDob.isEmpty() ||
            caseType.isEmpty() || hearingDate.isEmpty()) {
            errorLabel.setText("All fields are required.");
            return;
        }

        currentCase.setDefendantName(defendantName);
        currentCase.setDefendantDob(defendantDob);
        currentCase.setCaseType(caseType);
        currentCase.setHearingDate(hearingDate);

        boolean success = caseRecordManager.updateCaseRecord(currentCase);
        if (success) {
            errorLabel.setForeground(Color.GREEN);
            errorLabel.setText("Case record updated successfully.");
            mainView.loadCaseRecords();
        } else {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("Error updating case record.");
        }
    }

    // Handle resolve
    private void handleResolve() {
        mainView.getResolveCasePanel().loadCase(currentCase);
        mainView.showPanel(MainView.RESOLVE_CASE_PANEL);
    }

    // Handle remove
    private void handleRemove() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to remove this case?",
            "Confirm Remove",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = caseRecordManager.archiveCaseRecord(currentCase);
            if (success) {
                mainView.loadCaseRecords();
                mainView.showPanel(MainView.MAIN_PANEL);
            } else {
                errorLabel.setText("Error removing case record.");
            }
        }
    }

    // Handle add summary
    private void handleAddSummary() {
        mainView.getAddSummaryPanel().loadCase(currentCase);
        mainView.showPanel(MainView.ADD_SUMMARY_PANEL);
    }
}
package ui;

import managers.CaseRecordManager;
import model.ArchivedCaseRecord;
import model.ArchivedSummary;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DisplayArchivedCasePanel extends JPanel {

    // Reference to MainView
    private MainView mainView;

    // Manager
    private CaseRecordManager caseRecordManager;

    // Current archived case
    private ArchivedCaseRecord currentCase;

    // Form fields
    private JTextField archivedCaseIdField;
    private JTextField originalCaseIdField;
    private JTextField defendantNameField;
    private JTextField defendantDobField;
    private JTextField caseTypeField;
    private JTextField hearingDateField;
    private JTextField caseStatusField;
    private JTextField archiveDateField;

    // Summaries panel
    private JPanel summariesListPanel;

    // Title
    private JLabel titleLabel;

    // Error label
    private JLabel errorLabel;

    // Constructor
    public DisplayArchivedCasePanel(MainView mainView) {
        this.mainView = mainView;
        this.caseRecordManager = mainView.getCaseRecordManager();
        initializePanel();
    }

    // Initialize the panel
    private void initializePanel() {
        setLayout(new BorderLayout());

        // Title
        titleLabel = new JLabel("Archived Case Record", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Case Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Archived Case ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Archived Case ID:"), gbc);
        gbc.gridx = 1;
        archivedCaseIdField = new JTextField(20);
        archivedCaseIdField.setEditable(false);
        archivedCaseIdField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(archivedCaseIdField, gbc);

        // Original Case ID
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Original Case ID:"), gbc);
        gbc.gridx = 1;
        originalCaseIdField = new JTextField(20);
        originalCaseIdField.setEditable(false);
        originalCaseIdField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(originalCaseIdField, gbc);

        // Defendant Name
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Defendant Name:"), gbc);
        gbc.gridx = 1;
        defendantNameField = new JTextField(20);
        defendantNameField.setEditable(false);
        defendantNameField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(defendantNameField, gbc);

        // Defendant DOB
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Defendant DOB:"), gbc);
        gbc.gridx = 1;
        defendantDobField = new JTextField(20);
        defendantDobField.setEditable(false);
        defendantDobField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(defendantDobField, gbc);

        // Case Type
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Case Type:"), gbc);
        gbc.gridx = 1;
        caseTypeField = new JTextField(20);
        caseTypeField.setEditable(false);
        caseTypeField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(caseTypeField, gbc);

        // Hearing Date
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Hearing Date:"), gbc);
        gbc.gridx = 1;
        hearingDateField = new JTextField(20);
        hearingDateField.setEditable(false);
        hearingDateField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(hearingDateField, gbc);

        // Case Status
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Case Status:"), gbc);
        gbc.gridx = 1;
        caseStatusField = new JTextField(20);
        caseStatusField.setEditable(false);
        caseStatusField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(caseStatusField, gbc);

        // Archive Date
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Archive Date:"), gbc);
        gbc.gridx = 1;
        archiveDateField = new JTextField(20);
        archiveDateField.setEditable(false);
        archiveDateField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(archiveDateField, gbc);

        // Error label
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        formPanel.add(errorLabel, gbc);

        centerPanel.add(formPanel, BorderLayout.NORTH);

        // Summaries section
        JPanel summariesPanel = new JPanel(new BorderLayout());
        summariesPanel.setBorder(
            BorderFactory.createTitledBorder("Summaries"));

        summariesListPanel = new JPanel();
        summariesListPanel.setLayout(
            new BoxLayout(summariesListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(summariesListPanel);
        scrollPane.setPreferredSize(new Dimension(0, 150));
        summariesPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(summariesPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Delete button
        JButton deleteBtn = new JButton("Permanently Delete");
        deleteBtn.setForeground(Color.RED);
        deleteBtn.addActionListener(e -> handleDelete());
        buttonPanel.add(deleteBtn);

        // Back button
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e ->
            mainView.showPanel(MainView.ARCHIVED_CASES_PANEL));
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Load archived case into panel
    public void loadArchivedCase(ArchivedCaseRecord archivedCase) {
        this.currentCase = archivedCase;

        // Populate fields
        titleLabel.setText("Archived Case — " + archivedCase.getDefendantName());
        archivedCaseIdField.setText(
            String.valueOf(archivedCase.getArchivedCaseId()));
        originalCaseIdField.setText(
            String.valueOf(archivedCase.getOriginalCaseId()));
        defendantNameField.setText(archivedCase.getDefendantName());
        defendantDobField.setText(archivedCase.getDefendantDob());
        caseTypeField.setText(archivedCase.getCaseType());
        hearingDateField.setText(archivedCase.getHearingDate());
        caseStatusField.setText(archivedCase.getCaseStatus());
        archiveDateField.setText(archivedCase.getArchiveDate());

        // Load summaries
        loadSummaries();
        errorLabel.setText("");
    }

    // Load archived summaries
    private void loadSummaries() {
        summariesListPanel.removeAll();
        List<ArchivedSummary> summaries =
            caseRecordManager.getArchivedSummariesByCaseId(
                currentCase.getArchivedCaseId());

        if (summaries.isEmpty()) {
            JLabel noSummariesLabel = new JLabel("No summaries found.");
            noSummariesLabel.setBorder(
                BorderFactory.createEmptyBorder(5, 10, 5, 10));
            summariesListPanel.add(noSummariesLabel);
        } else {
            for (ArchivedSummary summary : summaries) {
                JLabel summaryLabel = new JLabel(
                    "[" + summary.getDateAdded() + "] — " +
                    summary.getSummaryText());
                summaryLabel.setBorder(
                    BorderFactory.createEmptyBorder(5, 10, 5, 10));
                summariesListPanel.add(summaryLabel);
                summariesListPanel.add(new JSeparator());
            }
        }

        summariesListPanel.revalidate();
        summariesListPanel.repaint();
    }

    // Handle permanent delete
    private void handleDelete() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to permanently delete this case?\n" +
            "This action cannot be undone.",
            "Confirm Permanent Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = caseRecordManager.deleteArchivedCaseRecord(
                currentCase.getArchivedCaseId());
            if (success) {
                mainView.getArchivedCasesPanel().loadArchivedCases();
                mainView.showPanel(MainView.ARCHIVED_CASES_PANEL);
            } else {
                errorLabel.setText("Error deleting case. Please try again.");
            }
        }
    }
}
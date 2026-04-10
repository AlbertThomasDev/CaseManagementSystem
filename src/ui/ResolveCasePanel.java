package ui;

import managers.CaseRecordManager;
import model.CaseRecord;

import javax.swing.*;
import java.awt.*;

public class ResolveCasePanel extends JPanel {

    // Reference to MainView
    private MainView mainView;

    // Manager
    private CaseRecordManager caseRecordManager;

    // Current case
    private CaseRecord currentCase;

    // UI components
    private JLabel titleLabel;
    private JLabel caseDetailsLabel;
    private JTextArea summaryInput;
    private JLabel errorLabel;

    // Constructor
    public ResolveCasePanel(MainView mainView) {
        this.mainView = mainView;
        this.caseRecordManager = mainView.getCaseRecordManager();
        initializePanel();
    }

    // Initialize the panel
    private void initializePanel() {
        setLayout(new BorderLayout());

        // Title
        titleLabel = new JLabel("Resolve Case Record", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Case details for context
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Case Details"));
        caseDetailsLabel = new JLabel();
        caseDetailsLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        detailsPanel.add(caseDetailsLabel, BorderLayout.CENTER);
        centerPanel.add(detailsPanel, BorderLayout.NORTH);

        // Resolve summary section
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(
            BorderFactory.createTitledBorder("Resolve Summary (Mandatory)"));

        summaryInput = new JTextArea(4, 20);
        summaryInput.setLineWrap(true);
        summaryInput.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(summaryInput);
        summaryPanel.add(scrollPane, BorderLayout.CENTER);

        // Error label
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        summaryPanel.add(errorLabel, BorderLayout.SOUTH);

        centerPanel.add(summaryPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Resolve button
        JButton resolveBtn = new JButton("Resolve Case");
        resolveBtn.addActionListener(e -> handleResolve());
        buttonPanel.add(resolveBtn);

        // Back button
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e ->
            mainView.showPanel(MainView.DISPLAY_CASE_PANEL));
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Load case into panel
    public void loadCase(CaseRecord caseRecord) {
        this.currentCase = caseRecord;
        titleLabel.setText("Resolve Case - " + caseRecord.getDefendantName());
        caseDetailsLabel.setText(
            "<html>" +
            "Case ID: " + caseRecord.getCaseId() + "<br>" +
            "Defendant: " + caseRecord.getDefendantName() + "<br>" +
            "Case Type: " + caseRecord.getCaseType() + "<br>" +
            "Hearing Date: " + caseRecord.getHearingDate() + "<br>" +
            "Status: " + caseRecord.getCaseStatus() +
            "</html>"
        );
        summaryInput.setText("");
        errorLabel.setText("");
    }

    // Handle resolve
    private void handleResolve() {
        String summaryText = summaryInput.getText().trim();

        if (summaryText.isEmpty()) {
            errorLabel.setText("Resolve summary is mandatory.");
            return;
        }

        boolean success = caseRecordManager.resolveCaseRecord(
            currentCase.getCaseId(), summaryText);

        if (success) {
            JOptionPane.showMessageDialog(this,
                "Case resolved successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            mainView.loadCaseRecords();
            mainView.showPanel(MainView.MAIN_PANEL);
        } else {
            errorLabel.setText("Error resolving case. Please try again.");
        }
    }
}
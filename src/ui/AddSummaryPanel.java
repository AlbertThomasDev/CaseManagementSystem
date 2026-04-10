package ui;

import managers.CaseRecordManager;
import model.CaseRecord;
import model.Summary;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddSummaryPanel extends JPanel {

    // Reference to MainView
    private MainView mainView;

    // Manager
    private CaseRecordManager caseRecordManager;

    // Current case
    private CaseRecord currentCase;

    // UI components
    private JTextArea summaryInput;
    private JPanel summariesListPanel;
    private JLabel errorLabel;
    private JLabel titleLabel;

    // Constructor
    public AddSummaryPanel(MainView mainView) {
        this.mainView = mainView;
        this.caseRecordManager = mainView.getCaseRecordManager();
        initializePanel();
    }

    // Initialize the panel
    private void initializePanel() {
        setLayout(new BorderLayout());

        // Title
        titleLabel = new JLabel("Summaries", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Center panel — existing summaries + input
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Existing summaries section
        JPanel existingSummariesPanel = new JPanel(new BorderLayout());
        existingSummariesPanel.setBorder(
            BorderFactory.createTitledBorder("Existing Summaries"));

        summariesListPanel = new JPanel();
        summariesListPanel.setLayout(
            new BoxLayout(summariesListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(summariesListPanel);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        existingSummariesPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(existingSummariesPanel, BorderLayout.CENTER);

        // Add summary section
        JPanel addSummarySection = new JPanel(new BorderLayout());
        addSummarySection.setBorder(
            BorderFactory.createTitledBorder("Add New Summary"));

        summaryInput = new JTextArea(4, 20);
        summaryInput.setLineWrap(true);
        summaryInput.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane(summaryInput);
        addSummarySection.add(inputScrollPane, BorderLayout.CENTER);

        // Error label
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        addSummarySection.add(errorLabel, BorderLayout.SOUTH);

        centerPanel.add(addSummarySection, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Submit button
        JButton submitBtn = new JButton("Add Summary");
        submitBtn.addActionListener(e -> handleSubmit());
        buttonPanel.add(submitBtn);

        // Back button
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> 
            mainView.showPanel(MainView.DISPLAY_CASE_PANEL));
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Load case and summaries
    public void loadCase(CaseRecord caseRecord) {
        this.currentCase = caseRecord;
        titleLabel.setText("Summaries — " + caseRecord.getDefendantName());
        loadSummaries();
        summaryInput.setText("");
        errorLabel.setText("");
    }

    // Load existing summaries into scroll pane
    private void loadSummaries() {
        summariesListPanel.removeAll();
        List<Summary> summaries = 
            caseRecordManager.getSummariesByCaseId(currentCase.getCaseId());

        if (summaries.isEmpty()) {
            JLabel noSummariesLabel = new JLabel("No summaries yet.");
            noSummariesLabel.setBorder(
                BorderFactory.createEmptyBorder(5, 10, 5, 10));
            summariesListPanel.add(noSummariesLabel);
        } else {
            for (Summary summary : summaries) {
                JLabel summaryLabel = new JLabel(
                    "[" + summary.getDateAdded() + "] - " + 
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

    // Handle submit
    private void handleSubmit() {
        String summaryText = summaryInput.getText().trim();

        if (summaryText.isEmpty()) {
            errorLabel.setText("Summary cannot be empty.");
            return;
        }

        boolean success = caseRecordManager.createSummary(
            currentCase.getCaseId(), summaryText);

        if (success) {
            summaryInput.setText("");
            errorLabel.setText("");
            loadSummaries(); // refresh summaries list
        } else {
            errorLabel.setText("Error adding summary. Please try again.");
        }
    }
}
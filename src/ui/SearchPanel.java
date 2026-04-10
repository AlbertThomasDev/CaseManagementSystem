package ui;

import managers.SearchManager;
import model.CaseRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class SearchPanel extends JPanel {

    // Reference to MainView
    private MainView mainView;

    // SearchManager
    private SearchManager searchManager;

    // Criteria fields
    private JTextField defendantNameField;
    private JTextField caseTypeField;
    private JTextField hearingDateField;
    private JTextField caseStatusField;

    // Results table
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    // Constructor
    public SearchPanel(MainView mainView) {
        this.mainView = mainView;
        this.searchManager = new SearchManager(mainView.getCaseRecordManager());
        initializePanel();
    }

    // Initialize the panel
    private void initializePanel() {
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Search Case Records", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Criteria panel
        JPanel criteriaPanel = new JPanel(new GridBagLayout());
        criteriaPanel.setBorder(BorderFactory.createTitledBorder("Search Criteria"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Defendant Name
        gbc.gridx = 0; gbc.gridy = 0;
        criteriaPanel.add(new JLabel("Defendant Name:"), gbc);
        gbc.gridx = 1;
        defendantNameField = new JTextField(15);
        // defendantNameField.getDocument().addDocumentListener(
        //     new javax.swing.event.DocumentListener() {
        //         public void insertUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
        //         public void removeUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
        //         public void changedUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
        //     });
        criteriaPanel.add(defendantNameField, gbc);

        // Case Type
        gbc.gridx = 2; gbc.gridy = 0;
        criteriaPanel.add(new JLabel("Case Type:"), gbc);
        gbc.gridx = 3;
        caseTypeField = new JTextField(15);
        // caseTypeField.getDocument().addDocumentListener(
        //     new javax.swing.event.DocumentListener() {
        //         public void insertUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
        //         public void removeUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
        //         public void changedUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
        //     });
        criteriaPanel.add(caseTypeField, gbc);

        // Hearing Date
        gbc.gridx = 0; gbc.gridy = 1;
        criteriaPanel.add(new JLabel("Hearing Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        hearingDateField = new JTextField(15);
        // hearingDateField.getDocument().addDocumentListener(
        //     new javax.swing.event.DocumentListener() {
        //         public void insertUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
        //         public void removeUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
        //         public void changedUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
        //     });
        criteriaPanel.add(hearingDateField, gbc);

        // Case Status
        gbc.gridx = 2; gbc.gridy = 1;
        criteriaPanel.add(new JLabel("Case Status:"), gbc);
        gbc.gridx = 3;
        caseStatusField = new JTextField(15);
        // caseStatusField.getDocument().addDocumentListener(
        //     new javax.swing.event.DocumentListener() {
        //         public void insertUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
        //         public void removeUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
        //         public void changedUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
        //     });
        criteriaPanel.add(caseStatusField, gbc);

        // Search button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> updateResults());
        criteriaPanel.add(searchBtn, gbc);

        add(criteriaPanel, BorderLayout.NORTH);

        // Results table
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Results"));

        String[] columns = {"Case ID", "Defendant Name",
                            "Case Type", "Hearing Date", "Case Status", "View"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        resultsTable = new JTable(tableModel);
        resultsTable.setFillsViewportHeight(true);
        resultsTable.setRowHeight(30);

        // Set View button renderer and editor
        resultsTable.getColumn("View").setCellRenderer(
            new MainView.ViewButtonRenderer());
        resultsTable.getColumn("View").setCellEditor(
            new MainView.ViewButtonEditor(new JCheckBox(), mainView, tableModel));

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        add(resultsPanel, BorderLayout.CENTER);

        // Load all records initially
        updateResults();
    }

    // Update results table based on criteria
    private void updateResults() {
        String defendantName = defendantNameField.getText().trim();
        String caseType = caseTypeField.getText().trim();
        String hearingDate = hearingDateField.getText().trim();
        String caseStatus = caseStatusField.getText().trim();

        List<CaseRecord> results = searchManager.search(
            defendantName, caseType, hearingDate, caseStatus);

        tableModel.setRowCount(0);
        for (CaseRecord record : results) {
            tableModel.addRow(new Object[]{
                record.getCaseId(),
                record.getDefendantName(),
                record.getCaseType(),
                record.getHearingDate(),
                record.getCaseStatus(),
                "View"
            });
        }
    }
}
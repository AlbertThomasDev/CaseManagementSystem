package ui;

import managers.CaseRecordManager;
import model.ArchivedCaseRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ArchivedCasesPanel extends JPanel {

    // Reference to MainView
    private MainView mainView;

    // Manager
    private CaseRecordManager caseRecordManager;

    // Table
    private JTable archivedTable;
    private DefaultTableModel tableModel;

    // Constructor
    public ArchivedCasesPanel(MainView mainView) {
        this.mainView = mainView;
        this.caseRecordManager = mainView.getCaseRecordManager();
        initializePanel();
    }

    // Initialize the panel
    private void initializePanel() {
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Archived Case Records", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Archived ID", "Defendant Name",
                            "Case Type", "Hearing Date",
                            "Case Status", "Archive Date", "View"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        archivedTable = new JTable(tableModel);
        archivedTable.setFillsViewportHeight(true);
        archivedTable.setRowHeight(30);

        // Set View button renderer and editor
        archivedTable.getColumn("View").setCellRenderer(
            new MainView.ViewButtonRenderer());
        archivedTable.getColumn("View").setCellEditor(
            new ArchivedViewButtonEditor(new JCheckBox(), mainView, tableModel));

        JScrollPane scrollPane = new JScrollPane(archivedTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e ->
            mainView.showPanel(MainView.MAIN_PANEL));
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Load archived case records
    public void loadArchivedCases() {
        tableModel.setRowCount(0);
        List<ArchivedCaseRecord> records =
            caseRecordManager.getAllArchivedCaseRecords();
        for (ArchivedCaseRecord record : records) {
            tableModel.addRow(new Object[]{
                record.getArchivedCaseId(),
                record.getDefendantName(),
                record.getCaseType(),
                record.getHearingDate(),
                record.getCaseStatus(),
                record.getArchiveDate(),
                "View"
            });
        }
    }

    // Archived View button editor — navigates to DisplayArchivedCasePanel
    static class ArchivedViewButtonEditor extends DefaultCellEditor {
        private JButton button;
        private MainView mainView;
        private int selectedRow;
        private DefaultTableModel tableModel;

        public ArchivedViewButtonEditor(JCheckBox checkBox, MainView mainView,
                                        DefaultTableModel tableModel) {
            super(checkBox);
            this.mainView = mainView;
            this.tableModel = tableModel;
            button = new JButton("View");
            button.addActionListener(e -> {
                int archivedCaseId = (int) tableModel.getValueAt(selectedRow, 0);
                mainView.viewArchivedCase(archivedCaseId);
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table,
                Object value, boolean isSelected, int row, int column) {
            selectedRow = row;
            return button;
        }
    }
}
package ui;

import managers.CaseRecordManager;
import model.CaseRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainView extends JFrame {

    // CardLayout and main panel
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Table
    private JTable caseTable;
    private DefaultTableModel tableModel;

    // Manager
    private CaseRecordManager caseRecordManager;

    // Panel names
    public static final String MAIN_PANEL = "MainPanel";
    public static final String CREATE_CASE_PANEL = "CreateCasePanel";

    // Constructor
    public MainView() {
        caseRecordManager = new CaseRecordManager();
        initializeWindow();
        initializeCardLayout();
        initializeNavigationMenu();
        initializeMainPanel();
        loadCaseRecords();

        // Add CreateCasePanel to CardLayout
        CreateCasePanel createCasePanel = new CreateCasePanel(this);
        addPanel(createCasePanel, CREATE_CASE_PANEL);
    }

    // Initialize the main window
    private void initializeWindow() {
        setTitle("Case Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    // Initialize CardLayout
    private void initializeCardLayout() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel, BorderLayout.CENTER);
    }

    // Initialize navigation menu
    private void initializeNavigationMenu() {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navPanel.setBackground(Color.DARK_GRAY);

        // Create Case button
        JButton createCaseBtn = new JButton("Create Case");
        createCaseBtn.addActionListener(e -> showPanel(CREATE_CASE_PANEL));
        navPanel.add(createCaseBtn);

        add(navPanel, BorderLayout.NORTH);
    }

    // Initialize main panel with table
    private void initializeMainPanel() {
        JPanel homePanel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("All Case Records", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        homePanel.add(titleLabel, BorderLayout.NORTH);

        // Table columns
        String[] columns = {"Case ID", "Defendant Name", 
                            "Case Type", "Hearing Date", "Case Status", "View"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // table is read only
            }
        };

        caseTable = new JTable(tableModel);
        caseTable.setFillsViewportHeight(true);
        caseTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(caseTable);
        homePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(homePanel, MAIN_PANEL);
    }

    // Load case records into table
    public void loadCaseRecords() {
        tableModel.setRowCount(0); // clear table
        List<CaseRecord> records = caseRecordManager.getAllCaseRecords();
        for (CaseRecord record : records) {
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

    // Show a specific panel
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    // Add a panel to the CardLayout
    public void addPanel(JPanel panel, String panelName) {
        mainPanel.add(panel, panelName);
    }

    // Get CaseRecordManager
    public CaseRecordManager getCaseRecordManager() {
        return caseRecordManager;
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }
}
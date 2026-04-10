package ui;

import managers.CaseRecordManager;
import managers.NotificationScheduler;
import model.ArchivedCaseRecord;
import model.CaseRecord;
import model.Notification;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
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

    // Notification scheduler
    private NotificationScheduler notificationScheduler;

    // Panels
    private DisplayCasePanel displayCasePanel;
    private AddSummaryPanel addSummaryPanel;
    private ResolveCasePanel resolveCasePanel;
    private ArchivedCasesPanel archivedCasesPanel;
    private DisplayArchivedCasePanel displayArchivedCasePanel;
    private NotificationPanel notificationPanel;

    // Panel names
    public static final String MAIN_PANEL = "MainPanel";
    public static final String CREATE_CASE_PANEL = "CreateCasePanel";
    public static final String DISPLAY_CASE_PANEL = "DisplayCasePanel";
    public static final String RESOLVE_CASE_PANEL = "ResolveCasePanel";
    public static final String ADD_SUMMARY_PANEL = "AddSummaryPanel";
    public static final String SEARCH_PANEL = "SearchPanel";
    public static final String ARCHIVED_CASES_PANEL = "ArchivedCasesPanel";
    public static final String DISPLAY_ARCHIVED_CASE_PANEL = "DisplayArchivedCasePanel";
    public static final String NOTIFICATION_PANEL = "NotificationPanel";

    // Constructor
    public MainView() {
        caseRecordManager = new CaseRecordManager();
        initializeWindow();
        initializeCardLayout();
        initializeNavigationMenu();
        initializeMainPanel();
        loadCaseRecords();

        // Add panels to CardLayout
        CreateCasePanel createCasePanel = new CreateCasePanel(this);
        addPanel(createCasePanel, CREATE_CASE_PANEL);

        displayCasePanel = new DisplayCasePanel(this);
        addPanel(displayCasePanel, DISPLAY_CASE_PANEL);

        addSummaryPanel = new AddSummaryPanel(this);
        addPanel(addSummaryPanel, ADD_SUMMARY_PANEL);

        resolveCasePanel = new ResolveCasePanel(this);
        addPanel(resolveCasePanel, RESOLVE_CASE_PANEL);

        archivedCasesPanel = new ArchivedCasesPanel(this);
        addPanel(archivedCasesPanel, ARCHIVED_CASES_PANEL);

        displayArchivedCasePanel = new DisplayArchivedCasePanel(this);
        addPanel(displayArchivedCasePanel, DISPLAY_ARCHIVED_CASE_PANEL);

        notificationPanel = new NotificationPanel(this);
        addPanel(notificationPanel, NOTIFICATION_PANEL);

        SearchPanel searchPanel = new SearchPanel(this);
        addPanel(searchPanel, SEARCH_PANEL);

        // Start notification scheduler
        notificationScheduler = new NotificationScheduler(caseRecordManager);
        List<Notification> notifications = 
            notificationScheduler.checkForUpcomingHearings();
        notificationPanel.showNotificationPopup(notifications);
        notificationScheduler.start();
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
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        navPanel.setBackground(Color.DARK_GRAY);

        // Main Menu button
        JButton mainMenuBtn = new JButton("Main Menu");
        mainMenuBtn.addActionListener(e -> showPanel(MAIN_PANEL));
        navPanel.add(mainMenuBtn);
        
        // Create Case button
        JButton createCaseBtn = new JButton("Create Case");
        createCaseBtn.addActionListener(e -> showPanel(CREATE_CASE_PANEL));
        navPanel.add(createCaseBtn);

        // Search button
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> showPanel(SEARCH_PANEL));
        navPanel.add(searchBtn);

        // Archived Cases button
        JButton archivedBtn = new JButton("Archived Cases");
        archivedBtn.addActionListener(e -> {
            archivedCasesPanel.loadArchivedCases();
            showPanel(ARCHIVED_CASES_PANEL);
        });
        navPanel.add(archivedBtn);

        // Notifications button
        JButton notificationsBtn = new JButton("Notifications");
        notificationsBtn.addActionListener(e -> {
            notificationPanel.loadNotifications();
            showPanel(NOTIFICATION_PANEL);
        });
        navPanel.add(notificationsBtn);

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
                return column == 5;
            }
        };

        caseTable = new JTable(tableModel);
        caseTable.setFillsViewportHeight(true);
        caseTable.setRowHeight(30);

        // Set View button renderer and editor
        caseTable.getColumn("View").setCellRenderer(new ViewButtonRenderer());
        caseTable.getColumn("View").setCellEditor(
            new ViewButtonEditor(new JCheckBox(), this, tableModel));

        JScrollPane scrollPane = new JScrollPane(caseTable);
        homePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(homePanel, MAIN_PANEL);
    }

    // Load case records into table
    public void loadCaseRecords() {
        tableModel.setRowCount(0);
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

    // Navigate to DisplayCasePanel with selected case
    public void viewCase(int caseId) {
        List<CaseRecord> records = caseRecordManager.getAllCaseRecords();
        for (CaseRecord record : records) {
            if (record.getCaseId() == caseId) {
                displayCasePanel.loadCase(record);
                showPanel(DISPLAY_CASE_PANEL);
                return;
            }
        }
    }

    // Navigate to DisplayArchivedCasePanel with selected archived case
    public void viewArchivedCase(int archivedCaseId) {
        ArchivedCaseRecord record =
            caseRecordManager.getArchivedCaseById(archivedCaseId);
        if (record != null) {
            displayArchivedCasePanel.loadArchivedCase(record);
            showPanel(DISPLAY_ARCHIVED_CASE_PANEL);
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

    // Get DisplayCasePanel
    public DisplayCasePanel getDisplayCasePanel() {
        return displayCasePanel;
    }

    // Get AddSummaryPanel
    public AddSummaryPanel getAddSummaryPanel() {
        return addSummaryPanel;
    }

    // Get ResolveCasePanel
    public ResolveCasePanel getResolveCasePanel() {
        return resolveCasePanel;
    }

    // Get ArchivedCasesPanel
    public ArchivedCasesPanel getArchivedCasesPanel() {
        return archivedCasesPanel;
    }

    // Get DisplayArchivedCasePanel
    public DisplayArchivedCasePanel getDisplayArchivedCasePanel() {
        return displayArchivedCasePanel;
    }

    // Get NotificationPanel
    public NotificationPanel getNotificationPanel() {
        return notificationPanel;
    }

    // Shutdown scheduler on close
    public void shutdown() {
        notificationScheduler.stop();
    }

    // View button renderer — public static for reuse
    public static class ViewButtonRenderer extends JButton
            implements TableCellRenderer {
        public ViewButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            setText("View");
            return this;
        }
    }

    // View button editor — public static for reuse
    public static class ViewButtonEditor extends DefaultCellEditor {
        private JButton button;
        private MainView mainView;
        private int selectedRow;
        private DefaultTableModel tableModel;

        public ViewButtonEditor(JCheckBox checkBox, MainView mainView,
                                DefaultTableModel tableModel) {
            super(checkBox);
            this.mainView = mainView;
            this.tableModel = tableModel;
            button = new JButton("View");
            button.addActionListener(e -> {
                int caseId = (int) tableModel.getValueAt(selectedRow, 0);
                mainView.viewCase(caseId);
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

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
            // Shutdown scheduler when app closes
            mainView.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    mainView.shutdown();
                }
            });
        });
    }
}
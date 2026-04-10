package managers;

import database.DatabaseConnection;
import model.ArchivedCaseRecord;
import model.ArchivedSummary;
import model.CaseRecord;
import model.Summary;
import model.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CaseRecordManager {

    private Connection connection;

    // Constructor
    public CaseRecordManager() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        createTable();
        createSummariesTable();
        createArchivedCasesTable();
        createArchivedSummariesTable();
        createNotificationsTable();
    }

    // Creates cases table
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS cases (" +
                     "case_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "defendant_name TEXT NOT NULL, " +
                     "defendant_dob TEXT NOT NULL, " +
                     "case_type TEXT NOT NULL, " +
                     "hearing_date TEXT NOT NULL, " +
                     "case_status TEXT NOT NULL DEFAULT 'Open'" +
                     ")";
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            System.out.println("Cases table ready.");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    // Creates summaries table
    private void createSummariesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS summaries (" +
                     "summary_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "case_id INTEGER NOT NULL, " +
                     "summary_text TEXT NOT NULL, " +
                     "date_added TEXT NOT NULL, " +
                     "FOREIGN KEY (case_id) REFERENCES cases(case_id)" +
                     ")";
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            System.out.println("Summaries table ready.");
        } catch (SQLException e) {
            System.out.println("Error creating summaries table: " + e.getMessage());
        }
    }

    // Creates archived cases table
    private void createArchivedCasesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS archived_cases (" +
                     "archived_case_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "original_case_id INTEGER NOT NULL, " +
                     "defendant_name TEXT NOT NULL, " +
                     "defendant_dob TEXT NOT NULL, " +
                     "case_type TEXT NOT NULL, " +
                     "hearing_date TEXT NOT NULL, " +
                     "case_status TEXT NOT NULL, " +
                     "archive_date TEXT NOT NULL" +
                     ")";
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            System.out.println("Archived cases table ready.");
        } catch (SQLException e) {
            System.out.println("Error creating archived cases table: " + e.getMessage());
        }
    }

    // Creates archived summaries table
    private void createArchivedSummariesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS archived_summaries (" +
                     "archived_summary_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "archived_case_id INTEGER NOT NULL, " +
                     "summary_text TEXT NOT NULL, " +
                     "date_added TEXT NOT NULL, " +
                     "FOREIGN KEY (archived_case_id) " +
                     "REFERENCES archived_cases(archived_case_id)" +
                     ")";
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            System.out.println("Archived summaries table ready.");
        } catch (SQLException e) {
            System.out.println("Error creating archived summaries table: " + e.getMessage());
        }
    }

    // Creates a new case record
    public boolean createCaseRecord(String defendantName, String defendantDob,
                                     String caseType, String hearingDate) {
        String sql = "INSERT INTO cases (defendant_name, defendant_dob, " +
                     "case_type, hearing_date, case_status) " +
                     "VALUES (?, ?, ?, ?, 'Open')";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, defendantName);
            pstmt.setString(2, defendantDob);
            pstmt.setString(3, caseType);
            pstmt.setString(4, hearingDate);
            pstmt.executeUpdate();
            System.out.println("Case record created successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error creating case record: " + e.getMessage());
            return false;
        }
    }

    // Retrieves all case records sorted by hearing date ascending
    public List<CaseRecord> getAllCaseRecords() {
        List<CaseRecord> caseRecords = new ArrayList<>();
        String sql = "SELECT * FROM cases ORDER BY hearing_date ASC";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                CaseRecord record = new CaseRecord(
                    rs.getInt("case_id"),
                    rs.getString("defendant_name"),
                    rs.getString("defendant_dob"),
                    rs.getString("case_type"),
                    rs.getString("hearing_date"),
                    rs.getString("case_status")
                );
                caseRecords.add(record);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving case records: " + e.getMessage());
        }
        return caseRecords;
    }

    // Updates an existing case record
    public boolean updateCaseRecord(CaseRecord caseRecord) {
        String sql = "UPDATE cases SET defendant_name = ?, " +
                     "defendant_dob = ?, case_type = ?, " +
                     "hearing_date = ? WHERE case_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, caseRecord.getDefendantName());
            pstmt.setString(2, caseRecord.getDefendantDob());
            pstmt.setString(3, caseRecord.getCaseType());
            pstmt.setString(4, caseRecord.getHearingDate());
            pstmt.setInt(5, caseRecord.getCaseId());
            pstmt.executeUpdate();
            System.out.println("Case record updated successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating case record: " + e.getMessage());
            return false;
        }
    }

    // Resolves a case record
    public boolean resolveCaseRecord(int caseId, String summaryText) {
        String updateSql = "UPDATE cases SET case_status = 'Resolved' " +
                           "WHERE case_id = ?";
        try {
            // Update case status
            PreparedStatement pstmt = connection.prepareStatement(updateSql);
            pstmt.setInt(1, caseId);
            pstmt.executeUpdate();

            // Create resolve summary
            createSummary(caseId, summaryText);

            System.out.println("Case record resolved successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error resolving case record: " + e.getMessage());
            return false;
        }
    }

    // Archives a case record — moves to archived tables
    public boolean archiveCaseRecord(CaseRecord caseRecord) {
        String archiveDate = java.time.LocalDate.now().toString();
        try {
            // 1. Insert into archived_cases
            String insertArchivedSql = 
                "INSERT INTO archived_cases (original_case_id, defendant_name, " +
                "defendant_dob, case_type, hearing_date, case_status, archive_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertArchived = 
                connection.prepareStatement(insertArchivedSql,
                Statement.RETURN_GENERATED_KEYS);
            insertArchived.setInt(1, caseRecord.getCaseId());
            insertArchived.setString(2, caseRecord.getDefendantName());
            insertArchived.setString(3, caseRecord.getDefendantDob());
            insertArchived.setString(4, caseRecord.getCaseType());
            insertArchived.setString(5, caseRecord.getHearingDate());
            insertArchived.setString(6, caseRecord.getCaseStatus());
            insertArchived.setString(7, archiveDate);
            insertArchived.executeUpdate();

            // Get generated archived_case_id
            ResultSet generatedKeys = insertArchived.getGeneratedKeys();
            int archivedCaseId = generatedKeys.getInt(1);

            // 2. Move summaries to archived_summaries
            List<Summary> summaries = 
                getSummariesByCaseId(caseRecord.getCaseId());
            for (Summary summary : summaries) {
                String insertArchivedSummary =
                    "INSERT INTO archived_summaries " +
                    "(archived_case_id, summary_text, date_added) " +
                    "VALUES (?, ?, ?)";
                PreparedStatement insertSummary = 
                    connection.prepareStatement(insertArchivedSummary);
                insertSummary.setInt(1, archivedCaseId);
                insertSummary.setString(2, summary.getSummaryText());
                insertSummary.setString(3, summary.getDateAdded());
                insertSummary.executeUpdate();
            }

            // 3. Delete summaries from summaries table
            String deleteSummaries = 
                "DELETE FROM summaries WHERE case_id = ?";
            PreparedStatement deleteSum = 
                connection.prepareStatement(deleteSummaries);
            deleteSum.setInt(1, caseRecord.getCaseId());
            deleteSum.executeUpdate();

            // 4. Delete case from cases table
            String deleteCase = "DELETE FROM cases WHERE case_id = ?";
            PreparedStatement deleteCaseStmt = 
                connection.prepareStatement(deleteCase);
            deleteCaseStmt.setInt(1, caseRecord.getCaseId());
            deleteCaseStmt.executeUpdate();

            System.out.println("Case record archived successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error archiving case record: " + e.getMessage());
            return false;
        }
    }

    // Permanently deletes an archived case record
    public boolean deleteArchivedCaseRecord(int archivedCaseId) {
        try {
            // Delete archived summaries first
            String deleteArchivedSummaries =
                "DELETE FROM archived_summaries WHERE archived_case_id = ?";
            PreparedStatement deleteSum = 
                connection.prepareStatement(deleteArchivedSummaries);
            deleteSum.setInt(1, archivedCaseId);
            deleteSum.executeUpdate();

            // Delete archived case
            String deleteArchivedCase =
                "DELETE FROM archived_cases WHERE archived_case_id = ?";
            PreparedStatement deleteCase = 
                connection.prepareStatement(deleteArchivedCase);
            deleteCase.setInt(1, archivedCaseId);
            deleteCase.executeUpdate();

            System.out.println("Archived case record deleted successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting archived case record: " + e.getMessage());
            return false;
        }
    }

    // Creates a new summary
    public boolean createSummary(int caseId, String summaryText) {
        String dateAdded = java.time.LocalDate.now().toString();
        String sql = "INSERT INTO summaries (case_id, summary_text, date_added) " +
                     "VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, caseId);
            pstmt.setString(2, summaryText);
            pstmt.setString(3, dateAdded);
            pstmt.executeUpdate();
            System.out.println("Summary created successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error creating summary: " + e.getMessage());
            return false;
        }
    }

    // Fetches all summaries for a case
    public List<Summary> getSummariesByCaseId(int caseId) {
        List<Summary> summaries = new ArrayList<>();
        String sql = "SELECT * FROM summaries WHERE case_id = ? " +
                     "ORDER BY date_added ASC";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, caseId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Summary summary = new Summary(
                    rs.getInt("summary_id"),
                    rs.getInt("case_id"),
                    rs.getString("summary_text"),
                    rs.getString("date_added")
                );
                summaries.add(summary);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching summaries: " + e.getMessage());
        }
        return summaries;
    }

    // Fetches all archived case records
    public List<ArchivedCaseRecord> getAllArchivedCaseRecords() {
        List<ArchivedCaseRecord> archivedCases = new ArrayList<>();
        String sql = "SELECT * FROM archived_cases ORDER BY archive_date ASC";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ArchivedCaseRecord record = new ArchivedCaseRecord(
                    rs.getInt("archived_case_id"),
                    rs.getInt("original_case_id"),
                    rs.getString("defendant_name"),
                    rs.getString("defendant_dob"),
                    rs.getString("case_type"),
                    rs.getString("hearing_date"),
                    rs.getString("case_status"),
                    rs.getString("archive_date")
                );
                archivedCases.add(record);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving archived case records: " + e.getMessage());
        }
        return archivedCases;
    }

    // Fetches all archived summaries for an archived case
    public List<ArchivedSummary> getArchivedSummariesByCaseId(int archivedCaseId) {
        List<ArchivedSummary> summaries = new ArrayList<>();
        String sql = "SELECT * FROM archived_summaries " +
                     "WHERE archived_case_id = ? ORDER BY date_added ASC";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, archivedCaseId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ArchivedSummary summary = new ArchivedSummary(
                    rs.getInt("archived_summary_id"),
                    rs.getInt("archived_case_id"),
                    rs.getString("summary_text"),
                    rs.getString("date_added")
                );
                summaries.add(summary);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching archived summaries: " + e.getMessage());
        }
        return summaries;
    }

    // Fetches a single archived case record by ID
    public ArchivedCaseRecord getArchivedCaseById(int archivedCaseId) {
        String sql = "SELECT * FROM archived_cases WHERE archived_case_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, archivedCaseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new ArchivedCaseRecord(
                    rs.getInt("archived_case_id"),
                    rs.getInt("original_case_id"),
                    rs.getString("defendant_name"),
                    rs.getString("defendant_dob"),
                    rs.getString("case_type"),
                    rs.getString("hearing_date"),
                    rs.getString("case_status"),
                    rs.getString("archive_date")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching archived case: " + e.getMessage());
        }
        return null;
    }

    // Creates notifications table
    private void createNotificationsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS notifications (" +
                    "notification_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "case_id INTEGER NOT NULL, " +
                    "message TEXT NOT NULL, " +
                    "hearing_date TEXT NOT NULL, " +
                    "date_created TEXT NOT NULL" +
                    ")";
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            System.out.println("Notifications table ready.");
        } catch (SQLException e) {
            System.out.println("Error creating notifications table: " + e.getMessage());
        }
    }

    // Creates a new notification
    public boolean createNotification(int caseId, String message,
                                    String hearingDate) {
        // Check if notification already exists for this case
        if (notificationExists(caseId)) {
            return false;
        }
        String dateCreated = java.time.LocalDate.now().toString();
        String sql = "INSERT INTO notifications (case_id, message, " +
                    "hearing_date, date_created) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, caseId);
            pstmt.setString(2, message);
            pstmt.setString(3, hearingDate);
            pstmt.setString(4, dateCreated);
            pstmt.executeUpdate();
            System.out.println("Notification created successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error creating notification: " + e.getMessage());
            return false;
        }
    }

    // Checks if notification already exists for a case
    public boolean notificationExists(int caseId) {
        String sql = "SELECT * FROM notifications WHERE case_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, caseId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking notification: " + e.getMessage());
            return false;
        }
    }

    // Fetches all notifications
    public List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications ORDER BY hearing_date ASC";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Notification notification = new Notification(
                    rs.getInt("notification_id"),
                    rs.getInt("case_id"),
                    rs.getString("message"),
                    rs.getString("hearing_date"),
                    rs.getString("date_created")
                );
                notifications.add(notification);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching notifications: " + e.getMessage());
        }
        return notifications;
    }

    // Removes notifications 5 days after hearing date has passed
    public void cleanupOldNotifications() {
        String sql = "DELETE FROM notifications WHERE " +
                    "date(hearing_date) < date('now', '-5 days')";
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            System.out.println("Old notifications cleaned up.");
        } catch (SQLException e) {
            System.out.println("Error cleaning up notifications: " + e.getMessage());
        }
    }
}
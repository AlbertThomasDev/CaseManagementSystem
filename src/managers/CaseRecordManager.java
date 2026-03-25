package managers;

import database.DatabaseConnection;
import model.CaseRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CaseRecordManager {

    private Connection connection;

    // Constructor
    public CaseRecordManager() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        createTable();
    }

    // Creates the cases table if it does not exist
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
}
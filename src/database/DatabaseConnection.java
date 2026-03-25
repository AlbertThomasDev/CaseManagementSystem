package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Singleton instance
    private static DatabaseConnection instance;
    private Connection connection;

    // Database file path
    private static final String URL = "jdbc:sqlite:casemanagement.db";

    // Private constructor - prevents instantiation from outside
    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    // Returns the single instance
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Returns the connection
    public Connection getConnection() {
        return connection;
    }

    // Closes the connection
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
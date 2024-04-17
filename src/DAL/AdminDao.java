package DAL;

import BE.Admin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {
    // Field to manage database connections
    private ConnectionManager connectionManager;

    // Constructor to initialize the ConnectionManager
    public AdminDao() {
        connectionManager = new ConnectionManager(); // Instance of ConnectionManager to handle database connections
    }

    // Method to fetch all Admin records from the database
    public List<Admin> getAllAdmins() throws SQLException {
        List<Admin> getAllAdmins = new ArrayList<>(); // List to hold Admin objects
        try (Connection con = connectionManager.getConnection()) { // Try-with-resources to ensure closure of connection
            String sql = "SELECT * FROM Admin"; // SQL query to fetch all admins
            Statement statement = con.createStatement(); // Create a statement to execute SQL query

            if (statement.execute(sql)) { // Execute SQL query and check if it returns a ResultSet
                ResultSet resultSet = statement.getResultSet(); // Get ResultSet from the statement
                while (resultSet.next()) { // Iterate over each row in the result set
                    int id = resultSet.getInt("id"); // Extract 'id' from current row
                    String username = resultSet.getString("username"); // Extract 'username' from current row
                    String password = resultSet.getString("password"); // Extract 'password' from current row
                    Admin admin = new Admin(id, username, password); // Create a new Admin object
                    getAllAdmins.add(admin); // Add the new Admin object to the list
                }
            }
        }
        return getAllAdmins; // Return the list of Admin objects
    }
}
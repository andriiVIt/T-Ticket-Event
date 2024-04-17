package DAL;

import BE.Coordinator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorDao {
    private static ConnectionManager connectionManager; // Static field to manage database connections

    // Constructor to initialize the ConnectionManager
    public CoordinatorDao() {
        connectionManager = new ConnectionManager(); // Initialize the ConnectionManager instance
    }

    // Method to fetch all coordinators from the database
    public List<Coordinator> getAllCoordinators() throws SQLException {
        List<Coordinator> allCoordinators = new ArrayList<>(); // List to hold Coordinator objects
        try (Connection con = connectionManager.getConnection()) { // Try-with-resources to ensure closure of connection
            String sql = "SELECT * FROM Coordinator;"; // SQL query to fetch all coordinators
            Statement statement = con.createStatement(); // Create a statement to execute SQL query

            if (statement.execute(sql)) { // Execute SQL query and check if it returns a ResultSet
                ResultSet resultSet = statement.getResultSet(); // Get ResultSet from the statement
                while (resultSet.next()) { // Iterate over each row in the result set
                    int id = resultSet.getInt("id"); // Extract 'id' from current row
                    String username = resultSet.getString("username"); // Extract 'username' from current row
                    String password = resultSet.getString("password"); // Extract 'password' from current row
                    Coordinator coordinator = new Coordinator(id, username, password); // Create a new Coordinator object
                    allCoordinators.add(coordinator); // Add the new Coordinator object to the list
                }
            }
        }
        return allCoordinators; // Return the list of Coordinator objects
    }

    // Method to create a new coordinator in the database
    public Coordinator createCoordinator(Coordinator coordinator) throws SQLException {
        try (Connection con = connectionManager.getConnection()) { // Get a connection from the ConnectionManager
            PreparedStatement pst = con.prepareStatement("INSERT INTO Coordinator (username, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, coordinator.getUsername()); // Set username in the prepared statement
            pst.setString(2, coordinator.getPassword()); // Set password in the prepared statement
            pst.execute(); // Execute the insert SQL statement

            if (pst.getGeneratedKeys().next()) { // Check if generated keys are available
                int id = pst.getGeneratedKeys().getInt(1); // Get generated key as coordinator ID
                coordinator.setId(id); // Set ID to the Coordinator object
                return coordinator; // Return the Coordinator with the ID set
            }
        }
        throw new RuntimeException("Id not set"); // Throw an exception if ID was not set
    }

    // Static method to delete a coordinator from the database
    public static void deleteCoordinator(Coordinator coordinator) throws SQLException {
        try (Connection con = connectionManager.getConnection()) { // Get a connection from the ConnectionManager
            PreparedStatement statement = con.prepareStatement("DELETE FROM Coordinator WHERE id = ?;");
            statement.setInt(1, coordinator.getId()); // Set the coordinator ID to delete
            statement.executeUpdate(); // Execute the delete SQL statement
        }
    }

    // Method to fetch a coordinator by their username
    public Coordinator getCoordinatorByUsername(String username) throws SQLException {
        Coordinator coordinator = null; // Initialize coordinator as null
        try (Connection con = connectionManager.getConnection()) { // Get a connection from the ConnectionManager
            String sql = "SELECT * FROM Coordinator WHERE username = ?;"; // SQL query to fetch coordinator by username
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username); // Set the username in the prepared statement

            ResultSet resultSet = pst.executeQuery(); // Execute the query and get the ResultSet
            if (resultSet.next()) { // If the ResultSet has at least one row
                int id = resultSet.getInt("id"); // Extract 'id' from current row
                String password = resultSet.getString("password"); // Extract 'password' from current row
                coordinator = new Coordinator(id, username, password); // Create a new Coordinator object
            }
        }
        return coordinator; // Return the found Coordinator or null
    }
}



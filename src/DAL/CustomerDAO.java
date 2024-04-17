package DAL;

import BE.Customer;
import BE.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private ConnectionManager connectionManager; // Instance to manage database connections

    // Constructor to initialize the ConnectionManager
    public CustomerDAO() {
        connectionManager = new ConnectionManager();
    }

    // Method to retrieve all customers associated with a specific event
    public List<Customer> getAllCustomers(Event event) throws SQLException {
        List<Customer> allCustomers = new ArrayList<>();
        String sql = "SELECT * FROM EventCustomer JOIN Customer ON EventCustomer.customerid = Customer.id WHERE EventCustomer.eventid = ?";
        try (Connection con = connectionManager.getConnection(); // Open a connection
             PreparedStatement statement = con.prepareStatement(sql)) { // Prepare the SQL statement
            statement.setInt(1, event.getId()); // Set the event ID in the query

            try (ResultSet resultSet = statement.executeQuery()) { // Execute the query and process the result set
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    Customer customer = new Customer(id, name, email); // Create a new Customer object
                    allCustomers.add(customer); // Add the Customer to the list
                }
            }
        } catch (SQLException e) {
            throw e; // Re-throw any SQL exceptions
        }
        return allCustomers; // Return the list of customers
    }

    // Method to create a new customer in the database
    public Customer createCustomer(Customer customer) throws SQLException {
        try (Connection con = connectionManager.getConnection()) { // Open a connection
            PreparedStatement pst = con.prepareStatement("INSERT INTO Customer (name, email) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, customer.getName()); // Set the customer's name
            pst.setString(2, customer.getEmail()); // Set the customer's email
            pst.execute(); // Execute the insert statement

            if (pst.getGeneratedKeys().next()) { // Check if a new ID was generated
                int id = pst.getGeneratedKeys().getInt(1);
                customer.setId(id); // Set the new ID on the customer object
                return customer; // Return the customer with the ID set
            }
        }
        throw new RuntimeException("Id not set"); // Throw an exception if no ID was generated
    }

    // Method to delete a customer and all associated records from the database
    public void deleteCustomer(Customer customer) throws SQLException {
        try (Connection con = connectionManager.getConnection()) { // Open a connection
            // Delete all EventCustomer entries for this customer
            PreparedStatement pstEventCustomer = con.prepareStatement("DELETE FROM EventCustomer WHERE customerid = ?");
            pstEventCustomer.setInt(1, customer.getId());
            pstEventCustomer.executeUpdate();

            // Delete all Ticket entries for this customer
            PreparedStatement pstTicket = con.prepareStatement("DELETE FROM Ticket WHERE customerid = ?");
            pstTicket.setInt(1, customer.getId());
            pstTicket.executeUpdate();

            // Finally, delete the customer from the Customer table
            PreparedStatement pstCustomer = con.prepareStatement("DELETE FROM Customer WHERE id = ?");
            pstCustomer.setInt(1, customer.getId());
            pstCustomer.executeUpdate();
        }
    }
}

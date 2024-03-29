package DAL;

import BE.Customer;
import BE.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO { private ConnectionManager connectionManager;

    public CustomerDAO() {
        connectionManager = new ConnectionManager();
    }

    public List<Customer> getAllCustomers(Event event) throws SQLException {
        List<Customer> allCustomers = new ArrayList<>();
        String sql = "SELECT * FROM EventCustomer JOIN Customer ON EventCustomer.customerid = Customer.id WHERE EventCustomer.eventid = ?";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, event.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    Customer customer = new Customer(id, name, email);
                    allCustomers.add(customer);
                }
            }
        } catch (SQLException e) {
            // handle the exception appropriately
            throw e;
        }
        return allCustomers;
    }

    public Customer createCustomer(Customer customer) throws SQLException {
        try (Connection con = connectionManager.getConnection()) {
            PreparedStatement pst = con.prepareStatement("INSERT INTO Customer (name, email) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, customer.getName());
            pst.setString(2, customer.getEmail());
            pst.execute();

            if (pst.getGeneratedKeys().next()) {
                int id = pst.getGeneratedKeys().getInt(1);
                customer.setId(id);
                return customer;
            }
        }
        throw new RuntimeException("Id not set");
    }

    public void deleteCustomer(Customer customer) throws SQLException {
        try (Connection con = connectionManager.getConnection()) {
            PreparedStatement pstEventCustomer = con.prepareStatement("DELETE FROM EventCustomer WHERE customerid = ?");
            pstEventCustomer.setInt(1, customer.getId());
            pstEventCustomer.executeUpdate();

            PreparedStatement pstTicket = con.prepareStatement("DELETE FROM Ticket WHERE customerid = ?");
            pstTicket.setInt(1, customer.getId());
            pstTicket.executeUpdate();

            PreparedStatement pstCustomer = con.prepareStatement("DELETE FROM Customer WHERE id = ?");
            pstCustomer.setInt(1, customer.getId());
            pstCustomer.executeUpdate();
        }
    }
}

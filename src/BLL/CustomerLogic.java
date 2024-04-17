package BLL;

import BE.Customer;
import BE.Event;
import DAL.CustomerDAO;

import java.sql.SQLException;
import java.util.List;

public class CustomerLogic {
    CustomerDAO customerDAO = new CustomerDAO();
    // Method to create a new Customer in the database
    public Customer createCustomer(Customer customer) throws SQLException {
        return customerDAO.createCustomer(customer);
    }
    // Method to retrieve all Customers associated with a specific Event
    public List<Customer> getAllCustomers(Event event) throws SQLException{
        // Calls the DAO method to fetch all Customers attending a specific Event
        return customerDAO.getAllCustomers(event);
    }
    // Method to delete a Customer from the database
    public void deleteCustomer(Customer customer) throws SQLException{
        // Calls the DAO method to remove a specific Customer from the database
        customerDAO.deleteCustomer(customer);
    }
}


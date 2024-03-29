package BLL;

import BE.Customer;
import BE.Event;
import DAL.CustomerDAO;

import java.sql.SQLException;
import java.util.List;

public class CustomerLogic {
    CustomerDAO customerDAO = new CustomerDAO();

    public Customer createCustomer(Customer customer) throws SQLException {
        return customerDAO.createCustomer(customer);
    }

    public List<Customer> getAllCustomers(Event event) throws SQLException{
        return customerDAO.getAllCustomers(event);
    }

    public void deleteCustomer(Customer customer) throws SQLException{
        customerDAO.deleteCustomer(customer);
    }
}

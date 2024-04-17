package BLL;

import BE.Customer;
import BE.Event;
import BE.Ticket;
import DAL.TicketDAO;

import java.sql.SQLException;
import java.util.List;

public class TicketLogic {
    TicketDAO ticketDAO = new TicketDAO();
    // Method to retrieve all tickets for a specific customer and a specific event
    public List<Ticket> getAllTickets(Customer customer, Event event) throws SQLException {
        // Calls the DAO method to fetch all tickets issued to a particular customer for a specified event

        return ticketDAO.getAllTickets(customer, event);
    }
    // Method to create a new ticket in the database
    public Ticket createTicket(Ticket ticket) throws SQLException {
        // Calls the DAO method to insert a new ticket into the database and return the newly created ticket

        return ticketDAO.createTicket(ticket);
    }
}

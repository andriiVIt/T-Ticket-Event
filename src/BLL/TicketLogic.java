package BLL;

import BE.Customer;
import BE.Event;
import BE.Ticket;
import DAL.TicketDAO;

import java.sql.SQLException;
import java.util.List;

public class TicketLogic {
    TicketDAO ticketDAO = new TicketDAO();

    public List<Ticket> getAllTickets(Customer customer, Event event) throws SQLException {
        return ticketDAO.getAllTickets(customer, event);
    }

    public Ticket createTicket(Ticket ticket) throws SQLException {
        return ticketDAO.createTicket(ticket);
    }
}

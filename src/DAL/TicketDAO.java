package DAL;

import BE.Customer;
import BE.Event;
import BE.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketDAO {

    private ConnectionManager connectionManager;

    public TicketDAO() {
        connectionManager = new ConnectionManager();
    }

    public List<Ticket> getAllTickets(Customer customer, Event event) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();

        String sql = "SELECT * FROM Ticket INNER JOIN Customer ON Ticket.customerid = Customer.id INNER JOIN Event ON Ticket.eventid = Event.id WHERE Ticket.customerid = ? AND Ticket.eventid = ?";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, customer.getId());
            statement.setInt(2, event.getId());

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String ticketType = rs.getString("ticketType");

                    Ticket ticket = new Ticket(UUID.fromString(rs.getString("uuid")), ticketType, event, customer);
                    tickets.add(ticket);
                }
            }
        } return tickets;
    }

    public Ticket createTicket(Ticket ticket) throws SQLException {
        try (Connection con = connectionManager.getConnection()) {
            String sqlCommandInsert = "INSERT INTO Ticket (uuid, ticketType, eventid, customerid) VALUES (?, ?, ?, ?)";
            PreparedStatement pstCreateTicket = con.prepareStatement(sqlCommandInsert);
            pstCreateTicket.setString(1, ticket.getUuid().toString());
            pstCreateTicket.setString(2, ticket.getTicketType());
            pstCreateTicket.setInt(3, ticket.getEvent().getId());
            pstCreateTicket.setInt(4, ticket.getCustomer().getId());
            pstCreateTicket.executeUpdate();
        }
        return ticket;
    }
}

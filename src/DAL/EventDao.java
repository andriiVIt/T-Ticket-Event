package DAL;

import BE.Coordinator;
import BE.Event;
import BE.Customer;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventDao {private ConnectionManager connectionManager;

    public EventDao() {
        connectionManager = new ConnectionManager();
    }

    public List<Event> getAllEvents() throws SQLException {
        List<Event> allEvents = new ArrayList<>();

        try (Connection con = connectionManager.getConnection()) {
            String sql = "SELECT * FROM Event";
            Statement statement = con.createStatement();

            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String location = resultSet.getString("location");
                    LocalDate date = resultSet.getDate("date").toLocalDate();
                    float time = resultSet.getFloat("time");
                    String note = resultSet.getString("note");
                    byte[] imageData = resultSet.getBytes("imageData");

                    Event event = new Event(id, name, location, date, time, note, imageData);
                    allEvents.add(event);
                }
            }
        } return allEvents;
    }

    public Event createEvent(Event event) throws SQLException {
        try (Connection con = connectionManager.getConnection()) {
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement pst = con.prepareStatement("INSERT INTO Event (name, location, date, time, note, imageData) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, event.getName());
            pst.setString(2, event.getLocation());
            pst.setDate(3, Date.valueOf(event.getDate()));
            pst.setDouble(4, event.getTime());
            pst.setString(5, event.getNote());
            pst.setBytes(6, event.getImageData());

            pst.execute();

            if (pst.getGeneratedKeys().next()) {
                int id = pst.getGeneratedKeys().getInt(1);
                event.setId(id);
                con.commit();
                return event;
            } else {
                con.rollback();
                throw new RuntimeException("Id not set");
            }
        } catch (SQLException e) {
            try (Connection con = connectionManager.getConnection()) {
                con.rollback();
            }
            throw e;
        }
    }

    public void assignEventCoordinator(Event event, Coordinator coordinator) throws SQLException {
        try (Connection con = connectionManager.getConnection()) {
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement pst = con.prepareStatement("INSERT INTO EventCoordinator(eventid, coordinatorid) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, event.getId());
            pst.setInt(2, coordinator.getId());
            pst.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            try (Connection con = connectionManager.getConnection()) {
                con.rollback();
            }
            throw e;
        }
    }

    public void joinEvent(Event event, Customer customer) throws SQLException {
        try (Connection con = connectionManager.getConnection()) {
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement pst = con.prepareStatement("INSERT INTO EventCustomer(eventid, customerid) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, event.getId());
            pst.setInt(2, customer.getId());
            pst.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            try (Connection con = connectionManager.getConnection()) {
                con.rollback();
            }
            throw e;
        }
    }

    public void deleteEvent(Event event) throws SQLException {
        // Initialize the connection outside the try-with-resources block to use it in the catch block for rollback
        Connection con = null;
        try {
            // Get the connection from the connection manager
            con = connectionManager.getConnection();
            // Turn off auto-commit to manage transactions manually
            con.setAutoCommit(false);
            // Set a transaction isolation level, if needed
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            // Delete from EventCustomer where the event ID matches
            try (PreparedStatement pstEventCustomer = con.prepareStatement("DELETE FROM EventCustomer WHERE eventid = ?")) {
                pstEventCustomer.setInt(1, event.getId());
                pstEventCustomer.executeUpdate();
            }

            // Delete from EventCoordinator where the event ID matches
            try (PreparedStatement pstEventCoordinator = con.prepareStatement("DELETE FROM EventCoordinator WHERE eventid = ?")) {
                pstEventCoordinator.setInt(1, event.getId());
                pstEventCoordinator.executeUpdate();
            }

            // Finally, delete from Event where the event ID matches
            try (PreparedStatement pstEvent = con.prepareStatement("DELETE FROM Event WHERE id = ?")) {
                pstEvent.setInt(1, event.getId());
                pstEvent.executeUpdate();
            }

            // Commit the transaction if all operations were successful
            con.commit();
        } catch (SQLException e) {
            // Rollback any changes made during the transaction on the same connection
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    // Log the exception or handle the rollback failure
                    ex.printStackTrace();
                }
            }
            // Rethrow the original SQLException
            throw e;
        } finally {
            // Attempt to reset the connection's auto-commit mode to true if it's not null
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException ex) {
                    // Handle or log this exception
                    ex.printStackTrace();
                }
            }
            // Since we didn't use try-with-resources for the connection (to access it in the catch block),
            // we need to ensure it's closed here
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    // Handle or log this exception
                    ex.printStackTrace();
                }
            }
        }
    }

    public List<Event> getEventsByCoordinator(Coordinator coordinator) throws SQLException {
        List<Event> events = new ArrayList<>();
        try (Connection con = connectionManager.getConnection()) {
            String sql = "SELECT e.* FROM Event e INNER JOIN EventCoordinator ec ON e.id = ec.eventid WHERE ec.coordinatorid = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, coordinator.getId());

            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String location = resultSet.getString("location");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                float time = resultSet.getFloat("time");
                String note = resultSet.getString("note");
                byte[] imageData = resultSet.getBytes("imageData");

                Event event = new Event(id, name, location, date, time, note, imageData);
                events.add(event);
            }
        }
        return events;
    }
}

package BLL;

import BE.Coordinator;
import BE.Customer;
import BE.Event;
import DAL.EventDao;

import java.sql.SQLException;
import java.util.List;

public class EventLogic {
    EventDao eventDAO = new EventDao();
    // Method to retrieve all events from the database

    public List<Event> getAllEvents() throws SQLException {
        // Calls the DAO method to fetch all events stored in the database
        return eventDAO.getAllEvents();
    }
    // Method to create a new event in the database
    public Event createEvent(Event event) throws SQLException {
        // Calls the DAO method to insert a new event into the database and return the newly created event.
        return eventDAO.createEvent(event);
    }
    // Method to assign a Coordinator to an event
    public void assignEventCoordinator(Event event, Coordinator coordinator) throws SQLException{
        // Calls the DAO method to link an event with a coordinator in the database
        eventDAO.assignEventCoordinator(event, coordinator);
    }
    // Method for a customer to join an event
    public void joinEvent(Event event, Customer customer) throws SQLException{
        // Calls the DAO method to record a customer's participation in an event

        eventDAO.joinEvent(event, customer);
    }
// Method to delete an event from the database

    public void deleteEvent(Event event) throws SQLException{
        // Calls the DAO method to remove an event from the database
        eventDAO.deleteEvent(event);
    }
    // Method to retrieve all events managed by a specific coordinator
    public List<Event> getEventsByCoordinator(Coordinator coordinator) throws SQLException {
        // Calls the DAO method to fetch all events that are managed by a particular coordinator

        return eventDAO.getEventsByCoordinator(coordinator);
    }
}

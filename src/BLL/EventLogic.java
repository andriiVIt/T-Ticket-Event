package BLL;

import BE.Coordinator;
import BE.Customer;
import BE.Event;
import DAL.EventDao;

import java.sql.SQLException;
import java.util.List;

public class EventLogic {
    EventDao eventDAO = new EventDao();

    public List<Event> getAllEvents() throws SQLException {
        return eventDAO.getAllEvents();
    }

    public Event createEvent(Event event) throws SQLException {
        return eventDAO.createEvent(event);
    }

    public void assignEventCoordinator(Event event, Coordinator coordinator) throws SQLException{
        eventDAO.assignEventCoordinator(event, coordinator);
    }

    public void joinEvent(Event event, Customer customer) throws SQLException{
        eventDAO.joinEvent(event, customer);
    }

    public void deleteEvent(Event event) throws SQLException{
        eventDAO.deleteEvent(event);
    }

    public List<Event> getEventsByCoordinator(Coordinator coordinator) throws SQLException {
        return eventDAO.getEventsByCoordinator(coordinator);
    }
}

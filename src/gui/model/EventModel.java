package gui.model;

import BE.Coordinator;
import BE.Customer;
import BE.Event;
import BLL.EventLogic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class EventModel {EventLogic eventLogic = new EventLogic();

    private ObservableList<Event> events = FXCollections.observableArrayList();
    private ObservableList<Event> eventCoordinator = FXCollections.observableArrayList();
    private ObservableList<Coordinator> coordinators = FXCollections.observableArrayList();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();

    public ObservableList<Event> getEvents() throws SQLException {
        events.addAll(eventLogic.getAllEvents());
        return events;
    }

    public Event createEvent(Event event) throws SQLException {
        Event e = eventLogic.createEvent(event);
        events.add(e);
        return e;
    }

    public void assignEventCoordinator(Event event, Coordinator coordinator) throws SQLException{
        eventLogic.assignEventCoordinator(event, coordinator);
        coordinators.add(coordinator);
    }

    public void joinEvent(Event event, Customer customer) throws SQLException{
        eventLogic.joinEvent(event, customer);
        customers.add(customer);
    }

    public void deleteEvent(Event event) throws SQLException {
        eventLogic.deleteEvent(event);
        events.remove(event);
    }

    public ObservableList<Event> getEventsByCoordinator(Coordinator coordinator) throws SQLException {
        eventCoordinator.addAll(eventLogic.getEventsByCoordinator(coordinator));
        return eventCoordinator;
    }
}

package gui.controller;

import BE.Customer;
import BE.Event;
import BE.Ticket;
import gui.model.CustomerModel;
import gui.model.EventModel;
import gui.model.TicketModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.UUID;

public class CreateCustomerController implements Initializable {

    // instance variables with @FXML
    @FXML
    public AnchorPane createCustomerAnchorPane;
    @FXML
    private TextField nameField, emailField;

    // instance variables
    private CustomerModel customerModel;
    private EventModel eventModel;
    private TicketModel ticketModel;
    private Event event;

    public void setModel(CustomerModel customerModel, EventModel eventModel, TicketModel ticketModel) {
        this.customerModel = customerModel;
        this.eventModel = eventModel;
        this.ticketModel = ticketModel;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Create participant - creates participant with a ticket
     */
    public void createParticipant() throws SQLException {
        String name = nameField.getText();
        String email = emailField.getText();

        // Creating customer
        Customer customer = new Customer(name, email);
        customerModel.createCustomer(customer);
        eventModel.joinEvent(event, customer);
        customerModel.fetchAllCustomers(event);

        // Creating ticket for the customer
        Ticket ticket = new Ticket(UUID.randomUUID(), "Event Ticket", event, customer);
        ticketModel.createTicket(ticket);

        Stage stage = (Stage) createCustomerAnchorPane.getScene().getWindow();
        stage.close();
    }

    /**
     * Cancel method - close the current window
     */
    public void cancel() {
        Stage stage = (Stage) createCustomerAnchorPane.getScene().getWindow();
        stage.close();
    }

    /**
     * Initialize method
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}

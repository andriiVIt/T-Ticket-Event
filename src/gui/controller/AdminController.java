package gui.controller;

import BE.Event;
import gui.model.CoordinatorModel;
import gui.model.CustomerModel;
import gui.model.EventModel;
import gui.util.BlurEffectUtil;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public MFXButton logOutButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane gridPane;

    private int currentPage, totalPages;

    // Method to handle creating a new event
    public void createEvent() {
        BlurEffectUtil.applyBlurEffect(scrollPane, 10); // Apply a blur effect to the scroll pane

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/view/CreateEvent.fxml"));
            Parent createEventParent = fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Set the window modality
            stage.setTitle("Create Event");
            stage.setResizable(false); // Make the window not resizable
            stage.setScene(new Scene(createEventParent));

            CreateEventController createEventController = fxmlLoader.getController();
            createEventController.setEventModel(new EventModel());
            createEventController.setCoordinatorModel(new CoordinatorModel());
            createEventController.setRefreshCallback(this::refreshEventCards);
            createEventController.setScrollPane(scrollPane);
            createEventController.setOnCloseRequestHandler(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Proper error handling should be implemented
        }
    }

    // Method to handle creating a new coordinator
    public void createCoordinator() {
        BlurEffectUtil.applyBlurEffect(scrollPane, 10); // Apply blur effect to the scroll pane

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/view/CreateCoordinator.fxml"));
            Parent createEventParent = fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Set window modality
            stage.setResizable(false); // Window is not resizable
            stage.setTitle("Create Coordinator");
            stage.setScene(new Scene(createEventParent));

            CreateCoordinatorController createCoordinatorController = fxmlLoader.getController();
            createCoordinatorController.setModel(new CoordinatorModel());
            createCoordinatorController.setScrollPane(scrollPane);
            createCoordinatorController.setOnCloseRequestHandler(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Proper error handling should be implemented
        }
    }

    // Method to refresh the display of event cards
    public void refreshEventCards() {
        try {
            gridPane.getChildren().clear(); // Clear existing content
            populateGridPane();
        } catch (IOException e) {
            // Better error handling should be considered
        }
    }

    // Populate the grid pane with event cards
    private void populateGridPane() throws IOException {
        EventModel eventModel = new EventModel();
        List<Event> events;
        try {
            events = eventModel.getEvents();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch events from the database.", e);
        }

        int numRows = 4;
        int numColumns = 2;
        int eventsPerPage = numRows * numColumns;
        totalPages = (int) Math.ceil((double) events.size() / eventsPerPage);

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                int eventIndex = currentPage * eventsPerPage + row * numColumns + col;
                if (eventIndex >= events.size()) {
                    break;
                }
                Pane pane = new Pane();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/view/EventCard.fxml"));

                fxmlLoader.setControllerFactory(clazz -> {
                    EventCardController controller = new EventCardController(events.get(eventIndex), scrollPane, new CustomerModel(), eventModel, this);
                    controller.setOnDeleteEventCallback(deletedEvent -> refreshEventCards());
                    return controller;
                });
                Pane contentPane = fxmlLoader.load();
                pane.getChildren().add(contentPane);
                gridPane.add(pane, col, row);
            }
        }
    }

    // Navigate to the previous page of events
    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            refreshEventCards();
        }
    }

    // Navigate to the next page of events
    public void nextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            refreshEventCards();
        }
    }

    // Initialize the controller and populate the grid pane with event cards
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentPage = 1;  // Initialization correction, assuming paging starts at index 0
        try {
            populateGridPane();
        } catch (IOException e) {
            // Better error handling should be considered
        }
    }

    // Handle logout functionality
    public void clickLogOut(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
            ((Stage) logOutButton.getScene().getWindow()).close(); // Close the current window

        } catch (IOException e) {
            throw new RuntimeException(e);  // Consider better error handling
        }
    }
}


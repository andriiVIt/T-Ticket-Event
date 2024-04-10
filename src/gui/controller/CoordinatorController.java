package gui.controller;

import BE.Coordinator;
import BE.Event;
import gui.model.CoordinatorModel;
import gui.model.CustomerModel;
import gui.model.EventModel;
import gui.util.BlurEffectUtil;
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

public class CoordinatorController implements Initializable {
    // instance variables with @FXML
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane eventPane;

    // instance variables
    private Coordinator coordinator;
    private int currentPage, totalPages;

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
        try {
            populateGridPane(coordinator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    /**
     * Create Coordinator method - opens the create_coordinator_window.fxml
     */
    public void createCoordinator(){
        BlurEffectUtil.applyBlurEffect(scrollPane, 10);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/view/CreateCoordinator.fxml"));
            Parent createEventParent = fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Create Coordinator");
            stage.setScene(new Scene(createEventParent));

            CreateCoordinatorController createCoordinatorWindowController = fxmlLoader.getController();
            createCoordinatorWindowController.setModel(new CoordinatorModel());
            createCoordinatorWindowController.setScrollPane(scrollPane);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresh Event Cards method - refresh events
     */
    public void refreshEventCards() {
        try {
            eventPane.getChildren().clear();
            populateGridPane(coordinator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Populate Grid Pane method - spawn events into the gird pane
     */
    private void populateGridPane(Coordinator coordinator) throws IOException {
        EventModel eventModel = new EventModel();
        List<Event> events;
        try {
            if (coordinator != null) {
                events = eventModel.getEventsByCoordinator(coordinator);
            } else {
                // Handle the case when the coordinator is not found
                System.out.println("Coordinator not found");
                return;
            }
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
                    EventCardController controller = new EventCardController(events.get(eventIndex), scrollPane, new CustomerModel(), eventModel);
                    controller.setOnDeleteEventCallback(deletedEvent -> refreshEventCards());
                    return controller;
                });
                Pane contentPane = fxmlLoader.load();
                pane.getChildren().add(contentPane);
                eventPane.add(pane, col, row);
            }
        }
    }

    /**
     * Previous Page method - go to the previous page
     */
    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            refreshEventCards();
        }
    }

    /**
     * Next Page method - go to the next page
     */
    public void nextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            refreshEventCards();
        }
    }

    /**
     * Initialize method
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentPage = 1;
        try {
            populateGridPane(coordinator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
}
}

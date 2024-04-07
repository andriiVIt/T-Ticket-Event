package gui.controller;

import BE.Event;
import gui.model.CoordinatorModel;
import gui.model.CustomerModel;
import gui.model.EventModel;
import gui.util.BlurEffectUtil;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane gridPane;

    private int currentPage,totalPages;


    public void createEvent() {
        BlurEffectUtil.applyBlurEffect(scrollPane, 10);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/view/CreateEvent.fxml"));
            Parent createEventParent = fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Create Event");
            stage.setResizable(false);
            Scene scene = new Scene(createEventParent);
            stage.setScene(scene);

            CreateEventController createEventController = fxmlLoader.getController();
            createEventController.setEventModel(new EventModel());
            createEventController.setCoordinatorModel(new CoordinatorModel());
            createEventController.setRefreshCallback(this::refreshEventCards);
            createEventController.setScrollPane(scrollPane);
            createEventController.setOnCloseRequestHandler(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void createCoordinator() {
        BlurEffectUtil.applyBlurEffect(scrollPane, 10);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/view/CreateCoordinator.fxml"));
            Parent createEventParent = fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Create Coordinator");
            stage.setScene(new Scene(createEventParent));

            CreateCoordinatorController createCoordinatorController = fxmlLoader.getController();
            createCoordinatorController.setModel(new CoordinatorModel());
            createCoordinatorController.setScrollPane(scrollPane);
            createCoordinatorController.setOnCloseRequestHandler(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void refreshEventCards() {
        try {
            gridPane.getChildren().clear();
            populateGridPane();
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
    }





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

    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            refreshEventCards();
        }
    }


    public void nextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            refreshEventCards();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentPage = 0;
        try {
            populateGridPane();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
    }
    }


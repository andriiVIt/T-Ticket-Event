package gui.controller;

import BE.Event;
import gui.model.CustomerModel;
import gui.model.EventModel;
import gui.model.TicketModel;
import gui.util.BlurEffectUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class EventCardController implements Initializable {

    @FXML
    private ImageView eventImage;
    @FXML
    private Label participants, timeNumber, eventTitle;
    @FXML
    private Button deleteButton;

    private final ScrollPane scrollPane;
    private final Event event;
    private final EventModel eventModel;
    private final CustomerModel customerModel;
    private AdminController adminController;
    private Consumer<Event> onDeleteEventCallback;

    public EventCardController(Event event, ScrollPane scrollPane, CustomerModel customerModel, EventModel eventModel, AdminController adminController) {
        this.event = event;
        this.scrollPane = scrollPane;
        this.customerModel = customerModel;
        this.eventModel = eventModel;
        this.adminController = adminController;

        try {customerModel.fetchAllCustomers(event);} catch (SQLException e) {throw new RuntimeException(e);}
    }
    public EventCardController(Event event, ScrollPane scrollPane, CustomerModel customerModel, EventModel eventModel) {
        this.event = event;
        this.scrollPane = scrollPane;
        this.customerModel = customerModel;
        this.eventModel = eventModel;

        try {customerModel.fetchAllCustomers(event);} catch (SQLException e) {throw new RuntimeException(e);}
    }
    public void deleteEvent(ActionEvent actionEvent) {
        try {
            eventModel.deleteEvent(event);
            adminController.refreshEventCards();
        } catch (SQLException e) {
           throw new RuntimeException(e);
        }
    }



    public void setOnDeleteEventCallback(Consumer<Event> onDeleteEventCallback) {
        this.onDeleteEventCallback = onDeleteEventCallback;
    }
    private Duration calculateTimeRemaining(LocalDate eventDate) {
        double eventTimeDouble = event.getTime();
        int hours = (int) eventTimeDouble;
        int minutes = (int) ((eventTimeDouble - hours) * 60);
        LocalTime eventLocalTime = LocalTime.of(hours, minutes);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventDateTime = LocalDateTime.of(eventDate, eventLocalTime);
        return Duration.between(now, eventDateTime);
    }
    public void viewEvent(ActionEvent actionEvent) {
        BlurEffectUtil.applyBlurEffect(scrollPane, 10);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/view/EventInfo.fxml"));
            Parent createEventParent = fxmlLoader.load();

            EventInfoController eventInfoController = fxmlLoader.getController();
            eventInfoController.setModel(new EventModel(), new CustomerModel(), new TicketModel(), scrollPane);
            eventInfoController.setEvent(event);
            eventInfoController.setOnDeleteEventCallback(deletedEvent -> {
                if (onDeleteEventCallback != null) {
                    onDeleteEventCallback.accept(deletedEvent);
                }
            });

            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle(event.getName());
            Scene scene = new Scene(createEventParent);
            scene.setFill(Color.TRANSPARENT);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventTitle.setText(event.getName());

        // setting the image
        ByteArrayInputStream inputStream = new ByteArrayInputStream(event.getImageData());
        Image image = new Image(inputStream);
        eventImage.setImage(image);
        eventImage.setFitWidth(120);
        eventImage.setFitHeight(120);
        eventImage.setPreserveRatio(false);

        Duration duration = calculateTimeRemaining(event.getDate());
        if (duration.toHours() < 0) {
            timeNumber.setText("passed");
            deleteButton.setVisible(true);
        } else if (duration.toDays() >= 1) {
            timeNumber.setText(duration.toDays() + " d");
        } else {
            timeNumber.setText(duration.toHours() + " h");
        }
        participants.setText(String.valueOf(customerModel.getCustomers().size()));
    }
    }


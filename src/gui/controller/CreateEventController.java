package gui.controller;

import BE.Coordinator;
import BE.Event;
import gui.model.CoordinatorModel;
import gui.model.EventModel;
import gui.util.BlurEffectUtil;
import gui.util.Message;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CreateEventController implements Initializable {

    @FXML
    private AnchorPane createEventAnchorPane;
    @FXML
    private MFXTextField eventNameField, eventLocationField;
    @FXML
    private MFXDatePicker eventDateField;
    @FXML
    private Spinner<Double> eventTimeField;
    @FXML
    private CheckComboBox<Coordinator> eventCoordinatorsField;

    // instance variables
    private ScrollPane scrollPane;
    private EventModel eventModel;
    private Runnable refreshCallback;
    private byte[] imageData;

    public void setCoordinatorModel(CoordinatorModel coordinatorModel) {
        eventCoordinatorsField.setTitle("Coordinators");
        eventCoordinatorsField.getItems().addAll(coordinatorModel.getCoordinators());

        coordinatorModel.getCoordinators().addListener((ListChangeListener<? super Coordinator>) obs->{
            eventCoordinatorsField.getItems().clear();
            eventCoordinatorsField.getItems().addAll(coordinatorModel.getCoordinators());
        });
    }

    public void setEventModel(EventModel eventModel) {
        this.eventModel = eventModel;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public void setRefreshCallback(Runnable refreshCallback) {this.refreshCallback = refreshCallback;}

    public void setOnCloseRequestHandler(Stage stage) {
        stage.setOnCloseRequest(event -> BlurEffectUtil.removeBlurEffect(scrollPane));
    }

    /**
     * Create Event method - creates event
     */
    public void createEvent() {
        String name = eventNameField.getText();
        String location = eventLocationField.getText();
        LocalDate date = eventDateField.getValue();
        double time = eventTimeField.getValue();
        String note = " ";

        if (name.isEmpty() || location.isEmpty() || date == null) {
            Message.showAlert("Error", "Please fill in all the fields", Alert.AlertType.WARNING);
        } else {
            TextArea textArea = new TextArea();
            textArea.setPromptText("Write a note here...");
            textArea.setWrapText(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add Note");
            alert.setHeaderText("Write a note");
            alert.getDialogPane().setContent(textArea);

            ButtonType skipButtonType = new ButtonType("Skip", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType writeNoteButtonType = new ButtonType("Write a Note", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(writeNoteButtonType, skipButtonType);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == writeNoteButtonType) {
                note = textArea.getText();
            }
            try {
                scrollPane.setEffect(null);
                Event event = new Event(name, location, date, time, note, imageData);
                eventModel.createEvent(event);

                // assign coordinators to the event
                List<Coordinator> selectedItems = eventCoordinatorsField.getCheckModel().getCheckedItems();
                for (Coordinator item : selectedItems) {
                    eventModel.assignEventCoordinator(event, new Coordinator(item.getId(), item.getUsername(), item.getPassword()));
                }

                // Call the refreshCallback
                if (refreshCallback != null) {
                    refreshCallback.run();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) createEventAnchorPane.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Cancel method - close the current window
     */
    public void cancel(){
        Stage stage = (Stage) createEventAnchorPane.getScene().getWindow();
        BlurEffectUtil.removeBlurEffect(scrollPane);
        stage.close();
    }

    /**
     * Get Event Image method - gets the image from file chooser
     */
    public void getEventImage() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Images File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images Files", "*.png", "*.jpg", "*.jpeg","*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imageData = readBytesFromFile(selectedFile);
        }
    }

    /**
     * Read Bytes From File - reads the bytes from selected file
     */
    private static byte[] readBytesFromFile(File file) throws Exception {
        InputStream is = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        is.close();
        bos.close();
        return bos.toByteArray();
    }

    /**
     * Initialize Time Spinner method
     */
    private StringConverter<Double> initializeTimeSpinner(){
        CustomSpinnerValueFactory valueFactory = new CustomSpinnerValueFactory(0, 23.30, 12.30);
        eventTimeField.setValueFactory(valueFactory);

        StringConverter<Double> converter = new StringConverter<>() {
            @Override
            public String toString(Double value) {
                if (value == null) {
                    return "";
                }
                int hours = (int) Math.floor(value);
                int minutes = (int) ((value - hours) * 100);
                return String.format("%02d.%02d", hours, minutes);
            }

            @Override
            public Double fromString(String string) {
                try {
                    if (string == null) {
                        return null;
                    }
                    string = string.trim();
                    if (string.length() < 1) {
                        return null;
                    }
                    String[] parts = string.split("\\.");
                    int hours = Integer.parseInt(parts[0]);
                    int minutes = Integer.parseInt(parts[1]);
                    return (double) hours + (double) minutes / 100;
                } catch (NumberFormatException ex) {
                    return null;
                }
            }
        };
        eventTimeField.getEditor().setTextFormatter(new TextFormatter<>(converter, 12.30));
        return converter;
    }

    public static class CustomSpinnerValueFactory extends SpinnerValueFactory<Double> {
        private final double min;
        private final double max;

        public CustomSpinnerValueFactory(double min, double max, double initialValue) {
            this.min = min;
            this.max = max;
            setValue(initialValue);

            setConverter(new StringConverter<>() {
                @Override
                public String toString(Double value) {
                    return String.format("%.2f", value);
                }

                @Override
                public Double fromString(String string) {
                    return Double.parseDouble(string);
                }
            });
        }

        @Override
        public void increment(int steps) {
            double currentValue = getValue();
            int hours = (int) currentValue;
            int minutes = (int) Math.round((currentValue - hours) * 100);

            minutes += steps * 30;

            if (minutes >= 60) {
                hours++;
                minutes -= 60;
            } else if (minutes < 0) {
                hours--;
                minutes += 60;
            }

            double newValue = hours + (double) minutes / 100;

            // Clamp the value between min and max
            if (newValue >= max) {
                newValue = max;
            } else if (newValue <= min) {
                newValue = min;
            }

            setValue(newValue);
        }

        @Override
        public void decrement(int steps) {
            increment(-steps);
        }
    }

    /**
     * Initialize method
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StringConverter<Double> converter = initializeTimeSpinner();
        eventTimeField.valueProperty().addListener((obs, oldValue, newValue) -> {
            String newText = converter.toString(newValue);
            eventTimeField.getEditor().setText(newText);
        });
    }
}

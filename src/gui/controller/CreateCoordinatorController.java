package gui.controller;

import BE.Coordinator;
import gui.model.CoordinatorModel;
import gui.util.BlurEffectUtil;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateCoordinatorController implements Initializable {
    // instance variables with @FXML
    @FXML
    private AnchorPane createCoordinatorAnchorPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField coordinatorNameField, passwordField;

    // instance variables
    private CoordinatorModel model;

    public void setModel(CoordinatorModel model){
        this.model = model;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public void setOnCloseRequestHandler(Stage stage) {
        stage.setOnCloseRequest(event -> BlurEffectUtil.removeBlurEffect(scrollPane));
    }

    /**
     * Create Coordinator method - create coordinator
     */
    public void createCoordinator() {
        String username = coordinatorNameField.getText();
        String password = passwordField.getText();

        try {
            Coordinator coordinator = new Coordinator(username, password);
            model.createCoordinator(coordinator);
            scrollPane.setEffect(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        BlurEffectUtil.removeBlurEffect(scrollPane);
        Stage stage = (Stage) createCoordinatorAnchorPane.getScene().getWindow();
        stage.close();
    }

    /**
     * Cancel method - close the current window
     */
    public void cancel() {
        scrollPane.setEffect(null);
        Stage stage = (Stage) createCoordinatorAnchorPane.getScene().getWindow();
        stage.close();
    }

    /**
     * Initialize method
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}

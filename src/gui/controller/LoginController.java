package gui.controller;

import BE.Coordinator;
import gui.model.AdminModel;
import gui.model.CoordinatorModel;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;



public class LoginController implements Initializable {
    @FXML
    private VBox loginVbox;
    @FXML
    private MFXTextField  userNameField;
    @FXML
    private MFXPasswordField passwordField;

    private AdminModel  adminModel;
    private CoordinatorModel coordinatorModel;



    public void logIn(ActionEvent actionEvent) {
        String inputUsername = userNameField.getText();
        String inputPassword = passwordField.getText();


        if(adminModel.isValidAdmin(inputUsername,inputPassword)){
           try {
               FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/view/adminWindow.fxml"));
               Parent root = fxmlLoader.load();
               Stage stage = new Stage();
               stage.setResizable(false);
               stage.setScene(new Scene(root));
               stage.setTitle("Admin Window");
               stage.show();

               Stage currentStage = (Stage) loginVbox.getScene().getWindow();
               currentStage.close();

           }catch (IOException e){
               e.printStackTrace();
           }
        }
        else if(coordinatorModel.isValidCoordinator(inputUsername,inputPassword)){
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/view/coordinatorWindow.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.setScene(new Scene(root));
                stage.setTitle("Coordinator Window");
                stage.show();

                Coordinator loggedInCoordinator = coordinatorModel.getCoordinatorByUsername(inputUsername);
                CoordinatorController controller = fxmlLoader.getController();
                controller.setCoordinator(loggedInCoordinator);

                Stage currentStage = (Stage) loginVbox.getScene().getWindow();
                currentStage.close();
            }catch (IOException | SQLException e) {
                e.printStackTrace();
            }

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Credentials");
            alert.setContentText("Username or password is incorrect");
            alert.setHeaderText(null);
            alert.showAndWait();

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        adminModel = new AdminModel();
        coordinatorModel = new CoordinatorModel();
    }
}

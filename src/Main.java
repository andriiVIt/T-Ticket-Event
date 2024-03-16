import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

import static javafx.application.Application.launch;

public class Main extends Application {
    public static void main(String[] args) {launch(args);

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlUrl = getClass().getResource("gui/view/Login.fxml");
        Parent root = FXMLLoader.load(Objects.requireNonNull(fxmlUrl));
        Scene scene = new Scene(root);
        primaryStage.setTitle("T-Ticket Event");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
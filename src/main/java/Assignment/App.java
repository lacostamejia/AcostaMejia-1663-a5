package Assignment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            //Launching the main controller with the window
            FXMLLoader mainLoad = new FXMLLoader(getClass().getResource("Inventory.fxml"));
            Parent mainRoot = (Parent) mainLoad.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(mainRoot));
            stage.setTitle("Inventory");
            stage.show();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

package task4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class task4 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("task4FXML.fxml"));
        Parent root = fxmlLoader.load();
        task4Controller controller = fxmlLoader.getController();

        Scene scene = new Scene(root, 580, 530);
        stage.setResizable(false);
        stage.setTitle("Editor");
        stage.setScene(scene);
        stage.requestFocus();
        stage.show();

    }
}


package task6;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class task6 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("task6FXML.fxml"));
        Parent root = fxmlLoader.load();
        task6Controller con = fxmlLoader.getController();


        Scene scene = new Scene(root, 550, 530);

        scene.setFill(Color.WHITE);

        con.initMouseControl(con.group, con.subScene, stage);
        stage.setResizable(false);
        stage.setTitle("Editor");
        stage.setScene(scene);
        stage.requestFocus();
        stage.show();

    }
}

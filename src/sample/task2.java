package sample;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class task2 extends Application {

    double width = 40;
    double height = 40;

    @Override
    public void start(Stage stage) throws Exception{

        Pane pane = new Pane();
        Ellipse ellipse = new Ellipse();
        Rectangle rect = new Rectangle();
        StackPane stackPane = new StackPane(rect,ellipse);
        ellipse.setRadiusX( width);
        ellipse.setRadiusY(height);
        rect.setHeight(height*2);
        rect.setWidth(width*2);

        rect.setStyle("-fx-fill: white; " +
                "-fx-stroke: rgba(150,150,150,0.5); " +
                "-fx-stroke-width: 3; " +
                "-fx-stroke-type: outside; -fx-stroke-dash-array: 1 10;");


        pane.getChildren().add(stackPane);



        stage.setTitle("Create Image!");
        Scene scene = new Scene(pane, 300, 275);
        scene.setOnKeyPressed(keyEvent -> {
            double ch = 5;
            if(keyEvent.getCode() == KeyCode.DOWN && stackPane.getLayoutY() < scene.getHeight() - height*2 - 6){
                stackPane.setLayoutY(stackPane.getLayoutY() + ch);
            }
            if(keyEvent.getCode() == KeyCode.UP && stackPane.getLayoutY() > 0){
                stackPane.setLayoutY(stackPane.getLayoutY() - ch);
            }
            if(keyEvent.getCode() == KeyCode.LEFT && stackPane.getLayoutX() > 0){
                stackPane.setLayoutX(stackPane.getLayoutX() - ch);
            }
            if(keyEvent.getCode() == KeyCode.RIGHT && stackPane.getLayoutX() < scene.getWidth() - width*2 - 6){
                stackPane.setLayoutX(stackPane.getLayoutX() + ch);
            }
            if(keyEvent.getCode() == KeyCode.COMMA && width > ch){
                width -= ch;
                ellipse.setRadiusX(width);
                rect.setWidth(2*width);
            }
            if(keyEvent.getCode() == KeyCode.PERIOD && stackPane.getLayoutX() + width*2 + ch*2 < scene.getWidth()){
                width += ch;
                ellipse.setRadiusX(width);
                rect.setWidth(2*width);
            }
            if(keyEvent.getCode() == KeyCode.MINUS && height > ch){
                height -= ch;
                ellipse.setRadiusY(height);
                rect.setHeight(2*height);
            }
            if(keyEvent.getCode() == KeyCode.EQUALS && stackPane.getLayoutY() + height*2 + ch*2 < scene.getHeight()){
                height += ch;
                ellipse.setRadiusY(height);
                rect.setHeight(2*height);
            }
        });
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

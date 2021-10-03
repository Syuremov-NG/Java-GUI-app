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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {

    String curSrcImg = "";
    Image curImg;


    ToggleButton createBtnWithImage(String src, int width, int height) throws FileNotFoundException {
        FileInputStream input = new FileInputStream(src);
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return new ToggleButton("", imageView);
    }

    @Override
    public void start(Stage stage) throws Exception{

        Image[] src = {
                new Image(new FileInputStream("src/img/icons8-black-circle-96.png")),
                new Image(new FileInputStream("src/img/icons8-blue-circle-96.png")),
                new Image(new FileInputStream("src/img/icons8-green-circle-96.png")),
                new Image(new FileInputStream("src/img/icons8-red-circle-96.png")),
        };
        String[] srcs = {
                "src/img/icons8-black-circle-96.png",
                "src/img/icons8-blue-circle-96.png",
                "src/img/icons8-green-circle-96.png",
                "src/img/icons8-red-circle-96.png"
        };

        BorderPane borderPane = new BorderPane();
        Pane pane2 = new Pane();
        Pane pane = new Pane(pane2);
        pane.setStyle("-fx-background-color: white");

        int btnWidth = 30;
        int btnHeight = 30;
        ToggleGroup btnGroup = new ToggleGroup();
        ToggleButton btn1 = createBtnWithImage(srcs[0], btnWidth, btnHeight);
        ToggleButton btn2 = createBtnWithImage(srcs[1], btnWidth, btnHeight);
        ToggleButton btn3 = createBtnWithImage(srcs[2], btnWidth, btnHeight);
        ToggleButton btn4 = createBtnWithImage(srcs[3], btnWidth, btnHeight);

        btn1.setToggleGroup(btnGroup);
        btn2.setToggleGroup(btnGroup);
        btn3.setToggleGroup(btnGroup);
        btn4.setToggleGroup(btnGroup);

        Button saveBtn = new Button("Сохранить");
        Button clearBtn = new Button("Очистить");

        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                curSrcImg = srcs[0];
            }
        });
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                curSrcImg = srcs[1];
            }
        });
        btn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                curSrcImg = srcs[2];
            }
        });
        btn4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                curSrcImg = srcs[3];
            }
        });
        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try
                {
                    WritableImage snapshot = pane.snapshot(new SnapshotParameters(), null);
                    File file = new File("test.png");
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(snapshot, null);
                    ImageIO.write(renderedImage, "png", file);
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        clearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pane.getChildren().clear();
            }
        });

        pane.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();

                System.out.println("clickX " + x + " clickY " + y);
                if(btnGroup.getSelectedToggle() != null && x > btnWidth/2 && y < pane.getHeight() - btnHeight/2) {
                    try {
                        Image img = new Image(new FileInputStream(curSrcImg));
                        ImageView imgView = new ImageView(img);
                        imgView.setFitHeight(btnHeight);
                        imgView.setFitWidth(btnWidth);
                        imgView.setLayoutX((x - imgView.getFitWidth() / 2));
                        imgView.setLayoutY((y - imgView.getFitHeight() / 2));
                        pane.getChildren().add(imgView);
                    } catch (FileNotFoundException e) {
                        System.out.println("Цвет не выбран!");
                    }
                }
            }
        });
        VBox vBox = new VBox(10, btn1, btn2, btn3, btn4);
        vBox.maxWidth(20);

        HBox hBox = new HBox(saveBtn, clearBtn);
        borderPane.setLeft(vBox);
        borderPane.setCenter(pane);
        borderPane.setBottom(hBox);
        stage.setTitle("Create Image!");
        stage.setScene(new Scene(borderPane, 300, 275));
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

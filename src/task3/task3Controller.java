package task3;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

public class task3Controller {

    ArrayList<Shape> arr = new ArrayList<>();

    private Rectangle curRect;
    Shape curShape = null;

    static void clipChildren(Region region) {

        final Rectangle outputClip = new Rectangle();
        region.setClip(outputClip);

        region.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            outputClip.setWidth(newValue.getWidth());
            outputClip.setHeight(newValue.getHeight());
        });
    }


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem saveBtn;

    @FXML
    private MenuItem menuClear;

    @FXML
    private ToggleGroup toggleShapes;

    @FXML
    private ToggleButton tglBtn1;

    @FXML
    private ToggleButton tglBtn2;

    @FXML
    private ToggleButton tglBtn3;

    @FXML
    private ToggleButton tglBtn4;

    @FXML
    private Pane pane;

    @FXML
    private ColorPicker borderColor;

    @FXML
    private ColorPicker fillColor;

    @FXML
    private TextField strokeWidth;

    @FXML
    private ChoiceBox<String> strokeType;

    @FXML
    private TextField widthImg;

    @FXML
    private TextField heightImg;

    @FXML
    private void save(ActionEvent event){
        double curWidth = pane.getWidth();
        double curHeight = pane.getHeight();
        pane.setPrefWidth(Integer.parseInt(widthImg.getText()));
        pane.setPrefHeight(Integer.parseInt(heightImg.getText()));
        try
        {
            WritableImage snapshot = pane.snapshot(new SnapshotParameters(), null);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select directory for save");
            fileChooser.setInitialFileName("Image");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png", "*.png"));
            File file = fileChooser.showSaveDialog((Stage) pane.getScene().getWindow());
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(snapshot, null);
            ImageIO.write(renderedImage, "png", file);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        pane.setPrefSize(curWidth, curHeight);
    }

    @FXML
    private void clear(){
        pane.getChildren().clear();
        arr.clear();
    }

    @FXML
    private void exit(){
        System.exit(0);
    }

    @FXML
    private void click(MouseEvent event){
        double x = event.getX();
        double y = event.getY();
        double wd = 40;
        double hg = 30;
        curShape = null;
        for (Shape shape : arr) {
            if (event.getTarget() == shape) {
                curShape = shape;
                break;
            }
        }
        if(event.getButton() == MouseButton.SECONDARY && curShape != null ){
            pane.getChildren().remove(curRect);
            curRect = new Rectangle();
            if(event.getTarget() != pane){
                curRect.setWidth(curShape.getBoundsInLocal().getWidth() * curShape.getScaleX());
                curRect.setHeight(curShape.getBoundsInLocal().getHeight()  * curShape.getScaleY());
                curRect.setStyle(" -fx-fill: rgba(0,0,0,0); " +
                        "-fx-stroke: gray; " +
                        "-fx-stroke-width: 2; " +
                        "-fx-stroke-type: outside; -fx-stroke-dash-array: 0 10;");
                curRect.setLayoutX(curShape.getBoundsInParent().getCenterX() - curRect.getWidth()/2);
                curRect.setLayoutY(curShape.getBoundsInParent().getCenterY() - curRect.getHeight()/2);
                pane.getChildren().add(curRect);
            }
        }
        if(event.getButton() == MouseButton.PRIMARY){
            pane.getChildren().remove(curRect);
            curShape = null;
            if(toggleShapes.getSelectedToggle() == tglBtn1 && event.getTarget() == pane) {
                boolean ans = true;
                Line line = new Line();
                line.setStartX(x - wd/2);
                line.setStartY(y - wd/2);
                line.setEndX(x + wd/2);
                line.setEndY(y + wd/2);

                int strWidth = Integer.parseInt(strokeWidth.getText().replaceAll("\\D+", ""));
                line.setStrokeWidth(strWidth);
                line.setStroke(borderColor.getValue());
                if(strokeType.getValue() == "Dotted"){
                    line.setStyle("-fx-stroke-dash-array:" + strWidth + " " + strWidth*2 + ";");
                }
                arr.add(line);
                pane.getChildren().add(line);

                //Пересечение через intersects
            }
            if(toggleShapes.getSelectedToggle() == tglBtn3 && event.getTarget() == pane) {
                boolean ans = true;
                Circle circle = new Circle();
                circle.setRadius(wd/2);
                circle.setLayoutX(x);
                circle.setLayoutY(y);
                int strWidth = Integer.parseInt(strokeWidth.getText().replaceAll("\\D+", ""));
                circle.setStrokeWidth(strWidth);
                circle.setStroke(borderColor.getValue());
                circle.setFill(fillColor.getValue());
                if(strokeType.getValue() == "Dotted"){
                    circle.setStyle("-fx-stroke-dash-array:" + strWidth + " " + strWidth*2 + ";");
                }
                arr.add(circle);
                pane.getChildren().add(circle);
            }
            if(toggleShapes.getSelectedToggle() == tglBtn4 && event.getTarget() == pane) {
                boolean ans = true;
                Rectangle rect = new Rectangle();
                rect.setWidth(wd);
                rect.setHeight(hg);
                rect.setLayoutX(x-wd/2);
                rect.setLayoutY(y-hg/2);
                int strWidth = Integer.parseInt(strokeWidth.getText().replaceAll("\\D+", ""));
                rect.setStrokeWidth(strWidth);
                rect.setStroke(borderColor.getValue());
                rect.setFill(fillColor.getValue());
                if(strokeType.getValue() == "Dotted"){
                    rect.setStyle("-fx-stroke-dash-array:" + strWidth + " " + strWidth*2 + ";");
                }
                arr.add(rect);
                pane.getChildren().add(rect);
            }
            if(toggleShapes.getSelectedToggle() == tglBtn2 && event.getTarget() == pane) {
                boolean ans = true;
                Ellipse ellipse = new Ellipse();
                ellipse.setRadiusX(wd/2);
                ellipse.setRadiusY(hg/2);
                ellipse.setLayoutX(x);
                ellipse.setLayoutY(y);
                int strWidth = Integer.parseInt(strokeWidth.getText().replaceAll("\\D+", ""));
                ellipse.setStrokeWidth(strWidth);
                ellipse.setStroke(borderColor.getValue());
                ellipse.setFill(fillColor.getValue());
                if(strokeType.getValue() == "Dotted"){
                    ellipse.setStyle("-fx-stroke-dash-array:" + strWidth + " " + strWidth*2 + ";");
                }
                arr.add(ellipse);
                pane.getChildren().add(ellipse);
            }
        }



        pane.requestFocus();
    }

    public void move(KeyEvent keyEvent){
        double ch = 5;
        if(curShape != null){
            if(keyEvent.getCode() == KeyCode.DOWN){
                curShape.setLayoutY(curShape.getLayoutY() + ch);
                curRect.setLayoutY(curRect.getLayoutY() + ch);
            }
            if(keyEvent.getCode() == KeyCode.UP){
                curShape.setLayoutY(curShape.getLayoutY() - ch);
                curRect.setLayoutY(curRect.getLayoutY() - ch);
            }
            if(keyEvent.getCode() == KeyCode.LEFT){
                curShape.setLayoutX(curShape.getLayoutX() - ch);
                curRect.setLayoutX(curRect.getLayoutX() - ch);
            }
            if(keyEvent.getCode() == KeyCode.RIGHT){
                curShape.setLayoutX(curShape.getLayoutX() + ch);
                curRect.setLayoutX(curRect.getLayoutX() + ch);
            }
            if(keyEvent.getCode() == KeyCode.COMMA){
                curShape.setScaleX(curShape.getScaleX() - ch/100);
                double diff = curRect.getWidth() - curShape.getBoundsInLocal().getWidth() * curShape.getScaleX();
                curRect.setWidth(curShape.getBoundsInLocal().getWidth() * curShape.getScaleX());
                curRect.setLayoutX(curRect.getLayoutX() + diff/2);
            }
            if(keyEvent.getCode() == KeyCode.PERIOD){
                curShape.setScaleX(curShape.getScaleX() + ch/100);
                double diff = curRect.getWidth() - curShape.getBoundsInLocal().getWidth() * curShape.getScaleX();
                curRect.setWidth(curShape.getBoundsInLocal().getWidth() * curShape.getScaleX());
                curRect.setLayoutX(curRect.getLayoutX() + diff/2);
            }
            if(keyEvent.getCode() == KeyCode.MINUS){
                curShape.setScaleY(curShape.getScaleY() - ch/100);
                double diff = curRect.getHeight() - curShape.getBoundsInLocal().getHeight() * curShape.getScaleY();
                curRect.setHeight(curShape.getBoundsInLocal().getHeight() * curShape.getScaleY());
                curRect.setLayoutY(curRect.getLayoutY() + diff/2);
            }
            if(keyEvent.getCode() == KeyCode.EQUALS){
                curShape.setScaleY(curShape.getScaleY() + ch/100);
                double diff = curRect.getHeight() - curShape.getBoundsInLocal().getHeight() * curShape.getScaleY();
                curRect.setHeight(curShape.getBoundsInLocal().getHeight() * curShape.getScaleY());
                curRect.setLayoutY(curRect.getLayoutY() + diff/2);
            }
        }

    }
    @FXML
    void initialize() {
        strokeType.setValue("Solid");
        strokeType.setItems(FXCollections.observableArrayList(
            "Solid", "Dotted"
        ));
        strokeWidth.setFocusTraversable(false);
        strokeType.setFocusTraversable(false);
        borderColor.setFocusTraversable(false);
        tglBtn1.setFocusTraversable(false);
        tglBtn2.setFocusTraversable(false);
        tglBtn3.setFocusTraversable(false);
        tglBtn4.setFocusTraversable(false);
        fillColor.setFocusTraversable(false);
        widthImg.setFocusTraversable(false);
        heightImg.setFocusTraversable(false);
        clipChildren(pane);
    }

}

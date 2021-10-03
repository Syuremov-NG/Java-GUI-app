package task6;

import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

public class task6Controller {

    private double paneWidth = 550;
    private double paneHeight = 400;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ColorPicker O_colorPicker;

    @FXML
    private ColorPicker C_colorPicker;

    @FXML
    private ColorPicker H_colorPicker;

    @FXML
    private ColorPicker S_colorPicker;

    @FXML
    private Button saveBtn;

    @FXML
    public SubScene subScene;

    @FXML
    private Button openBtn;

    @FXML
    private Button showBtn;

    @FXML
    private void open(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Document");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("XYZ files (*.xyz)", "*.xyz");
        fileChooser.getExtensionFilters().add(extFilter);
        this.file = fileChooser.showOpenDialog((Stage) saveBtn.getScene().getWindow());
        if (file != null) {
            //Open
            System.out.println("Процесс открытия файла");
        }
    }

    @FXML
    private void save(ActionEvent event){
        try
        {
            WritableImage snapshot = subScene.snapshot(new SnapshotParameters(), null);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select directory for save");
            fileChooser.setInitialFileName("Image");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png", "*.png"));
            File file = fileChooser.showSaveDialog((Stage) subScene.getScene().getWindow());
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(snapshot, null);
            ImageIO.write(renderedImage, "png", file);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private ArrayList<Molecule> readFile(){
        ArrayList<Molecule> ret = new ArrayList<>();
        try{
            FileReader fileReader = new FileReader(this.file);
            BufferedReader bfReader = new BufferedReader(fileReader);
            String line = bfReader.readLine();
            count = Integer.parseInt(line);
            line = bfReader.readLine();
            for (int i = 0; i < count; i++){
                line = bfReader.readLine();
                String[] words = line.split(" ");
                switch (words[0]){
                    case "O":
                        ret.add(new Molecule(0, Double.parseDouble(words[1]), Double.parseDouble(words[2]), Double.parseDouble(words[3]), 25, Color.BLUE));
                        break;
                    case "C":
                        ret.add(new Molecule(1, Double.parseDouble(words[1]), Double.parseDouble(words[2]), Double.parseDouble(words[3]), 20, Color.GRAY));
                        break;
                    case "H":
                        ret.add(new Molecule(2, Double.parseDouble(words[1]), Double.parseDouble(words[2]), Double.parseDouble(words[3]), 30, Color.GREEN));
                        break;
                    case "S":
                        ret.add(new Molecule(3, Double.parseDouble(words[1]), Double.parseDouble(words[2]), Double.parseDouble(words[3]), 15, Color.YELLOW));
                        break;
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return ret;
    }

    public void initMouseControl(Group group, SubScene scene, Stage stage){
        Rotate xRotate;
        Rotate yRatate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRatate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRatate.angleProperty().bind(angleY);

        scene.setOnMousePressed(mouseEvent -> {
            anchorX = mouseEvent.getSceneX();
            anchorY = mouseEvent.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });
        scene.setOnMouseDragged(mouseEvent->{
            angleX.set(anchorAngleX - (anchorY - mouseEvent.getSceneY()));
            angleY.set(anchorAngleY + (anchorX - mouseEvent.getSceneX()));
        });
        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() + delta);
        });
    }

    public void setPos(Sphere sph, double x, double y, double z){
        sph.setTranslateX(x*100);
        sph.setTranslateY(y*100);
        sph.setTranslateZ(z*100);
    }

    public Cylinder paintCylinder(Point3D A, Point3D B){
        Point3D temp = A.subtract(B);
        double Y = temp.getX() != 0 || temp.getZ() != 0 ? B.getY() : Math.max(B.getY(), A.getY());
        Point3D dir = A.subtract(B).crossProduct(new Point3D(0, -1, 0));
        double angle = Math.acos(A.subtract(B).normalize().dotProduct(new Point3D(0, -1, 0)));
        double h1 = A.distance(B);
        Cylinder c = new Cylinder(5, h1);
        PhongMaterial mat = new PhongMaterial();
        mat.setDiffuseColor(Color.GRAY);
        c.setMaterial(mat);
        c.getTransforms().addAll(new Translate(B.getX(), Y - h1 / 2d, B.getZ()),
                new Rotate(-Math.toDegrees(angle), 0d, h1 / 2d, 0d, new Point3D(dir.getX(), -dir.getY(), dir.getZ())));
        return c;
    }

    public ArrayList<ArrayList<Integer>> getPairs(ArrayList<Sphere> arr){
        ArrayList<ArrayList<Integer>> ret = new ArrayList<>();
        for(int i = 0; i < arr.size(); i++){
            for(int j = i+1; j < arr.size(); j++){
                ret.add(new ArrayList<Integer>());
                ret.get(ret.size()-1).add(i);
                ret.get(ret.size()-1).add(j);
            }
        }
        return ret;
    }

    class Molecule{
        public double x, y, z, radius;
        public int type;
        public Color col;
        Molecule(int type, double x, double y, double z, double radius, Color col){
            this.x = x;
            this.y = y;
            this.z = z;
            this.radius = radius;
            this.type = type;
            this.col = col;
        }
    }

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private File file;
    private int count;
    private ArrayList<Molecule> arrMol = new ArrayList<>();
    private ArrayList<Sphere> arrSphere = new ArrayList<>();
    private ArrayList<Point3D> arrPoints = new ArrayList<>();
    public Group group = new Group();

    @FXML
    void initialize() {

        Camera camera = new PerspectiveCamera();
        subScene.setCamera(camera);

        group.setTranslateX(subScene.getWidth()/2);
        group.setTranslateY(subScene.getHeight()/2);

        O_colorPicker.setOnAction(event -> {
            if(arrSphere.size() != 0){
                for(int i = 0; i < arrMol.size(); i++){
                    if(arrMol.get(i).type == 0){
                        PhongMaterial mat = new PhongMaterial();
                        mat.setDiffuseColor(O_colorPicker.getValue());
                        arrSphere.get(i).setMaterial(mat);

                    }
                }
            }
        });
        C_colorPicker.setOnAction(event -> {
            if(arrSphere.size() != 0) {
                for (int i = 0; i < arrMol.size(); i++) {
                    if (arrMol.get(i).type == 1) {
                        PhongMaterial mat = new PhongMaterial();
                        mat.setDiffuseColor(C_colorPicker.getValue());
                        arrSphere.get(i).setMaterial(mat);

                    }
                }
            }
        });
        H_colorPicker.setOnAction(event -> {
            if(arrSphere.size() != 0) {
                for (int i = 0; i < arrMol.size(); i++) {
                    if (arrMol.get(i).type == 2) {
                        PhongMaterial mat = new PhongMaterial();
                        mat.setDiffuseColor(H_colorPicker.getValue());
                        arrSphere.get(i).setMaterial(mat);

                    }
                }
            }
        });
        S_colorPicker.setOnAction(event -> {
            if(arrSphere.size() != 0) {
                for (int i = 0; i < arrMol.size(); i++) {
                    if (arrMol.get(i).type == 3) {
                        PhongMaterial mat = new PhongMaterial();
                        mat.setDiffuseColor(S_colorPicker.getValue());
                        arrSphere.get(i).setMaterial(mat);

                    }
                }
            }
        });

        showBtn.setOnAction(event -> {
            group.getChildren().clear();
            arrPoints.clear();
            arrSphere.clear();
            arrMol.clear();
            if(file != null){
                arrMol = readFile();
                System.out.println(arrMol);
                for(int i = 0; i < arrMol.size(); i++){
                    Sphere sphere = new Sphere(arrMol.get(i).radius);
                    PhongMaterial material = new PhongMaterial();
                    material.setDiffuseColor(arrMol.get(i).col);
                    material.setSpecularColor(Color.BLACK);
                    sphere.setMaterial(material);
                    arrSphere.add(sphere);
                    setPos(arrSphere.get(i), arrMol.get(i).x,arrMol.get(i).y,arrMol.get(i).z);
                    arrPoints.add(new Point3D(arrSphere.get(i).getTranslateX(), arrSphere.get(i).getTranslateY(), arrSphere.get(i).getTranslateZ()));
                    group.getChildren().add(arrSphere.get(i));
                }
            }
            group.getChildren().removeAll();

            ArrayList<ArrayList<Integer>> pairs = getPairs(arrSphere);
            for(int i = 0; i < pairs.size(); i++){
                group.getChildren().add(paintCylinder(arrPoints.get(pairs.get(i).get(0)), arrPoints.get(pairs.get(i).get(1))));
            }
        });

        subScene.setRoot(group);
        subScene.setFill(Color.SILVER);

    }
}

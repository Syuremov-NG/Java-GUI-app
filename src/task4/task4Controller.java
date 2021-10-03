package task4;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.util.converter.NumberStringConverter;

public class task4Controller {

    class Chart{
        public double min;
        public double max;
        public boolean showed;
        public double width;
        public String function;
        public int index;
        XYChart.Series ser = new XYChart.Series();
        Chart(double min, double max, boolean showed, double width, String func, int index){
            this.min = min;
            this.max = max;
            this.showed = showed;
            this.width = width;
            this.function = func;
            this.index = index;
            ser.setName(func);
        }
        public void repaint(){
            ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
            for(double i = this.min; i <= this.max; i += 0.05){
                double func = switch (this.function) {
                    case "y(x) = sin(x)" -> Math.sin(i);
                    case "y(x) = cos(x) - 2 * sin(x)" -> Math.cos(i) - 2 * Math.sin(i);
                    case "y(x) = sin(x * x)" -> Math.sin(i*i);
                    default -> Math.sin(i);
                };
                datas.add(new XYChart.Data(i, func));
            }
            this.ser.setData(datas);
        }
    }

    private int visibleCharts = 0;
    private Map<String,Chart> funcs = new HashMap<>();


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private LineChart<?, ?> lineCharts;

    @FXML
    private ComboBox<String> funcBox;

    @FXML
    private TextField minField;

    @FXML
    private TextField widthField;

    @FXML
    private TextField maxField;

    @FXML
    private RadioButton showRadio;

    @FXML
    private ToggleGroup visible;

    @FXML
    private RadioButton hideRadio;



    @FXML
    void initialize() {
        funcBox.setItems(FXCollections.observableArrayList(
                "y(x) = sin(x)", "y(x) = cos(x) - 2 * sin(x)", "y(x) = sin(x * x)"
        ));
        funcs.put("y(x) = sin(x)", new Chart(-5,5,false,2, "y(x) = sin(x)", 0));
        funcs.put("y(x) = cos(x) - 2 * sin(x)", new Chart(-5,5,false,2, "y(x) = cos(x) - 2 * sin(x)", 1));
        funcs.put("y(x) = sin(x * x)", new Chart(-5,5,false,2, "y(x) = sin(x * x)", 2));

        funcs.forEach((k,v) -> {
            v.repaint();
        });

        widthField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(widthField.getText() != "" && funcBox.getValue() != null && funcs.get(funcBox.getValue()).showed){
                funcs.get(funcBox.getValue()).width = Double.parseDouble(widthField.getText());
                lineCharts.lookup(".default-color"+lineCharts.getData().indexOf(funcs.get(funcBox.getValue()).ser)+".chart-series-line").setStyle("-fx-stroke-width: "+ funcs.get(funcBox.getValue()).width +"px;");
            }
            else if(widthField.getText() == "" && funcBox.getValue() != null && funcs.get(funcBox.getValue()).showed){
                funcs.get(funcBox.getValue()).width = 1;
                lineCharts.lookup(".default-color"+lineCharts.getData().indexOf(funcs.get(funcBox.getValue()).ser)+".chart-series-line").setStyle("-fx-stroke-width: "+ funcs.get(funcBox.getValue()).width +"px;");
            }
        });
        minField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(minField.getText() != ""){
                funcs.get(funcBox.getValue()).min = Double.parseDouble(minField.getText());
                funcs.get(funcBox.getValue()).repaint();
            }
            else{
                funcs.get(funcBox.getValue()).min = 0;
                funcs.get(funcBox.getValue()).repaint();
            }
        });
        maxField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(maxField.getText() != ""){
                funcs.get(funcBox.getValue()).max = Double.parseDouble(maxField.getText());
                funcs.get(funcBox.getValue()).repaint();
            }
            else{
                funcs.get(funcBox.getValue()).max = 0;
                funcs.get(funcBox.getValue()).repaint();
            }
        });
        funcBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showRadio.setSelected(funcs.get(funcBox.getValue()).showed);
                hideRadio.setSelected(!funcs.get(funcBox.getValue()).showed);
                widthField.setText(String.valueOf(funcs.get(funcBox.getValue()).width));
                minField.setText(String.valueOf(funcs.get(funcBox.getValue()).min));
                maxField.setText(String.valueOf(funcs.get(funcBox.getValue()).max));
            }
        });
        showRadio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(showRadio.isSelected() && funcBox.getValue() != null){

                    lineCharts.getData().add(funcs.get(funcBox.getValue()).ser);
                    funcs.get(funcBox.getValue()).showed = true;
                }
                if(widthField.getText() != "" && funcBox.getValue() != null && funcs.get(funcBox.getValue()).showed){
                    funcs.get(funcBox.getValue()).width = Double.parseDouble(widthField.getText());
                    lineCharts.lookup(".default-color"+lineCharts.getData().indexOf(funcs.get(funcBox.getValue()).ser)+".chart-series-line").setStyle("-fx-stroke-width: "+ funcs.get(funcBox.getValue()).width +"px;");
                }
                else if(widthField.getText() == "" && funcBox.getValue() != null && funcs.get(funcBox.getValue()).showed){
                    funcs.get(funcBox.getValue()).width = 1;
                    lineCharts.lookup(".default-color"+lineCharts.getData().indexOf(funcs.get(funcBox.getValue()).ser)+".chart-series-line").setStyle("-fx-stroke-width: "+ funcs.get(funcBox.getValue()).width +"px;");
                }
            }
        });
        hideRadio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(hideRadio.isSelected() && funcBox.getValue() != null){
                    lineCharts.getData().remove(funcs.get(funcBox.getValue()).ser);
                    funcs.get(funcBox.getValue()).showed = false;
                }
            }
        });
        lineCharts.setCreateSymbols(false);
        lineCharts.setAnimated(false);
    }
}

package task5;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

public class task5 extends Application {

    private final TableView table = new TableView();
    private final ObservableList<Languages> langs = FXCollections.observableArrayList(
            new Languages("Си", "Деннис Ритчи", "1972"),
            new Languages("С++", "Бьерн Страуструп", "1983"),
            new Languages("Python", "Гвидо ван Россум", "1991"),
            new Languages("Java", "Джеймс Гослинг", "1995"),
            new Languages("JavaScript", "Брендон Айк", "1995"),
            new Languages("С#", "Андерс Хейлсберг", "2001"),
            new Languages("Scala", "Мартин Одерски", "2003")
    );
    final HBox hb = new HBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(450);
        stage.setHeight(550);

        final Label label = new Label("Языки программирования");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);
        table.setTableMenuButtonVisible(true);
        Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn p) {
                        return new EditingCell();
                    }
                };

        TableColumn langCol = new TableColumn("Язык");
        langCol.setMinWidth(100);
        langCol.setCellValueFactory(
                new PropertyValueFactory<Languages, String>("lang"));
        langCol.setCellFactory(cellFactory);
        langCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Languages, String>>() {
                    @Override
                    public void handle(CellEditEvent<Languages, String> t) {
                        ((Languages) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setLang(t.getNewValue());
                    }
                }
        );


        TableColumn authorCol = new TableColumn("Автор");
        authorCol.setMinWidth(100);
        authorCol.setCellValueFactory(
                new PropertyValueFactory<Languages, String>("author"));
        authorCol.setCellFactory(cellFactory);
        authorCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Languages, String>>() {
                    @Override
                    public void handle(CellEditEvent<Languages, String> t) {
                        ((Languages) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setAuthor(t.getNewValue());
                    }
                }
        );

        TableColumn yearCol = new TableColumn("Год");
        yearCol.setMinWidth(100);
        yearCol.setCellValueFactory(
                new PropertyValueFactory<Languages, String>("year"));
        yearCol.setCellFactory(cellFactory);
        yearCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Languages, String>>() {
                    @Override
                    public void handle(CellEditEvent<Languages, String> t) {
                        ((Languages) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setYear(t.getNewValue());
                    }
                }
        );

        table.setItems(langs);
        table.getColumns().addAll(langCol, authorCol, yearCol);

        final TextField addLang = new TextField();
        addLang.setPromptText("Язык");
        addLang.setMaxWidth(langCol.getPrefWidth());
        final TextField addAuthor = new TextField();
        addAuthor.setMaxWidth(authorCol.getPrefWidth());
        addAuthor.setPromptText("Имя");
        final TextField addYear = new TextField();
        addYear.setMaxWidth(yearCol.getPrefWidth());
        addYear.setPromptText("Год");

        final Button addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                langs.add(new Languages(
                        addLang.getText(),
                        addAuthor.getText(),
                        addYear.getText()));
                addLang.clear();
                addAuthor.clear();
                addYear.clear();
            }
        });
        Button delButton = new Button("Delete");
        delButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                table.getItems().remove(table.getSelectionModel().getSelectedIndex());
            }
        });

        hb.getChildren().addAll(addLang, addAuthor, addYear, addButton, delButton);
        hb.setSpacing(3);


        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }

    public static class Languages{
        private SimpleStringProperty lang;
        private SimpleStringProperty author;
        private SimpleStringProperty year;

        Languages(String lang, String author, String year) {
            this.lang = new SimpleStringProperty(lang);
            this.author = new SimpleStringProperty(author);
            this.year = new SimpleStringProperty(year);
        }

        public String getLang() {
            return lang.get();
        }

        public String getYear() {
            return year.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public void setAuthor(String author) {
            this.author.set(author);
        }

        public void setLang(String lang) {
            this.lang.set(lang);
        }

        public void setYear(String year) {
            this.year.set(year);
        }
    }

    class EditingCell extends TableCell<Languages, String> {

        private TextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText((String) getItem());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
            textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0,
                                    Boolean arg1, Boolean arg2) {
                    if (!arg2) {
                        commitEdit(textField.getText());
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
}
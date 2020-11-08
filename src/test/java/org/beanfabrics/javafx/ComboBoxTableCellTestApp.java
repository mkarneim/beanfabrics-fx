package org.beanfabrics.javafx;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ComboBoxTableCellTestApp extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    TableView<Contact> tableView = new TableView<>();
    tableView.setEditable(true);

    TableColumn<Contact, String> titleCol = new TableColumn<>("Title");
    titleCol.setCellValueFactory(param -> param.getValue().title);
    titleCol.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList("Mr", "Mrs", "Dr")));
    titleCol.setEditable(true);
    tableView.getColumns().add(titleCol);

    TableColumn<Contact, String> nameCol = new TableColumn<>("Name");
    nameCol.setCellValueFactory(param -> param.getValue().name);
    nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    nameCol.setEditable(true);
    tableView.getColumns().add(nameCol);

    tableView.getItems().addAll(new Contact("Mr", "Smith"), new Contact("Mrs", "Penny"), new Contact("Dr", "Dolittle"));

    BorderPane root = new BorderPane();
    root.setCenter(tableView);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  public static class Contact {
    public StringProperty title = new SimpleStringProperty();
    public StringProperty name = new SimpleStringProperty();

    public Contact(String title, String name) {
      this.title.setValue(title);
      this.name.setValue(name);
    }
  }

}


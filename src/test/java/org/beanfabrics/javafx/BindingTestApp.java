package org.beanfabrics.javafx;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class BindingTestApp extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    StringProperty p = new SimpleStringProperty();

    TextField tfA = new TextField();
    tfA.textProperty().bindBidirectional(p);
    TextField tfB = new TextField();
    tfB.textProperty().bind(tfA.textProperty());



    Button button = new Button("Hi");
    HBox pane = new HBox();
    pane.getChildren().addAll(button, tfA, tfB);
    Parent root = pane;
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.setWidth(400);
    primaryStage.setHeight(400);
  }

}

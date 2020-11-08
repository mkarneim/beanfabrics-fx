package org.beanfabrics.javafx.binder;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.binder.BooleanPropertyBinder;
import org.beanfabrics.model.BooleanPM;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class BooleanPropertyBinderTestApp extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    BooleanProperty p = new SimpleBooleanProperty();
    BooleanPM pm = new BooleanPM();
    pm.addPropertyChangeListener(e -> {
      System.out.println("changed: " + pm.getBoolean());
    });
    ModelProvider localModelProvider = new ModelProvider();
    localModelProvider.setPresentationModel(pm);
    BooleanPropertyBinder binder = new BooleanPropertyBinder();

    binder.bind(p, localModelProvider, new Path("this"));

    CheckBox cb = new CheckBox();
    cb.selectedProperty().bindBidirectional(p);

    Button btn = new Button();
    btn.setOnAction(e -> {
      pm.setBoolean(!pm.getBoolean());
    });
    HBox pane = new HBox();
    pane.getChildren().addAll(cb, btn);
    Parent root = pane;
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.setWidth(400);
    primaryStage.setHeight(400);
  }

}

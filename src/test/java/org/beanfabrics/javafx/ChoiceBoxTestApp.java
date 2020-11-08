package org.beanfabrics.javafx;

import java.util.UUID;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.binder.BnFxBinder;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.Options;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Operation;

public class ChoiceBoxTestApp extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    DummyPM pm = new DummyPM();
    ModelProvider localModelProvider = new ModelProvider();
    localModelProvider.setPresentationModel(pm);

    ChoiceBox<String> choiceBox = new ChoiceBox<>();
    BnFxBinder.bind(choiceBox, localModelProvider, new Path("weekday"));

    Button removeButton = new Button("Remove");
    BnFxBinder.bind(removeButton, localModelProvider, new Path("removeSelectedDay"));
    Button addButton = new Button("Add");
    BnFxBinder.bind(addButton, localModelProvider, new Path("addDummyDay"));
    Button replaceButton = new Button("Replace");
    BnFxBinder.bind(replaceButton, localModelProvider, new Path("replaceOptions"));

    TextField valueTextField = new TextField();
    BnFxBinder.bind(valueTextField, localModelProvider, new Path("weekday"));

    HBox hbox = new HBox();
    hbox.setPadding(new Insets(20));
    hbox.setSpacing(20);
    hbox.getChildren().addAll(choiceBox, removeButton, addButton, replaceButton, valueTextField);
    Parent root = hbox;
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.setWidth(400);
    primaryStage.setHeight(400);
  }

  @SuppressWarnings("unused")
  private static class DummyPM extends AbstractPM {
    TextPM weekday = new TextPM();
    OperationPM removeSelectedDay = new OperationPM();
    OperationPM addDummyDay = new OperationPM();
    OperationPM replaceOptions = new OperationPM();

    public DummyPM() {
      PMManager.setup(this);
      weekday.setOptions(Options.create("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
      weekday.setText("Monday");
    }

    @Operation
    public void removeSelectedDay() {
      @SuppressWarnings("unchecked")
      Options<String> options = (Options<String>) weekday.getOptions();
      options.remove(weekday.getText());
      weekday.setOptions(options);
    }

    @Operation
    public void addDummyDay() {
      @SuppressWarnings("unchecked")
      Options<String> options = (Options<String>) weekday.getOptions();
      String v = UUID.randomUUID().toString();
      options.put(v, v);
      weekday.setOptions(options);
    }

    @Operation
    public void replaceOptions() {
      weekday.setOptions(Options.create("a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa"));
      weekday.setText("A");
    }

  }

}

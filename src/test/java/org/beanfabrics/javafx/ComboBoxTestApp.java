package org.beanfabrics.javafx;

import java.util.UUID;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.binder.BnFxBinder;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.Options;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Operation;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ComboBoxTestApp extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    DummyPM pm = new DummyPM();
    ModelProvider localModelProvider = new ModelProvider();
    localModelProvider.setPresentationModel(pm);

    ComboBox<String> weekdayComboBox = new ComboBox<>();
    weekdayComboBox.setEditable(true);
    BnFxBinder.bind(weekdayComboBox, localModelProvider, new Path("weekday"));

    ComboBox<String> colorComboBox = new ComboBox<>();
    BnFxBinder.bind(colorComboBox, localModelProvider, new Path("color"));

    Button removeButton = new Button("Remove");
    BnFxBinder.bind(removeButton, localModelProvider, new Path("removeSelectedDay"));
    Button addButton = new Button("Add");
    BnFxBinder.bind(addButton, localModelProvider, new Path("addDummyDay"));
    Button replaceButton = new Button("Replace");
    BnFxBinder.bind(replaceButton, localModelProvider, new Path("replaceOptions"));

    TextField weekdayTextField = new TextField();
    BnFxBinder.bind(weekdayTextField, localModelProvider, new Path("weekday"));

    TextField colorTextField = new TextField();
    BnFxBinder.bind(colorTextField, localModelProvider, new Path("color"));

    HBox hbox = new HBox();
    hbox.setPadding(new Insets(20));
    hbox.setSpacing(20);
    hbox.getChildren().addAll(weekdayComboBox, colorComboBox, removeButton, addButton, replaceButton, weekdayTextField,
        colorTextField);
    Parent root = hbox;
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.setWidth(900);
    primaryStage.setHeight(400);
  }

  @SuppressWarnings("unused")
  private static class DummyPM extends AbstractPM {
    TextPM weekday = new TextPM();
    TextPM color = new TextPM();
    OperationPM removeSelectedDay = new OperationPM();
    OperationPM addDummyDay = new OperationPM();
    OperationPM replaceOptions = new OperationPM();

    public DummyPM() {
      PMManager.setup(this);
      weekday.setOptions(Options.create("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
      weekday.setText("Monday");
      color.setOptions(Options.create("Green", "Blue", "Yellow", "Red", "Blue", "Orange", "Purple", "Black", "White"));
      color.setText("Green");
    }

    @Operation
    public void removeSelectedDay() {
      @SuppressWarnings("unchecked")
      Options<String> options = weekday.getOptions();
      options.remove(weekday.getText());
      weekday.setOptions(options);
    }

    @Operation
    public void addDummyDay() {
      @SuppressWarnings("unchecked")
      Options<String> options = weekday.getOptions();
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

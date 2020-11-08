package org.beanfabrics.javafx;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.binder.BnFxBinder;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class RadioButtonTestApp extends Application {
  public static void main(String[] args) {
    launch(args);
  }


  @Override
  public void start(Stage primaryStage) throws Exception {
    ChoicePM pm = new ChoicePM();
    ModelProvider localModelProvider = new ModelProvider();
    localModelProvider.setPresentationModel(pm);

    ToggleGroup group = new ToggleGroup();
    RadioButton btn1 = new RadioButton("Green");
    btn1.setToggleGroup(group);
    BnFxBinder.bind(btn1, localModelProvider, new Path("green"));
    RadioButton btn2 = new RadioButton("Blue");
    btn2.setToggleGroup(group);
    BnFxBinder.bind(btn2, localModelProvider, new Path("blue"));
    RadioButton btn3 = new RadioButton("Red");
    btn3.setToggleGroup(group);
    BnFxBinder.bind(btn3, localModelProvider, new Path("red"));

    TextField textTf = new TextField();
    BnFxBinder.bind(textTf, localModelProvider, new Path("text"));

    HBox hbox = new HBox();
    hbox.setPadding(new Insets(20));
    hbox.setSpacing(20);
    hbox.getChildren().addAll(btn1, btn2, btn3, textTf);
    Parent root = hbox;
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.setWidth(500);
    primaryStage.setHeight(400);
  }

  private class ChoicePM extends AbstractPM {
    BooleanPM green = new BooleanPM();
    BooleanPM blue = new BooleanPM();
    BooleanPM red = new BooleanPM();
    TextPM text = new TextPM();

    public ChoicePM() {
      PMManager.setup(this);
    }

    @OnChange
    public void onChange() {
      if (green.getBoolean()) {
        text.setText("Green");
      }
      if (blue.getBoolean()) {
        text.setText("Blue");
      }
      if (red.getBoolean()) {
        text.setText("Red");
      }
    }


  }
}

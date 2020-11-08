package org.beanfabrics.javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.binder.BnFxBinder;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.ConversionException;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Validation;

public class CheckBoxTestApp extends Application {
  public static void main(String[] args) {
    launch(args);
  }


  @Override
  public void start(Stage primaryStage) throws Exception {
    DriverPM pm = new DriverPM();
    ModelProvider localModelProvider = new ModelProvider();
    localModelProvider.setPresentationModel(pm);

    CheckBox checkBox = new CheckBox("May Drive");
    BnFxBinder.bind(checkBox, localModelProvider, new Path("mayDrive"));
    TextField ageTextField = new TextField("10");
    BnFxBinder.bind(ageTextField, localModelProvider, new Path("age"));

    HBox hbox = new HBox();
    hbox.setPadding(new Insets(20));
    hbox.setSpacing(20);
    hbox.getChildren().addAll(checkBox, ageTextField);
    Parent root = hbox;
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.setWidth(400);
    primaryStage.setHeight(400);
  }

  private class DriverPM extends AbstractPM {
    IntegerPM age = new IntegerPM();
    BooleanPM mayDrive = new BooleanPM();

    public DriverPM() {
      age.setMandatory(true);
      mayDrive.setEditable(false);
      PMManager.setup(this);
    }

    @OnChange(path = "age")
    void onAgeChanged() {
      try {
        mayDrive.setBoolean(age.getInteger() >= 18);
      } catch (NullPointerException | ConversionException e) {
        mayDrive.setBoolean(false);
      }
    }

    @Validation(path = "mayDrive", message = "Can't determine if driver is allowed to drive")
    public boolean isMayDriveValid() {
      return age.isValid();
    }
  }
}

package org.beanfabrics.javafx;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.binder.BnFxBinder;
import org.beanfabrics.javafx.property.TextPmProperty;
import org.beanfabrics.javafx.validation.GraphicValidationFromTextPmSupport;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Validation;

public class TextPMPropertyApp extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    KontoPM pm = new KontoPM();

    ModelProvider localModelProvider = new ModelProvider();
    localModelProvider.setPresentationModel(pm);

    TextPmProperty namePropertyA = new TextPmProperty(localModelProvider, new Path("name"));
    TextPmProperty namePropertyB = new TextPmProperty();
    namePropertyB.bindBidirectional(namePropertyA);
    TextPmProperty namePropertyC = new TextPmProperty();
    namePropertyC.bind(namePropertyA);
    // namePropertyC.bindBidirectional(namePropertyA);


    TextPmProperty nameDescProperty = new TextPmProperty(localModelProvider, new Path("nameDescription"));

    Button button = new Button("Hi");


    //
    TextField textFieldA = new TextField();
    textFieldA.textProperty().bindBidirectional(namePropertyA.textProperty());
    textFieldA.promptTextProperty().bind(namePropertyA.descriptionProperty());
    textFieldA.editableProperty().bind(namePropertyA.editableProperty());
    // textFieldA.tooltipProperty().bind(namePropertyA.descriptionProperty().asTooltip());

    GraphicValidationFromTextPmSupport gvs = new GraphicValidationFromTextPmSupport(textFieldA);
    gvs.textPmProperty().bind(namePropertyA);

    Label validationLabel = new Label();
    validationLabel.textProperty().bind(namePropertyA.validationStateProperty().asString());

    // TODO Anbindung von mandatory und validationState !!!!

    //
    TextField textFieldB = new TextField();
    textFieldB.textProperty().bindBidirectional(namePropertyB.textProperty());
    textFieldB.promptTextProperty().bind(namePropertyB.descriptionProperty());
    textFieldB.tooltipProperty().bind(namePropertyB.descriptionProperty().asTooltip());

    //
    TextField textFieldC = new TextField();
    // textFieldC.textProperty().bindBidirectional(namePropertyC.textProperty());
    // textFieldC.promptTextProperty().bind(namePropertyC.descriptionProperty());
    // textFieldC.tooltipProperty().bind(namePropertyC.descriptionProperty().asTooltip());

    BnFxBinder.bind(textFieldC, localModelProvider, new Path("name"));

    //
    TextField nameDescTf = new TextField();
    nameDescTf.textProperty().bindBidirectional(nameDescProperty.textProperty());

    HBox hBox = new HBox();
    hBox.setSpacing(20);
    hBox.getChildren().addAll(button, new Label("A"), textFieldA, new Label("B"), textFieldB, new Label("C"),
        textFieldC, nameDescTf);

    VBox vBox = new VBox();
    vBox.setSpacing(20);
    vBox.getChildren().addAll(hBox, validationLabel);
    Parent root = vBox;
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.setWidth(800);
    primaryStage.setHeight(400);
  }

  public static class KontoPM extends AbstractPM {
    TextPM name = new TextPM();
    TextPM nameDescription = new TextPM();

    public KontoPM() {
      PMManager.setup(this);
      name.setTitle("Kontoinhaber");
      nameDescription.setText("Der Name des Kontoinhabers");
    }

    @OnChange(path = "nameDescription")
    void onChangeNameDescription() {
      name.setDescription(nameDescription.getText());
      name.setEditable(!nameDescription.isEmpty());
    }

    @Validation(path = "name", message = "Der Name enthält nicht erlaubte Zeichen")
    boolean isValidName() {
      return !name.getText().contains("#");
    }

  }


}

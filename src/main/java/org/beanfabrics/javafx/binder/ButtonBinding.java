package org.beanfabrics.javafx.binder;

import static com.google.common.base.Preconditions.checkNotNull;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.OperationPmProperty;
import org.beanfabrics.javafx.validation.GraphicValidationFromOperationPmSupport;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ButtonBinding implements BnBinding {
  private Button control;
  private GraphicValidationFromOperationPmSupport gvs;
  private EventHandler<? super ActionEvent> eventHandler;

  public ButtonBinding(Button control, IModelProvider modelProvider, Path path) {
    this(control, new OperationPmProperty(modelProvider, path));
  }

  public ButtonBinding(Button control, OperationPmProperty property) {
    this.control = checkNotNull(control, "control == null!");
    checkNotNull(property, "property == null!");

    // TODO (mka, 5.4.2018) Can we somehow bind the textProperty to the titleProperty in a way that it
    // is
    // only used if the button doen't specify its name itself? I guess not...
    // control.textProperty().bind(operationPmProperty.titleProperty());


    // We use the addEventHandler method here since we want to allow that the application programmer
    // still can use the control.setOnAction(...)
    eventHandler = e -> {
      property.getOnAction().handle(e);
    };
    control.addEventHandler(ActionEvent.ACTION, eventHandler);

    gvs = new GraphicValidationFromOperationPmSupport(control);
    gvs.operationPmProperty().bind(property);

    control.disableProperty().bind(property.isNull().or(property.enabledProperty().not()));
  }

  @Override
  public void dispose() {
    control.removeEventHandler(ActionEvent.ACTION, eventHandler);
    gvs.operationPmProperty().unbind();
    control.disableProperty().unbind();
  }

}

package org.beanfabrics.javafx.validation;

import javafx.application.Platform;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import org.beanfabrics.javafx.property.OperationPmProperty;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.validation.ValidationState;
import org.controlsfx.validation.ValidationMessage;

public class GraphicValidationFromOperationPmSupport extends AbstractGraphicValidationSupport {

  private final TooltipValidationDecoration deco = new TooltipValidationDecoration();

  public GraphicValidationFromOperationPmSupport(Control control) {
    super(control);
  }

  //// operationPm ////
  private OperationPmProperty operationPm = new OperationPmProperty() {
    {
      validationStateProperty().addListener(e -> updateControl());
      enabledProperty().addListener(e -> updateControl());
      descriptionProperty().addListener(e -> updateControl());
    }

    @Override
    protected void invalidated() {
      super.invalidated();
      updateControl();
    };
  };

  public OperationPmProperty operationPmProperty() {
    return operationPm;
  }

  public IOperationPM getOperationPm() {
    return operationPmProperty().getValue();
  }

  public void setOperationPm(IOperationPM value) {
    operationPmProperty().set(value);
  }

  private void updateControl() {
    if (control.getScene() != null && control.getScene().getWindow() != null) {
      updateDecoration();
    }
  }

  @Override
  protected void updateDecoration() {
    Platform.runLater(() -> {
      ValidationState validationState = operationPmProperty().getValidationState();
      String description = operationPmProperty().getDescription();
      deco.removeDecorations(control);
      if (validationState != null) {
        deco.applyValidationDecoration(ValidationMessage.warning(control, validationState.getMessage()));
      } else {
        Tooltip tooltip = null;
        if (description != null && description.length() > 0) {
          tooltip = new Tooltip(description);
        }
        control.setTooltip(tooltip);
      }
    });
  }

}

package org.beanfabrics.javafx.validation;

import java.util.Arrays;
import java.util.Collection;
import org.beanfabrics.javafx.property.TextPmProperty;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.validation.ValidationState;
import org.controlsfx.control.decoration.Decoration;
import org.controlsfx.control.decoration.GraphicDecoration;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;

public class GraphicValidationFromTextPmSupport extends AbstractGraphicValidationSupport {
  private static final ValidationTooltipFactory TOOLTIP_FACTORY = new ValidationTooltipFactory();

  private final GraphicValidationDecoration deco = new GraphicValidationDecoration() {

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<Decoration> createValidationDecorations(ValidationMessage message) {
      // we want to have an additional offset in order to make sure that rendering this decoration inside
      // a table cell will not be clipped.
      return Arrays.asList(new GraphicDecoration(createDecorationNode(message), Pos.BOTTOM_LEFT, offsetX, offsetY));
    }

  };

  private int offsetX = 2;
  private int offsetY = -2;

  public GraphicValidationFromTextPmSupport(Control control) {
    super(control);
  }

  public int getOffsetX() {
    return offsetX;
  }

  public int getOffsetY() {
    return offsetY;
  }

  public void setOffsetX(int offsetX) {
    this.offsetX = offsetX;
    updateDecoration();
  }

  public void setOffsetY(int offsetY) {
    this.offsetY = offsetY;
    updateDecoration();
  }

  //// textPm ////
  private TextPmProperty textPm = new TextPmProperty() {
    {
      validationStateProperty().addListener(e -> updateControl());
      mandatoryProperty().addListener(e -> updateControl());
      emptyProperty().addListener(e -> updateControl());
      descriptionProperty().addListener(e -> updateControl());
    }

    @Override
    protected void invalidated() {
      super.invalidated();
      updateControl();
    };
  };

  public TextPmProperty textPmProperty() {
    return textPm;
  }

  public ITextPM getTextPm() {
    return textPmProperty().getValue();
  }

  public void setTextPm(ITextPM value) {
    textPmProperty().set(value);
  }

  protected void updateControl() {
    ValidationState validationState = textPmProperty().getValidationState();
    String description = textPmProperty().getDescription();

    Tooltip tooltip = null;
    if (validationState != null) {
      tooltip = TOOLTIP_FACTORY.createTooltip(validationState.getMessage(), Severity.ERROR);
    } else if (description != null) {
      tooltip = new Tooltip(description);
    }
    control.setTooltip(tooltip);

    if (control.getScene() != null && control.getScene().getWindow() != null) {
      updateDecoration();
    }
  }

  @Override
  protected void updateDecoration() {
    Platform.runLater(() -> {

      ValidationState validationState = textPmProperty().getValidationState();
      boolean mandatory = textPmProperty().isMandatory();
      boolean empty = textPmProperty().isEmpty();

      deco.removeDecorations(control);

      if (validationState != null) {
        deco.applyValidationDecoration(ValidationMessage.error(control, validationState.getMessage()));
      }
      if (mandatory && empty) {
        deco.applyRequiredDecoration(control);
      }
    });
  }

}

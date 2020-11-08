package org.beanfabrics.javafx.validation;

import java.util.Arrays;
import java.util.Collection;

import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.decoration.Decoration;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.decoration.AbstractValidationDecoration;

public class TooltipValidationDecoration extends AbstractValidationDecoration {

  private final ValidationTooltipFactory tooltipFactory = new ValidationTooltipFactory();

  /**
   * Creates default instance
   */
  public TooltipValidationDecoration() {}

  private Rectangle createDecorationNode(ValidationMessage message) {
    Rectangle result = new Rectangle(100, 100);
    Tooltip.install(result, tooltipFactory.createTooltip(message.getText(), message.getSeverity()));
    result.setFill(Color.TRANSPARENT);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Collection<Decoration> createValidationDecorations(ValidationMessage message) {
    return Arrays.asList(new OverlayGraphicDecoration(createDecorationNode(message)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Collection<Decoration> createRequiredDecorations(Control target) {
    throw new UnsupportedOperationException();
  }

}

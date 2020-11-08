package org.beanfabrics.javafx.validation;

import javafx.scene.control.Tooltip;
import org.controlsfx.validation.Severity;

/**
 * Factory for the cration of validation-related {@link Tooltip}s.
 * <p>
 * Originally the constants and the method are copied from
 * {@link org.controlsfx.validation.decoration.GraphicValidationDecoration}
 */
public class ValidationTooltipFactory {

  private static final String POPUP_SHADOW_EFFECT =
      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 5, 0, 0, 5);"; //$NON-NLS-1$
  private static final String TOOLTIP_COMMON_EFFECTS = "-fx-font-weight: bold; -fx-padding: 5; -fx-border-width:1;"; //$NON-NLS-1$
  private static final String ERROR_TOOLTIP_EFFECT = POPUP_SHADOW_EFFECT + TOOLTIP_COMMON_EFFECTS
      + "-fx-background-color: FBEFEF; -fx-text-fill: cc0033; -fx-border-color:cc0033;"; //$NON-NLS-1$

  private static final String WARNING_TOOLTIP_EFFECT = POPUP_SHADOW_EFFECT + TOOLTIP_COMMON_EFFECTS
      + "-fx-background-color: FFFFCC; -fx-text-fill: CC9900; -fx-border-color: CC9900;"; //$NON-NLS-1$

  public Tooltip createTooltip(String text, Severity severity) {
    if (text == null || text.trim().isEmpty()) {
      return null;
    }
    Tooltip tooltip = new Tooltip(text);
    tooltip.setOpacity(.9);
    tooltip.setAutoFix(true);
    tooltip.setStyle(Severity.ERROR == severity ? ERROR_TOOLTIP_EFFECT : WARNING_TOOLTIP_EFFECT);
    return tooltip;
  }
}

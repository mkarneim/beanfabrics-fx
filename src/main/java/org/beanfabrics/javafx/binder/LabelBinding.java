package org.beanfabrics.javafx.binder;

import static com.google.common.base.Preconditions.checkNotNull;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.TextPmProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;

public class LabelBinding implements BnBinding {
  private Labeled control;
  private TextPmProperty property;

  public LabelBinding(Label control, IModelProvider modelProvider, Path path) {
    this(control, new TextPmProperty(modelProvider, path));
  }

  public LabelBinding(Labeled control, TextPmProperty property) {
    this.control = checkNotNull(control, "control == null!");
    this.property = checkNotNull(property, "property == null!");

    control.textProperty().bindBidirectional(property.textProperty());
    control.tooltipProperty().bind(property.descriptionProperty().asTooltip());

    // TODO (mka, 5.4.2018) Should we show the validation state on labels?
    // GraphicValidationSupport gvs = new GraphicValidationSupport(control);
    // gvs.textPmProperty().bind(textPmProperty);

    control.disableProperty().bind(property.isNull());
  }

  @Override
  public void dispose() {
    control.textProperty().unbindBidirectional(property.textProperty());
    control.tooltipProperty().unbind();
    control.disableProperty().unbind();
  }
}

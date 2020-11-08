package org.beanfabrics.javafx.binder;

import static com.google.common.base.Preconditions.checkNotNull;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.TextPmProperty;
import org.beanfabrics.javafx.validation.GraphicValidationFromTextPmSupport;
import javafx.scene.control.ChoiceBox;

public class ChoiceBoxBinding implements BnBinding {
  private ChoiceBox<String> control;
  private TextPmProperty property;
  private GraphicValidationFromTextPmSupport gvs;

  public ChoiceBoxBinding(ChoiceBox<String> control, IModelProvider modelProvider, Path path) {
    this(control, new TextPmProperty(modelProvider, path));
  }

  public ChoiceBoxBinding(ChoiceBox<String> control, TextPmProperty property) {
    this.control = checkNotNull(control, "control == null!");
    this.property = checkNotNull(property, "property == null!");

    control.valueProperty().bindBidirectional(property.textProperty());
    control.disableProperty().bind(property.isNull().or(property.editableProperty().not()));
    control.itemsProperty().bind(property.optionsProperty());
    gvs = new GraphicValidationFromTextPmSupport(control);
    gvs.textPmProperty().bind(property);
  }

  @Override
  public void dispose() {
    control.valueProperty().unbindBidirectional(property.textProperty());
    control.disableProperty().unbind();
    control.itemsProperty().unbind();
    gvs.textPmProperty().unbind();
  }

}

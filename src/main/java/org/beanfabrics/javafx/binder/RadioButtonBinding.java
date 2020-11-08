package org.beanfabrics.javafx.binder;

import static com.google.common.base.Preconditions.checkNotNull;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.BooleanPmProperty;
import org.beanfabrics.javafx.validation.GraphicValidationFromTextPmSupport;
import javafx.scene.control.RadioButton;

public class RadioButtonBinding implements BnBinding {
  private RadioButton control;
  private BooleanPmProperty property;
  private GraphicValidationFromTextPmSupport gvs;

  public RadioButtonBinding(RadioButton control, IModelProvider modelProvider, Path path) {
    this(control, new BooleanPmProperty(modelProvider, path));
  }

  public RadioButtonBinding(RadioButton control, BooleanPmProperty property) {
    this.control = checkNotNull(control, "control == null!");
    this.property = checkNotNull(property, "property == null!");

    control.selectedProperty().bindBidirectional(property.selectedProperty());
    control.disableProperty().bind(property.isNull().or(property.editableProperty().not()));

    gvs = new GraphicValidationFromTextPmSupport(control);
    gvs.textPmProperty().bind(property);
  }


  @Override
  public void dispose() {
    control.selectedProperty().unbindBidirectional(property.selectedProperty());
    control.disableProperty().unbind();
    gvs.textPmProperty().unbind();
  }

}

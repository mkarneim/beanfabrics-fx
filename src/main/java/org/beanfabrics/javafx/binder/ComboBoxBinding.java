package org.beanfabrics.javafx.binder;

import static com.google.common.base.Preconditions.checkNotNull;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.TextPmProperty;
import org.beanfabrics.javafx.validation.GraphicValidationFromTextPmSupport;
import javafx.scene.control.ComboBox;

public class ComboBoxBinding implements BnBinding {
  private final ComboBox<String> control;
  private final TextPmProperty property;
  private final GraphicValidationFromTextPmSupport gvs;

  public ComboBoxBinding(ComboBox<String> control, IModelProvider modelProvider, Path path) {
    this(control, new TextPmProperty(modelProvider, path));
  }

  public ComboBoxBinding(ComboBox<String> control, TextPmProperty property) {
    this.control = checkNotNull(control, "control == null!");
    this.property = checkNotNull(property, "property == null!");

    control.valueProperty().bindBidirectional(property.textProperty());
    control.getEditor().textProperty().bindBidirectional(property.textProperty());
    control.disableProperty().bind(property.isNull().or(property.editableProperty().not()));
    control.itemsProperty().bind(property.optionsProperty());
    gvs = new GraphicValidationFromTextPmSupport(control);
    gvs.textPmProperty().bind(property);
  }

  public GraphicValidationFromTextPmSupport getGvs() {
    return gvs;
  }

  @Override
  public void dispose() {
    control.valueProperty().unbindBidirectional(property.textProperty());
    control.getEditor().textProperty().unbindBidirectional(property.textProperty());
    control.disableProperty().unbind();
    control.itemsProperty().unbind();
    gvs.textPmProperty().unbind();
  }

}

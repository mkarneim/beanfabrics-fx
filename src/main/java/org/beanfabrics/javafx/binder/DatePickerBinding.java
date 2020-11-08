package org.beanfabrics.javafx.binder;

import static com.google.common.base.Preconditions.checkNotNull;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.LocalDatePmProperty;
import org.beanfabrics.javafx.validation.GraphicValidationFromTextPmSupport;
import javafx.scene.control.DatePicker;

public class DatePickerBinding implements BnBinding {
  private DatePicker control;
  private LocalDatePmProperty property;
  private GraphicValidationFromTextPmSupport gvs;

  public DatePickerBinding(DatePicker control, IModelProvider modelProvider, Path path) {
    this(control, new LocalDatePmProperty(modelProvider, path));
  }

  public DatePickerBinding(DatePicker control, LocalDatePmProperty property) {
    this.control = checkNotNull(control, "control == null!");
    this.property = checkNotNull(property, "property == null!");

    control.getEditor().textProperty().bindBidirectional(property.textProperty());
    control.promptTextProperty().bind(property.descriptionProperty());
    control.disableProperty().bind(property.isNull().or(property.editableProperty().not()));
    control.converterProperty().bind(property.converterProperty());
    gvs = new GraphicValidationFromTextPmSupport(control);
    gvs.textPmProperty().bind(property);
  }

  @Override
  public void dispose() {

    control.getEditor().textProperty().unbindBidirectional(property.textProperty());
    control.promptTextProperty().unbind();
    control.disableProperty().unbind();
    control.converterProperty().unbind();
    gvs.textPmProperty().unbind();
  }
}

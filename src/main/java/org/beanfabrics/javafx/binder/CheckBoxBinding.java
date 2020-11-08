package org.beanfabrics.javafx.binder;

import static com.google.common.base.Preconditions.checkNotNull;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.BooleanPmProperty;
import org.beanfabrics.javafx.validation.GraphicValidationFromTextPmSupport;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.CheckBox;

public class CheckBoxBinding implements BnBinding {
  private CheckBox control;
  private BooleanPmProperty property;
  private ChangeListener<? super Boolean> graphicFixer;
  private GraphicValidationFromTextPmSupport gvs;

  public CheckBoxBinding(CheckBox control, IModelProvider modelProvider, Path path) {
    this(control, new BooleanPmProperty(modelProvider, path));
  }

  public CheckBoxBinding(CheckBox control, BooleanPmProperty property) {
    this.control = checkNotNull(control, "control == null!");
    this.property = checkNotNull(property, "property == null!");

    control.selectedProperty().bindBidirectional(property.selectedProperty());
    graphicFixer = (obs, oldV, newV) -> {
      fixGraphic(control, newV);
    };
    property.editableProperty().addListener(graphicFixer);
    fixGraphic(control, property.isEditable());

    control.disableProperty().bind(property.isNull().or(property.editableProperty().not()));

    gvs = new GraphicValidationFromTextPmSupport(control);
    gvs.textPmProperty().bind(property);
  }

  private static void fixGraphic(CheckBox control, boolean editable) {
    if (editable) {
      control.setStyle("");
    } else {
      control.setStyle("-fx-opacity: 1");
    }
  }

  @Override
  public void dispose() {
    control.selectedProperty().unbindBidirectional(property.selectedProperty());
    property.editableProperty().removeListener(graphicFixer);
    control.disableProperty().unbind();
    gvs.textPmProperty().unbind();
  }

}

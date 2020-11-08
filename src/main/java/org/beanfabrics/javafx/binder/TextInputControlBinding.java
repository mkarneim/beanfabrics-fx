package org.beanfabrics.javafx.binder;

import static com.google.common.base.Preconditions.checkNotNull;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.TextPmProperty;
import org.beanfabrics.javafx.validation.GraphicValidationFromTextPmSupport;
import org.beanfabrics.model.ITextPM;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextInputControl;

public class TextInputControlBinding implements BnBinding {
  private final TextInputControl control;
  private final TextPmProperty property;
  private final ChangeListener<Boolean> listener = new ChangeListener<Boolean>() {
    @Override
    public void changed(ObservableValue<? extends Boolean> obs, Boolean oldV, Boolean newV) {
      // Focus lost
      if (oldV == true && newV == false) {
        ITextPM pm = property.getPresentationModel();
        if (pm != null) {
          pm.reformat();
        }
      }
    }
  };
  private final GraphicValidationFromTextPmSupport gvs;

  public TextInputControlBinding(TextInputControl control, IModelProvider modelProvider, Path path) {
    this(control, new TextPmProperty(modelProvider, path));
  }

  public TextInputControlBinding(TextInputControl control, TextPmProperty property) {
    this.control = checkNotNull(control, "control == null!");
    this.property = checkNotNull(property, "property == null!");

    control.textProperty().bindBidirectional(property.textProperty());
    control.promptTextProperty().bind(property.descriptionProperty());
    control.editableProperty().bind(property.editableProperty());
    control.focusedProperty().addListener(listener);

    gvs = new GraphicValidationFromTextPmSupport(control);
    gvs.textPmProperty().bind(property);

    control.disableProperty().bind(property.isNull());
  }

  @Override
  public void dispose() {
    control.textProperty().unbindBidirectional(property.textProperty());
    control.promptTextProperty().unbind();
    control.editableProperty().unbind();
    control.focusedProperty().removeListener(listener);
    control.disableProperty().unbind();
  }
}

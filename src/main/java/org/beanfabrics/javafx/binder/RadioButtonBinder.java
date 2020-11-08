package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import com.google.auto.service.AutoService;
import javafx.scene.control.RadioButton;

@AutoService(Binder.class)
public class RadioButtonBinder implements Binder<RadioButton> {

  @Override
  public RadioButtonBinding bind(RadioButton control, IModelProvider modelProvider, Path path) {
    return new RadioButtonBinding(control, modelProvider, path);
  }

}

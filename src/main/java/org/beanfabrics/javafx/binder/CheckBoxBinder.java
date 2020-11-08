package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import com.google.auto.service.AutoService;
import javafx.scene.control.CheckBox;

@AutoService(Binder.class)
public class CheckBoxBinder implements Binder<CheckBox> {

  @Override
  public CheckBoxBinding bind(CheckBox control, IModelProvider modelProvider, Path path) {
    return new CheckBoxBinding(control, modelProvider, path);
  }

}

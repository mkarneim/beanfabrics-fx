package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.TextPmProperty;
import com.google.auto.service.AutoService;
import javafx.scene.control.ComboBox;

@AutoService(Binder.class)
public class ComboBoxBinder implements Binder<ComboBox<String>> {

  @Override
  public ComboBoxBinding bind(ComboBox<String> control, IModelProvider modelProvider, Path path) {
    return new ComboBoxBinding(control, modelProvider, path);
  }

  public ComboBoxBinding bind(ComboBox<String> control, TextPmProperty property) {
    return new ComboBoxBinding(control, property);
  }

}

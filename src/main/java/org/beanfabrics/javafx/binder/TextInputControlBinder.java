package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.TextPmProperty;
import com.google.auto.service.AutoService;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

@AutoService(Binder.class)
public class TextInputControlBinder implements Binder<TextInputControl> {

  @Override
  public BnBinding bind(TextInputControl control, IModelProvider modelProvider, Path path) {
    return new TextInputControlBinding(control, modelProvider, path);
  }

  public BnBinding bind(TextField control, TextPmProperty property) {
    return new TextInputControlBinding(control, property);
  }

}

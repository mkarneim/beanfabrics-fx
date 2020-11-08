package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import com.google.auto.service.AutoService;
import javafx.scene.control.Button;

@AutoService(Binder.class)
public class ButtonBinder implements Binder<Button> {

  @Override
  public ButtonBinding bind(Button control, IModelProvider modelProvider, Path path) {
    return new ButtonBinding(control, modelProvider, path);
  }

}

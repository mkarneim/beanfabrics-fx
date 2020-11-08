package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import com.google.auto.service.AutoService;
import javafx.scene.control.ChoiceBox;


@AutoService(Binder.class)
public class ChoiceBoxBinder implements Binder<ChoiceBox<String>> {

  @Override
  public ChoiceBoxBinding bind(ChoiceBox<String> control, IModelProvider modelProvider, Path path) {
    return new ChoiceBoxBinding(control, modelProvider, path);
  }

}

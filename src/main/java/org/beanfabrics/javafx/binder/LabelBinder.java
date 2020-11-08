package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import com.google.auto.service.AutoService;
import javafx.scene.control.Label;

@AutoService(Binder.class)
public class LabelBinder implements Binder<Label> {
  @Override
  public LabelBinding bind(Label control, IModelProvider modelProvider, Path path) {
    return new LabelBinding(control, modelProvider, path);
  }
}

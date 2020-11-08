package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.TextPmProperty;
import com.google.auto.service.AutoService;
import javafx.beans.property.DoubleProperty;

@AutoService(Binder.class)
public class DoublePropertyBinder implements Binder<DoubleProperty> {
  @Override
  public BnBinding bind(DoubleProperty view, IModelProvider modelProvider, Path path) {
    TextPmProperty pmProperty = new TextPmProperty(modelProvider, path);

    DoubleProperty asDouble = pmProperty.textProperty().asDouble();
    view.bindBidirectional(asDouble);

    return new BnBinding() {
      @Override
      public void dispose() {
        pmProperty.setModelProvider(null);
        pmProperty.setPath(null);
        view.unbindBidirectional(asDouble);
      }
    };
  }
}

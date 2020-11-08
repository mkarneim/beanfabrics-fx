package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.TextPmProperty;
import com.google.auto.service.AutoService;
import javafx.beans.property.FloatProperty;

@AutoService(Binder.class)
public class FloatPropertyBinder implements Binder<FloatProperty> {
  @Override
  public BnBinding bind(FloatProperty view, IModelProvider modelProvider, Path path) {
    TextPmProperty pmProperty = new TextPmProperty(modelProvider, path);

    FloatProperty asFloat = pmProperty.textProperty().asFloat();
    view.bindBidirectional(asFloat);

    return new BnBinding() {
      @Override
      public void dispose() {
        pmProperty.setModelProvider(null);
        pmProperty.setPath(null);
        view.unbindBidirectional(asFloat);
      }
    };
  }
}

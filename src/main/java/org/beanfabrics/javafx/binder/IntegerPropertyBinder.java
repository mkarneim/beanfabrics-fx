package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.TextPmProperty;
import com.google.auto.service.AutoService;
import javafx.beans.property.IntegerProperty;

@AutoService(Binder.class)
public class IntegerPropertyBinder implements Binder<IntegerProperty> {
  @Override
  public BnBinding bind(IntegerProperty view, IModelProvider modelProvider, Path path) {
    TextPmProperty pmProperty = new TextPmProperty(modelProvider, path);

    IntegerProperty asInteger = pmProperty.textProperty().asInteger();
    view.bindBidirectional(asInteger);

    return new BnBinding() {
      @Override
      public void dispose() {
        pmProperty.setModelProvider(null);
        pmProperty.setPath(null);
        view.unbindBidirectional(asInteger);
      }
    };
  }
}

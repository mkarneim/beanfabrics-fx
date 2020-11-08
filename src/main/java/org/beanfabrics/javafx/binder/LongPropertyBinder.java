package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.TextPmProperty;
import com.google.auto.service.AutoService;
import javafx.beans.property.LongProperty;

@AutoService(Binder.class)
public class LongPropertyBinder implements Binder<LongProperty> {
  @Override
  public BnBinding bind(LongProperty view, IModelProvider modelProvider, Path path) {
    TextPmProperty pmProperty = new TextPmProperty(modelProvider, path);

    LongProperty asLong = pmProperty.textProperty().asLong();
    view.bindBidirectional(asLong);

    return new BnBinding() {
      @Override
      public void dispose() {
        pmProperty.setModelProvider(null);
        pmProperty.setPath(null);
        view.unbindBidirectional(asLong);
      }
    };
  }
}

package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.ExtStringProperty;
import org.beanfabrics.javafx.property.TextPmProperty;
import com.google.auto.service.AutoService;
import javafx.beans.property.StringProperty;

@AutoService(Binder.class)
public class StringPropertyBinder implements Binder<StringProperty> {
  @Override
  public BnBinding bind(StringProperty view, IModelProvider modelProvider, Path path) {
    TextPmProperty pmProperty = new TextPmProperty(modelProvider, path);

    ExtStringProperty textProperty = pmProperty.textProperty();
    view.bindBidirectional(textProperty);

    return new BnBinding() {
      @Override
      public void dispose() {
        pmProperty.setModelProvider(null);
        pmProperty.setPath(null);
        view.unbindBidirectional(textProperty);
      }
    };
  }
}

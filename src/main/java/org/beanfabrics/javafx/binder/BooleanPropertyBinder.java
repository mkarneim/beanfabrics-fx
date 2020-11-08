package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.BooleanPmProperty;
import com.google.auto.service.AutoService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;

@AutoService(Binder.class)
public class BooleanPropertyBinder implements Binder<BooleanProperty> {
  @Override
  public BnBinding bind(BooleanProperty view, IModelProvider modelProvider, Path path) {
    BooleanPmProperty pmProperty = new BooleanPmProperty(modelProvider, path);

    Property<Boolean> selectedProperty = pmProperty.selectedProperty();
    view.bindBidirectional(selectedProperty);

    return new BnBinding() {
      @Override
      public void dispose() {
        pmProperty.setModelProvider(null);
        pmProperty.setPath(null);
        view.unbindBidirectional(selectedProperty);
      }
    };
  }
}

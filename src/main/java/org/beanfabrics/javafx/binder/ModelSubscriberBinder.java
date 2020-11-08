package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;

public class ModelSubscriberBinder implements Binder<ModelSubscriber> {

  @Override
  public BnBinding bind(ModelSubscriber subscriber, IModelProvider modelProvider, Path path) {
    return new ModelSubscriberBinding(subscriber, modelProvider, path);
  }

}

package org.beanfabrics.javafx.binder;

import static com.google.common.base.Preconditions.checkNotNull;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;

public class ModelSubscriberBinding implements BnBinding {
  private ModelSubscriber subscriber;

  public ModelSubscriberBinding(ModelSubscriber subscriber, IModelProvider modelProvider, Path path) {
    this.subscriber = checkNotNull(subscriber, "subscriber == null!");
    subscriber.setModelProvider(modelProvider);
    subscriber.setPath(path);
  }

  @Override
  public void dispose() {
    subscriber.setModelProvider(null);
    subscriber.setPath(null);
  }
}

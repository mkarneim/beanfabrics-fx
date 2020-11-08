package org.beanfabrics.javafx.binder;

import javax.annotation.Nullable;
import org.beanfabrics.model.PresentationModel;

@FunctionalInterface
public interface PmChangeListener<PM extends PresentationModel> {

  void changed(@Nullable PM pm);
}

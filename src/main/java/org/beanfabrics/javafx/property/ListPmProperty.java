package org.beanfabrics.javafx.property;

import javax.annotation.Nullable;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.IListPM;
import javafx.beans.property.ObjectProperty;

/**
 * The {@link ListPmProperty} is a Java FX {@link ObjectProperty} that holds a {@link IListPM}
 * instance.
 * <p>
 *
 */
public class ListPmProperty extends PresentationModelProperty<IListPM<?>> {

  public ListPmProperty() {}

  public ListPmProperty(@Nullable IListPM<?> presentationModel) {
    super(presentationModel);
  }

  public ListPmProperty(@Nullable IModelProvider provider, @Nullable Path path) {
    super(provider, path);
  }

}

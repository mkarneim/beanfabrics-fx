package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.PresentationModel;

/**
 * A {@link Binder} is a helper class that can bind the given view component to the
 * {@link PresentationModel} provided by the given {@link IModelProvider} at the given {@link Path}.
 *
 * @param <V>
 */
public interface Binder<V> {

  BnBinding bind(V view, IModelProvider modelProvider, Path path);

}

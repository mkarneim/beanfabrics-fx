package org.beanfabrics.javafx.property;

import javax.annotation.Nullable;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.ITextPM;
import javafx.beans.property.ObjectProperty;

/**
 * The {@link TextPmProperty} is a Java FX {@link ObjectProperty} that holds a {@link ITextPM}
 * instance.
 */
public class TextPmProperty extends ITextPmProperty<ITextPM> {

  public TextPmProperty() {}

  public TextPmProperty(@Nullable ITextPM presentationModel) {
    super(presentationModel);
  }

  public TextPmProperty(@Nullable IModelProvider provider, @Nullable Path path) {
    super(provider, path);
  }

}

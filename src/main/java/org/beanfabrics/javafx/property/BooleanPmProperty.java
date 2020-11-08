package org.beanfabrics.javafx.property;

import java.beans.PropertyChangeListener;
import javax.annotation.Nullable;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.ConversionException;
import org.beanfabrics.model.IBooleanPM;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * The {@link BooleanPmProperty} is a Java FX {@link ObjectProperty} that holds a {@link IBooleanPM}
 * instance.
 */
public class BooleanPmProperty extends ITextPmProperty<IBooleanPM> {

  public BooleanPmProperty() {}

  public BooleanPmProperty(@Nullable IBooleanPM presentationModel) {
    super(presentationModel);
  }

  public BooleanPmProperty(@Nullable IModelProvider provider, @Nullable Path path) {
    super(provider, path);
  }



  //// pmPropertyChangeListener ////
  private PropertyChangeListener pmPropertyChangeListener;

  private PropertyChangeListener getPmPropertyChangeListener() {
    if (pmPropertyChangeListener == null) {
      pmPropertyChangeListener = evt -> {
        if ("text".equals(evt.getPropertyName())) {
          try {
            Boolean value = getPresentationModel().getBoolean();
            setSelected(value != null && value.booleanValue());
          } catch (ConversionException ex) {
            // we can't sync this value, so we ignore it
          }
        }
      };
    }
    return pmPropertyChangeListener;
  }

  @Override
  protected void onChange(@Nullable IBooleanPM oldValue, @Nullable IBooleanPM newValue) {
    super.onChange(oldValue, newValue);
    if (oldValue != null) {
      oldValue.removePropertyChangeListener(getPmPropertyChangeListener());
    }
    if (newValue != null) {
      newValue.addPropertyChangeListener(getPmPropertyChangeListener());
      try {
        Boolean booleanValue = newValue.getBoolean();
        setSelected(booleanValue);
      } catch (ConversionException e) {
        // we can't sync this value, so we ignore it
      }
    }
  }



  //// selected ////

  private BooleanProperty selected;

  public Property<Boolean> selectedProperty() {
    if (selected == null) {
      selected = new SimpleBooleanProperty() {
        @Override
        protected void invalidated() {
          super.invalidated();
          IBooleanPM pm = getPresentationModel();
          if (pm != null) {
            pm.setBoolean(getValue());
          }
        }
      };
    }
    return selected;
  }

  public boolean isSelected() {
    return selectedProperty().getValue();
  }

  public void setSelected(boolean value) {
    selectedProperty().setValue(value);
  }

}

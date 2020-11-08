package org.beanfabrics.javafx.property;

import java.beans.PropertyChangeListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.Nullable;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.ConversionException;
import org.beanfabrics.model.date.ILocalTimePM;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;

/**
 * The {@link LocalTimePmProperty} is a Java FX {@link ObjectProperty} that holds a
 * {@link ILocalTimePM} instance.
 */
public class LocalTimePmProperty extends ITextPmProperty<ILocalTimePM> {

  public LocalTimePmProperty() {}

  public LocalTimePmProperty(@Nullable ILocalTimePM presentationModel) {
    super(presentationModel);
  }

  public LocalTimePmProperty(@Nullable IModelProvider provider, @Nullable Path path) {
    super(provider, path);
  }


  private boolean pending_propertyChange_text = false;

  //// pmPropertyChangeListener ////
  private PropertyChangeListener pmPropertyChangeListener;

  private PropertyChangeListener getPmPropertyChangeListener() {
    if (pmPropertyChangeListener == null) {
      pmPropertyChangeListener = evt -> {
        if ("text".equals(evt.getPropertyName())) {
          pending_propertyChange_text = true;
          try {
            ILocalTimePM pm = getPresentationModel();
            try {
              LocalTime value = pm.getLocalTime();
              setLocalTime(value);
            } catch (ConversionException e) {
              // we can't sync this value, so we ignore it
            }
          } finally {
            pending_propertyChange_text = false;
          }
        }
        if ("formatter".equals(evt.getPropertyName()) || "parser".equals(evt.getPropertyName())) {
          ILocalTimePM pm = getPresentationModel();
          DateTimeFormatter formatter = pm.getFormatter();
          DateTimeFormatter parser = pm.getParser();
          setConverter(new LocalTimeStringConverter(formatter, parser));
        }
      };
    }
    return pmPropertyChangeListener;
  }

  @Override
  protected void onChange(@Nullable ILocalTimePM oldValue, @Nullable ILocalTimePM newValue) {
    super.onChange(oldValue, newValue);
    if (oldValue != null) {
      oldValue.removePropertyChangeListener(getPmPropertyChangeListener());
    }
    if (newValue != null) {
      newValue.addPropertyChangeListener(getPmPropertyChangeListener());
      try {
        setLocalTime(newValue.getLocalTime());
      } catch (ConversionException e) {
        // we can't sync this value, so we ignore it
      }

      DateTimeFormatter formatter = newValue.getFormatter();
      DateTimeFormatter parser = newValue.getParser();
      setConverter(new LocalTimeStringConverter(formatter, parser));
    }
  }



  //// LocalTime ////
  private ObjectProperty<LocalTime> localTime;

  public ObjectProperty<LocalTime> localTimeProperty() {
    if (localTime == null) {
      localTime = new SimpleObjectProperty<LocalTime>(this, "localTime") {
        @Override
        protected void invalidated() {
          if (pending_propertyChange_text) {
            return;
          }
          ILocalTimePM pm = getPresentationModel();
          if (pm != null) {
            LocalTime value = getLocalTime();
            pm.setLocalTime(value);
          }
        }
      };
    }
    return localTime;
  }

  public void setLocalTime(LocalTime localTime) {
    localTimeProperty().set(localTime);
  }

  public LocalTime getLocalTime() {
    return localTimeProperty().get();
  }



  //// converter ////
  private ReadOnlyObjectWrapper<StringConverter<LocalTime>> converter;

  private final ReadOnlyObjectWrapper<StringConverter<LocalTime>> _converter() {
    if (converter == null) {
      converter = new ReadOnlyObjectWrapper<>(this, "converter", null);
    }
    return converter;
  }

  public ReadOnlyObjectProperty<StringConverter<LocalTime>> converterProperty() {
    return _converter().getReadOnlyProperty();
  }

  private void setConverter(StringConverter<LocalTime> value) {
    _converter().set(value);
  }

  public StringConverter<LocalTime> getConverter() {
    return converterProperty().get();
  }

}

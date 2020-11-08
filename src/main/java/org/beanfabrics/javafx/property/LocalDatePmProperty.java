package org.beanfabrics.javafx.property;

import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.annotation.Nullable;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.ConversionException;
import org.beanfabrics.model.date.ILocalDatePM;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

/**
 * The {@link LocalDatePmProperty} is a Java FX {@link ObjectProperty} that holds a
 * {@link ILocalDatePM} instance.
 */
public class LocalDatePmProperty extends ITextPmProperty<ILocalDatePM> {

  public LocalDatePmProperty() {}

  public LocalDatePmProperty(@Nullable ILocalDatePM presentationModel) {
    super(presentationModel);
  }

  public LocalDatePmProperty(@Nullable IModelProvider provider, @Nullable Path path) {
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
            ILocalDatePM pm = getPresentationModel();
            try {
              LocalDate value = pm.getLocalDate();
              setLocalDate(value);
            } catch (ConversionException e) {
              // we can't sync this value, so we ignore it
            }
          } finally {
            pending_propertyChange_text = false;
          }
        }
        if ("formatter".equals(evt.getPropertyName()) || "parser".equals(evt.getPropertyName())) {
          ILocalDatePM pm = getPresentationModel();
          DateTimeFormatter formatter = pm.getFormatter();
          DateTimeFormatter parser = pm.getParser();
          setConverter(new LocalDateStringConverter(formatter, parser));
        }
      };
    }
    return pmPropertyChangeListener;
  }

  @Override
  protected void onChange(@Nullable ILocalDatePM oldValue, @Nullable ILocalDatePM newValue) {
    super.onChange(oldValue, newValue);
    if (oldValue != null) {
      oldValue.removePropertyChangeListener(getPmPropertyChangeListener());
    }
    if (newValue != null) {
      newValue.addPropertyChangeListener(getPmPropertyChangeListener());
      try {
        setLocalDate(newValue.getLocalDate());
      } catch (ConversionException e) {
        // we can't sync this value, so we ignore it
      }

      DateTimeFormatter formatter = newValue.getFormatter();
      DateTimeFormatter parser = newValue.getParser();
      setConverter(new LocalDateStringConverter(formatter, parser));
    }
  }



  //// localDate ////
  private ObjectProperty<LocalDate> localDate;

  public ObjectProperty<LocalDate> localDateProperty() {
    if (localDate == null) {
      localDate = new SimpleObjectProperty<LocalDate>(this, "localDate") {
        @Override
        protected void invalidated() {
          if (pending_propertyChange_text) {
            return;
          }
          ILocalDatePM pm = getPresentationModel();
          if (pm != null) {
            LocalDate value = getLocalDate();
            pm.setLocalDate(value);
          }
        }
      };
    }
    return localDate;
  }

  public void setLocalDate(LocalDate localDate) {
    localDateProperty().set(localDate);
  }

  public LocalDate getLocalDate() {
    return localDateProperty().get();
  }



  //// converter ////
  private ReadOnlyObjectWrapper<StringConverter<LocalDate>> converter;

  private final ReadOnlyObjectWrapper<StringConverter<LocalDate>> _converter() {
    if (converter == null) {
      converter = new ReadOnlyObjectWrapper<>(this, "converter", null);
    }
    return converter;
  }

  public ReadOnlyObjectProperty<StringConverter<LocalDate>> converterProperty() {
    return _converter().getReadOnlyProperty();
  }

  private void setConverter(StringConverter<LocalDate> value) {
    _converter().set(value);
  }

  public StringConverter<LocalDate> getConverter() {
    return converterProperty().get();
  }

}

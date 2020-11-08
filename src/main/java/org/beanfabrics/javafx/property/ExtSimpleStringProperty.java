package org.beanfabrics.javafx.property;

import java.text.NumberFormat;
import java.text.ParseException;
import javax.annotation.Nullable;
import org.beanfabrics.javafx.util.JavaFxUtils;
import com.google.common.base.Strings;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Tooltip;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

public class ExtSimpleStringProperty extends SimpleStringProperty implements ExtStringProperty {
  private static final StringConverter<Number> NUMBER_STRING_CONVERTER = new NumberStringConverter() {
    @Override
    public Number fromString(String value) {
      try {
        if (Strings.isNullOrEmpty(value)) {
          return null;
        } else {
          NumberFormat parser = getNumberFormat();
          return parser.parse(value);
        }
      } catch (ParseException ex) {
        return null;
      }
    }
  };
  private @Nullable DoubleProperty asDouble;
  private @Nullable FloatProperty asFloat;
  private @Nullable IntegerProperty asInteger;
  private @Nullable LongProperty asLong;

  public ExtSimpleStringProperty() {
    super();
  }

  public ExtSimpleStringProperty(Object bean, String name, String initialValue) {
    super(bean, name, initialValue);
  }

  public ExtSimpleStringProperty(Object bean, String name) {
    super(bean, name);
  }

  public ExtSimpleStringProperty(String initialValue) {
    super(initialValue);
  }

  @Override
  public DoubleProperty asDouble() {
    if (asDouble == null) {
      asDouble = new SimpleDoubleProperty();
      Number initialValue = NUMBER_STRING_CONVERTER.fromString(getValue());
      asDouble.setValue(initialValue);
      Bindings.bindBidirectional(this, asDouble, NUMBER_STRING_CONVERTER);
    }
    return asDouble;
  }

  @Override
  public FloatProperty asFloat() {
    if (asFloat == null) {
      asFloat = new SimpleFloatProperty();
      Number initialValue = NUMBER_STRING_CONVERTER.fromString(getValue());
      asFloat.setValue(initialValue);
      Bindings.bindBidirectional(this, asFloat, NUMBER_STRING_CONVERTER);
    }
    return asFloat;
  }

  @Override
  public IntegerProperty asInteger() {
    if (asInteger == null) {
      asInteger = new SimpleIntegerProperty();
      Number initialValue = NUMBER_STRING_CONVERTER.fromString(getValue());
      asInteger.setValue(initialValue);
      Bindings.bindBidirectional(this, asInteger, NUMBER_STRING_CONVERTER);
    }
    return asInteger;
  }

  @Override
  public LongProperty asLong() {
    if (asLong == null) {
      asLong = new SimpleLongProperty();
      Number initialValue = NUMBER_STRING_CONVERTER.fromString(getValue());
      asLong.setValue(initialValue);
      Bindings.bindBidirectional(this, asLong, NUMBER_STRING_CONVERTER);
    }
    return asLong;
  }

  @Override
  public ObjectBinding<Tooltip> asTooltip() {
    return JavaFxUtils.map(this, Tooltip::new);
  }
}

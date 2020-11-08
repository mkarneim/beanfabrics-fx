package org.beanfabrics.javafx.property;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.Property;
import javafx.beans.value.WritableStringValue;
import javafx.scene.control.Tooltip;

public interface ExtStringProperty extends Property<String>, WritableStringValue {
  DoubleProperty asDouble();

  FloatProperty asFloat();

  IntegerProperty asInteger();

  LongProperty asLong();

  ObjectBinding<Tooltip> asTooltip();
}

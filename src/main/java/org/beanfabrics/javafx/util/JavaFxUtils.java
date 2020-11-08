package org.beanfabrics.javafx.util;

import java.util.concurrent.Callable;
import java.util.function.Function;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;

public class JavaFxUtils {
  public static <T, R> ObjectBinding<R> map(ObservableValue<T> self, Function<T, R> function) {
    Callable<R> func = () -> {
      T value = self.getValue();
      return function.apply(value);
    };
    return Bindings.createObjectBinding(func, self);
  }
}

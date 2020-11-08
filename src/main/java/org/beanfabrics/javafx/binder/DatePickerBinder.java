package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import com.google.auto.service.AutoService;
import javafx.scene.control.DatePicker;

@AutoService(Binder.class)
public class DatePickerBinder implements Binder<DatePicker> {

  @Override
  public DatePickerBinding bind(DatePicker control, IModelProvider modelProvider, Path path) {
    return new DatePickerBinding(control, modelProvider, path);
  }

}

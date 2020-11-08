package org.beanfabrics.model.date;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.beanfabrics.model.ITextPM;

public interface ILocalTimePM extends ITextPM {

  LocalTime getLocalTime();

  void setLocalTime(LocalTime value);

  DateTimeFormatter getFormatter();

  DateTimeFormatter getParser();

}

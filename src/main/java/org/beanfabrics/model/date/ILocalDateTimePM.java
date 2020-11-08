package org.beanfabrics.model.date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.beanfabrics.model.ITextPM;

public interface ILocalDateTimePM extends ITextPM {

  LocalDateTime getLocalDateTime();

  void setLocalDateTime(LocalDateTime value);

  DateTimeFormatter getFormatter();

  DateTimeFormatter getParser();

}

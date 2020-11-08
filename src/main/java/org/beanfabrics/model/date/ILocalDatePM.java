package org.beanfabrics.model.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.beanfabrics.model.ITextPM;

public interface ILocalDatePM extends ITextPM {

  LocalDate getLocalDate();

  void setLocalDate(LocalDate value);

  DateTimeFormatter getFormatter();

  DateTimeFormatter getParser();

}

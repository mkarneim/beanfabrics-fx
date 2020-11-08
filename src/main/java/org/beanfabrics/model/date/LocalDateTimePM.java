package org.beanfabrics.model.date;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.DecimalStyle;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import org.beanfabrics.model.ConversionException;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.util.ResourceBundleFactory;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * The {@link LocalDateTimePM} is a {@link PresentationModel} that contains a {@link LocalDateTime} value.
 * <p>
 * The {@link DateTimeFormatter}s used for formatting and parsing can be set by calling
 * {@link #setFormatter(DateTimeFormatter, DateTimeFormatter)}. The default format is {@link Locale} dependent.
 *
 */
public class LocalDateTimePM extends TextPM implements ILocalDateTimePM {

  protected static final String KEY_MESSAGE_INVALID_DATE = "message.invalidDate";
  private final ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(LocalDateTimePM.class);

  private static DateTimeFormatter DEFAULT_FORMATTER;
  private static DateTimeFormatter DEFAULT_PARSER;

  /**
   * Returns the {@link DateTimeFormatter} that is used as {@link #getFormatter() formatter} by newly created
   * {@link LocalDateTimePM} instances.
   *
   * @return the {@link DateTimeFormatter}
   */
  public static synchronized DateTimeFormatter getDefaultFormatter() {
    if (DEFAULT_FORMATTER == null) {
      Locale locale = Locale.getDefault();
      Chronology chronology = Chronology.ofLocale(Locale.getDefault(Locale.Category.FORMAT));
      FormatStyle dateStyle = FormatStyle.MEDIUM;
      FormatStyle timeStyle = FormatStyle.MEDIUM;

      String pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(dateStyle, timeStyle, chronology, locale);
      return DateTimeFormatter.ofPattern(pattern);
    }
    return DEFAULT_FORMATTER;
  }

  /**
   * Sets the {@link DateTimeFormatter} that is used as {@link #getFormatter() formatter} by newly created
   * {@link LocalDateTimePM} instances.
   *
   * @param formatter
   */
  public static synchronized void setDefaultFormatter(DateTimeFormatter formatter) {
    DEFAULT_FORMATTER = formatter;
  }

  /**
   * Returns the {@link DateTimeFormatter} that is used as {@link #getParser() parser} by newly created
   * {@link LocalDateTimePM} instances.
   *
   * @return the {@link DateTimeFormatter}
   */
  public static synchronized DateTimeFormatter getDefaultParser() {
    if (DEFAULT_PARSER == null) {
      Locale locale = Locale.getDefault();
      Chronology chronology = Chronology.ofLocale(Locale.getDefault(Locale.Category.FORMAT));
      FormatStyle dateStyle = FormatStyle.SHORT;
      FormatStyle timeStyle = FormatStyle.MEDIUM;

      String pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(dateStyle, timeStyle, chronology, locale);
      // @formatter:off
      return new DateTimeFormatterBuilder()
          .parseLenient()
          .appendPattern(pattern)
          .toFormatter()
          .withChronology(chronology)
          .withDecimalStyle(DecimalStyle.of(locale));
      // @formatter:on
    }
    return DEFAULT_PARSER;
  }

  /**
   * Sets the {@link DateTimeFormatter} that is used as {@link #getParser() parser} by newly created
   * {@link LocalDateTimePM} instances.
   *
   * @param formatter
   */
  public static synchronized void setDefaultParser(DateTimeFormatter formatter) {
    DEFAULT_PARSER = formatter;
  }

  /////////////////////

  private DateTimeFormatter formatter;
  private DateTimeFormatter parser;

  /**
   * Constructs a {@link LocalDateTimePM} using the default format.
   *
   */
  public LocalDateTimePM() {
    formatter = getDefaultFormatter();
    parser = getDefaultParser();

    // Please note: to disable default validation rules just call getValidator().clear();
    getValidator().add(new DateValidationRule());
  }


  @Override
  public DateTimeFormatter getFormatter() {
    return formatter;
  }

  public void setFormatter(DateTimeFormatter newFormatter) {
    setFormatter(newFormatter, newFormatter);
  }

  public void setFormatter(DateTimeFormatter newFormatter, DateTimeFormatter newParser) {
    _setFormatter(newFormatter);
    setParser(newParser);
  }

  private void _setFormatter(DateTimeFormatter newFormatter) {
    DateTimeFormatter oldFormatter = formatter;
    if (oldFormatter == newFormatter) {
      return;
    }
    boolean doReformat;
    LocalDateTime oldValue = null;
    try {
      oldValue = getLocalDateTime();
      doReformat = true;
    } catch (ConversionException ex) {
      doReformat = false;
    }
    formatter = newFormatter;
    getPropertyChangeSupport().firePropertyChange("formatter", oldFormatter, newFormatter); //$NON-NLS-1$
    if (doReformat) {
      setLocalDateTime(oldValue);
    }
  }

  @Override
  public DateTimeFormatter getParser() {
    return parser;
  }

  private void setParser(DateTimeFormatter newParser) {
    DateTimeFormatter oldParser = parser;
    if (oldParser == newParser) {
      return;
    }
    parser = newParser;
    getPropertyChangeSupport().firePropertyChange("parser", oldParser, newParser); //$NON-NLS-1$
    revalidate();
  }

  @Override
  public void reformat() {
    if (!isEmpty() && isValid()) {
      setLocalDateTime(getLocalDateTime());
    }
  }

  public LocalDateTime getLocalDateTime() throws ConversionException {
    if (isEmpty()) {
      return null;
    }
    String str = getText();
    try {
      return LocalDateTime.parse(str, parser);
    } catch (DateTimeParseException e) {
      throw new ConversionException(e);
    }
  }

  public void setLocalDateTime(LocalDateTime value) {
    if (value == null) {
      setText(null);
    } else {
      String str = value.format(formatter);
      setText(str);
    }
  }

  public Date getDate() throws ConversionException {
    if (isEmpty()) {
      return null;
    }
    LocalDateTime localDateTime = getLocalDateTime();
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  public void setDate(Date date) {
    if (date == null) {
      setText(null);
    } else {
      LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
      setLocalDateTime(localDateTime);
    }
  }

  /** {@inheritDoc} */
  @Override
  public Comparable<?> getComparable() {
    return new LocalDateComparable();
  }

  /**
   * The {@link LocalDateComparable} delegates the comparison to the PM's date value.
   *
   */
  private class LocalDateComparable extends TextComparable {
    private Instant instant;

    /**
     * Constructs a {@link LocalDateComparable}.
     */
    public LocalDateComparable() {
      if (!isEmpty()) {
        try {
          LocalDateTime date = getLocalDateTime();
          instant = date.toInstant(ZoneOffset.UTC);
        } catch (ConversionException ex) {
          // ignore
        }
      }
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(Object o) {
      if (o == null) {
        throw new IllegalArgumentException("o == null");
      }
      if (!(o instanceof LocalDateComparable)) {
        throw new IllegalArgumentException("incompatible comparable class");
      }
      LocalDateComparable oc = (LocalDateComparable) o;
      if (instant == null) {
        if (oc.instant == null) {
          return super.compareTo(o);
        } else {
          return -1;
        }
      } else {
        if (oc.instant == null) {
          return 1;
        } else {
          return instant.compareTo(oc.instant);
        }
      }
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!super.equals(o)) {
        return false;
      }
      if (o == null) {
        return false;
      }
      if (o.getClass() != getClass()) {
        return false;
      }
      LocalDateComparable castedObj = (LocalDateComparable) o;
      return ((instant == null ? castedObj.instant == null : instant.equals(castedObj.instant)));
    }

    @Override
    public int hashCode() {
      return instant.hashCode();
    }
  }

  /**
   * This rule evaluates to invalid if the PM's value can't be converted into a date.
   *
   */
  public class DateValidationRule implements ValidationRule {
    /** {@inheritDoc} */
    public ValidationState validate() {
      if (isEmpty()) {
        return null;
      }
      try {
        getDate(); // try to convert to date
        return null;
      } catch (ConversionException ex) {
        String message = resourceBundle.getString(KEY_MESSAGE_INVALID_DATE);
        return new ValidationState(message);
      }
    }
  }

}

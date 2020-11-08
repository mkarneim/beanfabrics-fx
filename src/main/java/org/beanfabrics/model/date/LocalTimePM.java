package org.beanfabrics.model.date;

import java.time.LocalTime;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.DecimalStyle;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import org.beanfabrics.model.ConversionException;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.util.ResourceBundleFactory;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * The {@link LocalTimePM} is a {@link PresentationModel} that contains a {@link LocalTime} value.
 * <p>
 * The {@link DateTimeFormatter}s used for formatting and parsing can be set by calling
 * {@link #setFormatter(DateTimeFormatter, DateTimeFormatter)}. The default format is {@link Locale}
 * dependent.
 *
 */
public class LocalTimePM extends TextPM implements ILocalTimePM {

  protected static final String KEY_MESSAGE_INVALID_TIME = "message.invalidTime";
  private final ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(LocalTimePM.class);

  private static DateTimeFormatter DEFAULT_FORMATTER;
  private static DateTimeFormatter DEFAULT_PARSER;

  /**
   * Returns the {@link DateTimeFormatter} that is used as {@link #getFormatter() formatter} by newly
   * created {@link LocalTimePM} instances.
   *
   * @return the {@link DateTimeFormatter}
   */
  public static synchronized DateTimeFormatter getDefaultFormatter() {
    if (DEFAULT_FORMATTER == null) {
      Locale locale = Locale.getDefault();
      Chronology chronology = Chronology.ofLocale(Locale.getDefault(Locale.Category.FORMAT));
      FormatStyle dateStyle = null;
      FormatStyle timeStyle = FormatStyle.MEDIUM;

      String pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(dateStyle, timeStyle, chronology, locale);
      return DateTimeFormatter.ofPattern(pattern);
    }
    return DEFAULT_FORMATTER;
  }

  /**
   * Sets the {@link DateTimeFormatter} that is used as {@link #getFormatter() formatter} by newly
   * created {@link LocalTimePM} instances.
   *
   * @param formatter
   */
  public static synchronized void setDefaultFormatter(DateTimeFormatter formatter) {
    DEFAULT_FORMATTER = formatter;
  }

  /**
   * Returns the {@link DateTimeFormatter} that is used as {@link #getParser() parser} by newly
   * created {@link LocalTimePM} instances.
   *
   * @return the {@link DateTimeFormatter}
   */
  public static synchronized DateTimeFormatter getDefaultParser() {
    if (DEFAULT_PARSER == null) {
      Locale locale = Locale.getDefault();
      Chronology chronology = Chronology.ofLocale(Locale.getDefault(Locale.Category.FORMAT));
      FormatStyle dateStyle = null;
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
   * {@link LocalTimePM} instances.
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
   * Constructs a {@link LocalTimePM} using the default format.
   *
   */
  public LocalTimePM() {
    formatter = getDefaultFormatter();
    parser = getDefaultParser();

    // Please note: to disable default validation rules just call getValidator().clear();
    getValidator().add(new TimeValidationRule());
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
    LocalTime oldValue = null;
    try {
      oldValue = getLocalTime();
      doReformat = true;
    } catch (ConversionException ex) {
      doReformat = false;
    }
    formatter = newFormatter;
    getPropertyChangeSupport().firePropertyChange("formatter", oldFormatter, newFormatter); //$NON-NLS-1$
    if (doReformat) {
      setLocalTime(oldValue);
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
      setLocalTime(getLocalTime());
    }
  }

  @Override
  public LocalTime getLocalTime() throws ConversionException {
    if (isEmpty()) {
      return null;
    }
    String str = getText();
    try {
      return LocalTime.parse(str, parser);
    } catch (DateTimeParseException e) {
      throw new ConversionException(e);
    }
  }

  @Override
  public void setLocalTime(LocalTime value) {
    if (value == null) {
      setText(null);
    } else {
      String str = value.format(formatter);
      setText(str);
    }
  }

  /** {@inheritDoc} */
  @Override
  public Comparable<?> getComparable() {
    return new LocalTimeComparable();
  }

  /**
   * The {@link LocalTimeComparable} delegates the comparison to the PM's time value.
   *
   */
  private class LocalTimeComparable extends TextComparable {

    private LocalTime value;

    /**
     * Constructs a {@link LocalTimeComparable}.
     */
    public LocalTimeComparable() {
      if (!isEmpty()) {
        try {
          value = getLocalTime();
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
      if (!(o instanceof LocalTimeComparable)) {
        throw new IllegalArgumentException("incompatible comparable class");
      }
      LocalTimeComparable oc = (LocalTimeComparable) o;
      if (value == null) {
        if (oc.value == null) {
          return super.compareTo(o);
        } else {
          return -1;
        }
      } else {
        if (oc.value == null) {
          return 1;
        } else {
          return value.compareTo(oc.value);
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
      LocalTimeComparable castedObj = (LocalTimeComparable) o;
      return value == null ? castedObj.value == null : value.equals(castedObj.value);
    }

    @Override
    public int hashCode() {
      return value.hashCode();
    }
  }

  /**
   * This rule evaluates to invalid if the PM's value can't be converted into a time.
   *
   */
  public class TimeValidationRule implements ValidationRule {
    /** {@inheritDoc} */
    @Override
    public ValidationState validate() {
      if (isEmpty()) {
        return null;
      }
      try {
        getLocalTime(); // try to convert to a local time
        return null;
      } catch (ConversionException ex) {
        String message = resourceBundle.getString(KEY_MESSAGE_INVALID_TIME);
        return new ValidationState(message);
      }
    }
  }


}

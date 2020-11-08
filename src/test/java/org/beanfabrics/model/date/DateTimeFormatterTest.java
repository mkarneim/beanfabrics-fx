package org.beanfabrics.model.date;

import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DecimalStyle;
import java.time.format.FormatStyle;
import java.time.format.SignStyle;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;

import javafx.scene.control.DatePicker;
import javafx.util.converter.LocalDateTimeStringConverter;
import org.beanfabrics.testenv.TestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DateTimeFormatterTest extends TestBase {

  private static final Locale DEFAULT = Locale.getDefault();

  @Before
  public void before() {
    Locale.setDefault(DEFAULT);
  }

  @After
  public void after() {
    Locale.setDefault(DEFAULT);
  }

  ////////////////////////////////////////////////////////////////////////////////////////
  //// Gregorian Calendar (non-proleptic)

  @Test
  public void test_format_year_1_in_era_using_pattern() {
    // Given:
    Locale.setDefault(Locale.US);
    DateTimeFormatter underTest = DateTimeFormatter.ofPattern("dd.MM.yyyy G");
    LocalDate date = LocalDate.of(1, 1, 1);
    // When:
    String act = underTest.format(date);

    // Then:
    assertThat(act).isEqualTo("01.01.0001 AD");
  }

  @Test
  public void test_format_year_1_in_era_using_builder() {
    // Given:
    Locale.setDefault(Locale.US);
    // @formatter:off
    DateTimeFormatter underTest = new DateTimeFormatterBuilder()
      .parseLenient()
      .appendValue(ChronoField.DAY_OF_MONTH, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.MONTH_OF_YEAR, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.YEAR_OF_ERA, 4, 9, SignStyle.NORMAL)
      .appendLiteral(' ')
      .appendText(ChronoField.ERA, TextStyle.SHORT)
      .toFormatter();
    // @formatter:on
    LocalDate date = LocalDate.of(1, 1, 1);
    // When:
    String act = underTest.format(date);

    // Then:
    assertThat(act).isEqualTo("01.01.0001 AD");
  }

  @Test
  public void test_format_year_0_in_era_using_pattern() {
    // Given:
    Locale.setDefault(Locale.US);
    DateTimeFormatter underTest = DateTimeFormatter.ofPattern("dd.MM.yyyy G");
    LocalDate date = LocalDate.of(0, 1, 1);
    // When:
    String act = underTest.format(date);

    // Then:
    assertThat(act).isEqualTo("01.01.0001 BC");
  }

  @Test
  public void test_format_year_0_in_era_using_builder() {
    // Given:
    Locale.setDefault(Locale.US);
    // @formatter:off
    DateTimeFormatter underTest = new DateTimeFormatterBuilder()
      .parseLenient()
      .appendValue(ChronoField.DAY_OF_MONTH, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.MONTH_OF_YEAR, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.YEAR_OF_ERA, 4, 9, SignStyle.NORMAL)
      .appendLiteral(' ')
      .appendText(ChronoField.ERA, TextStyle.SHORT)
      .toFormatter();
    // @formatter:on
    LocalDate date = LocalDate.of(0, 1, 1);
    // When:
    String act = underTest.format(date);

    // Then:
    assertThat(act).isEqualTo("01.01.0001 BC");
  }

  @Test
  public void test_format_negative_year_in_era_using_pattern() {
    // Given:
    Locale.setDefault(Locale.US);
    DateTimeFormatter underTest = DateTimeFormatter.ofPattern("dd.MM.yyyy G");
    LocalDate date = LocalDate.of(-20, 01, 01);
    // When:
    String act = underTest.format(date);

    // Then:
    assertThat(act).isEqualTo("01.01.0021 BC");
  }

  @Test
  public void test_format_negative_year_in_era_using_builder() {
    // Given:
    Locale.setDefault(Locale.US);
    // @formatter:off
    DateTimeFormatter underTest = new DateTimeFormatterBuilder()
      .parseLenient()
      .appendValue(ChronoField.DAY_OF_MONTH, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.MONTH_OF_YEAR, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.YEAR_OF_ERA, 4, 9, SignStyle.NORMAL)
      .appendLiteral(' ')
      .appendText(ChronoField.ERA, TextStyle.SHORT)
      .toFormatter();
    // @formatter:on
    LocalDate date = LocalDate.of(-20, 01, 01);
    // When:
    String act = underTest.format(date);

    // Then:
    assertThat(act).isEqualTo("01.01.0021 BC");
  }

  ////////////////////////////////////////////////////////////////////////////////////////
  //// Proleptic Gregorian Calendar:

  @Test
  public void test_format_proleptic_year_1_using_pattern() {
    // Given:
    Locale.setDefault(Locale.US);
    DateTimeFormatter underTest = DateTimeFormatter.ofPattern("dd.MM.uuuu");
    LocalDate date = LocalDate.of(1, 1, 1);
    // When:
    String act = underTest.format(date);

    // Then:
    assertThat(act).isEqualTo("01.01.0001");
  }

  @Test
  public void test_format_proleptic_year_1_using_builder() {
    // Given:
    Locale.setDefault(Locale.US);
    // @formatter:off
    DateTimeFormatter underTest = new DateTimeFormatterBuilder()
      .parseLenient()
      .appendValue(ChronoField.DAY_OF_MONTH, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.MONTH_OF_YEAR, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.YEAR, 4, 9, SignStyle.NORMAL)
      .toFormatter();
    // @formatter:on
    LocalDate date = LocalDate.of(1, 1, 1);
    // When:
    String act = underTest.format(date);

    // Then:
    assertThat(act).isEqualTo("01.01.0001");
  }

  @Test
  public void test_format_proleptic_year_0_using_pattern() {
    // Given:
    Locale.setDefault(Locale.US);
    DateTimeFormatter underTest = DateTimeFormatter.ofPattern("dd.MM.uuuu");
    LocalDate date = LocalDate.of(0, 1, 1);
    // When:
    String act = underTest.format(date);

    // Then:
    assertThat(act).isEqualTo("01.01.0000");
  }

  @Test
  public void test_format_proleptic_year_0_using_builder() {
    // Given:
    Locale.setDefault(Locale.US);
    // @formatter:off
    DateTimeFormatter underTest = new DateTimeFormatterBuilder()
      .parseLenient()
      .appendValue(ChronoField.DAY_OF_MONTH, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.MONTH_OF_YEAR, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.YEAR, 4, 9, SignStyle.NORMAL)
      .toFormatter();
    // @formatter:on
    LocalDate date = LocalDate.of(0, 1, 1);
    // When:
    String act = underTest.format(date);

    // Then:
    assertThat(act).isEqualTo("01.01.0000");
  }

  @Test
  public void test_format_negative_proleptic_year_in_using_pattern() {
    // Given:
    Locale.setDefault(Locale.US);
    DateTimeFormatter underTest = DateTimeFormatter.ofPattern("dd.MM.uuuu");
    LocalDate date = LocalDate.of(-20, 01, 01);
    // When:
    String act = underTest.format(date);

    // Then:
    assertThat(act).isEqualTo("01.01.-0020");
  }

  @Test
  public void test_format_negative_proleptic_year_using_builder() {
    // Given:
    // @formatter:off
    DateTimeFormatter underTest = new DateTimeFormatterBuilder()
      .parseLenient()
      .appendValue(ChronoField.DAY_OF_MONTH, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.MONTH_OF_YEAR, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.YEAR, 4, 9, SignStyle.NORMAL)
      .toFormatter();
    // @formatter:on
    LocalDate date = LocalDate.of(-20, 01, 01);
    // When:
    String act = underTest.format(date);

    // Then:
    assertThat(act).isEqualTo("01.01.-0020");
  }


  ////////////////////////////////////////////////////////////////////////////////////////
  //// DatePicker

  @Test
  public void test_parse_date_with_four_digits_year_using_DatePicker_defaults() {
    // Given:
    Locale.setDefault(Locale.GERMANY);
    DateTimeFormatter underTest = getDatePickerDefaultParser();

    // When:
    LocalDate act = LocalDate.parse("01.01.2000", underTest);

    // Then:
    assertThat(act).isEqualTo(LocalDate.parse("2000-01-01"));
  }

  @Test
  public void test_parse_date_with_two_digits_year_using_DatePicker_defaults() {
    // Given:
    Locale.setDefault(Locale.GERMANY);
    DateTimeFormatter underTest = getDatePickerDefaultParser();

    // When:
    LocalDate act = LocalDate.parse("01.01.20", underTest);

    // Then:
    assertThat(act).isEqualTo(LocalDate.parse("2020-01-01"));
  }

  @Test
  public void test_parse_date_with_two_digits_year_99_using_DatePicker_defaults() {
    // Given:
    Locale.setDefault(Locale.GERMANY);
    DateTimeFormatter underTest = getDatePickerDefaultParser();

    // When:
    LocalDate act = LocalDate.parse("01.01.99", underTest);

    // Then:
    // we would prefer 1999, but this is how DatePicker's default parser works!
    assertThat(act).isEqualTo(LocalDate.parse("2099-01-01"));
  }

  private DateTimeFormatter getDatePickerDefaultParser() {
    Locale locale = Locale.getDefault();
    Chronology chronology = Chronology.ofLocale(Locale.getDefault(Locale.Category.FORMAT));
    FormatStyle dateStyle = FormatStyle.SHORT;
    FormatStyle timeStyle = null;
    return getDatePickerDefaultParser(dateStyle, timeStyle, chronology, locale);
  }

  /**
   * This method is copied from {@link LocalDateTimeStringConverter.LdtConverter#getDefaultParser}, which is indirectly
   * used by the {@link DatePicker}.
   *
   * @param dateStyle
   * @param timeStyle
   * @param chronology
   * @param locale
   * @return
   */
  private DateTimeFormatter getDatePickerDefaultParser(FormatStyle dateStyle, FormatStyle timeStyle,
      Chronology chronology, Locale locale) {

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

  ////////////////////////////////////////////////////////////////////////////////////////
  //// Some custom formats

  @Test
  public void test_parse_1() {
    // Given:
    // @formatter:off
    DateTimeFormatter underTest = new DateTimeFormatterBuilder()
      .parseLenient()
      .appendValue(ChronoField.DAY_OF_MONTH, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.MONTH_OF_YEAR, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.YEAR, 2)
      .toFormatter();
    // @formatter:on
    // When:
    LocalDate act = LocalDate.parse("01.01.2001", underTest);

    // Then:
    assertThat(act).isEqualTo(LocalDate.parse("2001-01-01"));
  }

  @Test
  public void test_parse_2() {
    // Given:
    // @formatter:off
    DateTimeFormatter underTest = new DateTimeFormatterBuilder()
      .parseLenient()
      .appendValue(ChronoField.DAY_OF_MONTH, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.MONTH_OF_YEAR, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.YEAR, 2)
      .toFormatter();
    // @formatter:on
    // When:
    LocalDate act = LocalDate.parse("01.01.20", underTest);

    // Then:
    assertThat(act).isEqualTo(LocalDate.parse("0020-01-01"));
  }

  @Test
  public void test_parse_3() {
    // Given:
    // @formatter:off
    DateTimeFormatter underTest = new DateTimeFormatterBuilder()
      .parseLenient()
      .appendValue(ChronoField.DAY_OF_MONTH, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.MONTH_OF_YEAR, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.YEAR, 4)
      .toFormatter();
    // @formatter:on
    // When:
    LocalDate act = LocalDate.parse("01.01.20", underTest);

    // Then:
    assertThat(act).isEqualTo(LocalDate.parse("0020-01-01"));
  }

  @Test
  public void test_parse_4() {
    // Given:
    // @formatter:off
    DateTimeFormatter underTest = new DateTimeFormatterBuilder()
      .parseLenient()
      .appendValue(ChronoField.DAY_OF_MONTH, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.MONTH_OF_YEAR, 2)
      .appendLiteral('.')
      .appendValueReduced(ChronoField.YEAR, 2, 2, LocalDate.of(2000, 1, 1))
      .toFormatter();
    // @formatter:on
    // When:
    LocalDate act = LocalDate.parse("01.01.20", underTest);

    // Then:
    assertThat(act).isEqualTo(LocalDate.parse("2020-01-01"));
  }

  @Test
  public void test_parse_5() {
    // Given:
    // @formatter:off
    DateTimeFormatter underTest = new DateTimeFormatterBuilder()
      .parseLenient()
      .appendValue(ChronoField.DAY_OF_MONTH, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.MONTH_OF_YEAR, 2)
      .appendLiteral('.')
      .appendValueReduced(ChronoField.YEAR, 2, 2, LocalDate.of(1940, 1, 1))
      .toFormatter();
    // @formatter:on
    // When:
    LocalDate act = LocalDate.parse("01.01.20", underTest);

    // Then:
    assertThat(act).isEqualTo(LocalDate.parse("2020-01-01"));
  }

  @Test
  public void test_parse_6() {
    // Given:
    // @formatter:off
    DateTimeFormatter underTest = new DateTimeFormatterBuilder()
      .parseLenient()
      .appendValue(ChronoField.DAY_OF_MONTH, 2)
      .appendLiteral('.')
      .appendValue(ChronoField.MONTH_OF_YEAR, 2)
      .appendLiteral('.')
      .appendValueReduced(ChronoField.YEAR, 2, 2, LocalDate.of(1940, 1, 1))
      .toFormatter();
    // @formatter:on
    // When:
    LocalDate act = LocalDate.parse("01.01.70", underTest);

    // Then:
    assertThat(act).isEqualTo(LocalDate.parse("1970-01-01"));
  }

}

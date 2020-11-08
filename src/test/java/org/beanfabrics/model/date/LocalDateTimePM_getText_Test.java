package org.beanfabrics.model.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import org.beanfabrics.testenv.TestBase;
import org.junit.Before;
import org.junit.Test;

public class LocalDateTimePM_getText_Test extends TestBase {
  private static final Locale DEFAULT = Locale.getDefault();

  @Before
  public void before() {
    Locale.setDefault(Locale.GERMANY);
  }

  @Before
  public void after() {
    Locale.setDefault(DEFAULT);
  }

  LocalDateTimePM underTest = new LocalDateTimePM();

  @Test
  public void test_getText_for_year_with_four_digits() {
    // Given:
    underTest.setLocalDateTime(LocalDateTime.of(2001, 01, 01, 0, 0));

    // When:
    String act = underTest.getText();

    // Then:
    assertThat(act).isEqualTo("01.01.2001 00:00:00");
  }

  @Test
  public void test_getText_for_year_with_two_digits() {
    // Give
    underTest.setLocalDateTime(LocalDateTime.of(20, 01, 01, 0, 0));

    // When:
    String act = underTest.getText();

    // Then:
    assertThat(act).isEqualTo("01.01.0020 00:00:00");
  }

  @Test
  public void test_getText_for_year_with_four_digits_having_value_2020_and_curstom_formatter() {
    // Given:
    // @formatter:off
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .parseLenient()
        .appendValue(ChronoField.DAY_OF_MONTH, 2)
        .appendLiteral('.')
        .appendValue(ChronoField.MONTH_OF_YEAR, 2)
        .appendLiteral('.')
        .appendValueReduced(ChronoField.YEAR, 2, 2, LocalDate.of(1940, 1, 1))
        .appendLiteral(' ')
        .appendValue(ChronoField.HOUR_OF_DAY, 2)
        .appendLiteral(':')
        .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
        .appendLiteral(':')
        .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
        .toFormatter();
    // @formatter:on
    underTest.setFormatter(formatter, underTest.getParser());
    underTest.setLocalDateTime(LocalDateTime.of(2020, 01, 01, 0, 0));

    // When:
    String act = underTest.getText();

    // Then:
    assertThat(act).isEqualTo("01.01.20 00:00:00");
  }

  @Test
  public void test_getText_for_year_with_four_digits_having_value_1999_and_curstom_formatter() {
    // Given:
    // @formatter:off
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .parseLenient()
        .appendValue(ChronoField.DAY_OF_MONTH, 2)
        .appendLiteral('.')
        .appendValue(ChronoField.MONTH_OF_YEAR, 2)
        .appendLiteral('.')
        .appendValueReduced(ChronoField.YEAR, 2, 2, LocalDate.of(1940, 1, 1))
        .appendLiteral(' ')
        .appendValue(ChronoField.HOUR_OF_DAY, 2)
        .appendLiteral(':')
        .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
        .appendLiteral(':')
        .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
        .toFormatter();
    // @formatter:on
    underTest.setFormatter(formatter, underTest.getParser());
    underTest.setLocalDateTime(LocalDateTime.of(1999, 01, 01, 0, 0));

    // When:
    String act = underTest.getText();

    // Then:
    assertThat(act).isEqualTo("01.01.99 00:00:00");
  }

}

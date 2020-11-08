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

public class LocalDateTimePM_getLocalDateTime_Test extends TestBase {
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
  public void test_getLocalDateTime_for_year_with_four_digits() {
    // Given:
    underTest.setText("1.1.2001 00:00:00");

    // When:
    LocalDateTime act = underTest.getLocalDateTime();

    // Then:
    assertThat(act).isEqualTo(LocalDateTime.of(2001, 01, 01, 00, 00));
  }

  @Test
  public void test_getLocalDateTime_for_year_with_two_digits_having_value_20() {
    // Give
    underTest.setText("1.1.20 00:00:00");

    // When:
    LocalDateTime act = underTest.getLocalDateTime();

    // Then:
    assertThat(act).isEqualTo(LocalDateTime.of(2020, 01, 01, 00, 00));
  }

  @Test
  public void test_getLocalDateTime_for_year_with_two_digits_having_value_99() {
    // Give
    underTest.setText("1.1.99 00:00:00");

    // When:
    LocalDateTime act = underTest.getLocalDateTime();

    // Then:
    assertThat(act).isEqualTo(LocalDateTime.of(2099, 01, 01, 00, 00));
  }

  @Test
  public void test_getLocalDateTime_for_year_with_two_digits_having_value_99_and_curstom_parser() {
    // Given:
    // @formatter:off
    DateTimeFormatter parser = new DateTimeFormatterBuilder()
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
    underTest.setFormatter(underTest.getFormatter(), parser);
    underTest.setText("1.1.99 00:00:00");

    // When:
    LocalDateTime act = underTest.getLocalDateTime();

    // Then:
    assertThat(act).isEqualTo(LocalDateTime.of(1999, 01, 01, 00, 00));
  }


}

package org.beanfabrics.model.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;
import org.beanfabrics.testenv.TestBase;
import org.junit.Before;
import org.junit.Test;

public class LocalDatePM_getText_Test extends TestBase {
  private static final Locale DEFAULT = Locale.getDefault();

  @Override
  @Before
  public void before() {
    Locale.setDefault(Locale.GERMANY);
  }

  @Before
  public void after() {
    Locale.setDefault(DEFAULT);
  }

  LocalDatePM underTest = new LocalDatePM();

  @Test
  public void test_getText_for_year_with_four_digits() {
    // Given:
    underTest.setLocalDate(LocalDate.of(2001, 01, 01));

    // When:
    String act = underTest.getText();

    // Then:
    assertThat(act).isEqualTo("01.01.2001");
  }

  @Test
  public void test_getText_for_year_with_two_digits() {
    // Give
    underTest.setLocalDate(LocalDate.of(20, 01, 01));

    // When:
    String act = underTest.getText();

    // Then:
    assertThat(act).isEqualTo("01.01.0020");
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
        .toFormatter();
    // @formatter:on
    underTest.setFormatter(formatter, underTest.getParser());
    underTest.setLocalDate(LocalDate.of(2020, 01, 01));

    // When:
    String act = underTest.getText();

    // Then:
    assertThat(act).isEqualTo("01.01.20");
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
        .toFormatter();
    // @formatter:on
    underTest.setFormatter(formatter, underTest.getParser());
    underTest.setLocalDate(LocalDate.of(1999, 01, 01));

    // When:
    String act = underTest.getText();

    // Then:
    assertThat(act).isEqualTo("01.01.99");
  }


}

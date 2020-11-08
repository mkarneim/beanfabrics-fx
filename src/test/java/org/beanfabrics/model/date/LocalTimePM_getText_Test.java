package org.beanfabrics.model.date;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;
import org.beanfabrics.testenv.TestBase;
import org.junit.Before;
import org.junit.Test;

public class LocalTimePM_getText_Test extends TestBase {
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

  LocalTimePM underTest = new LocalTimePM();

  @Test
  public void test_getText_for_zero_time() {
    // Given:
    underTest.setLocalTime(LocalTime.of(0, 0, 0));

    // When:
    String act = underTest.getText();

    // Then:
    assertThat(act).isEqualTo("00:00:00");
  }

  @Test
  public void test_getText_for_zero_time_with_no_seconds_defined() {
    // Given:
    underTest.setLocalTime(LocalTime.of(0, 0));

    // When:
    String act = underTest.getText();

    // Then:
    assertThat(act).isEqualTo("00:00:00");
  }

  @Test
  public void test_getText_for_time_in_24_hour_style() {
    // Give
    underTest.setLocalTime(LocalTime.of(23, 59, 15));

    // When:
    String act = underTest.getText();

    // Then:
    assertThat(act).isEqualTo("23:59:15");
  }

  @Test
  public void test_getText_with_curstom_formatter() {
    // Given:
    // @formatter:off
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .appendValue(ChronoField.HOUR_OF_DAY, 2)
        .appendLiteral(':')
        .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
        .appendLiteral(':')
        .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
        .toFormatter();
    // @formatter:on
    underTest.setFormatter(formatter, underTest.getParser());
    underTest.setLocalTime(LocalTime.of(1, 30));

    // When:
    String act = underTest.getText();

    // Then:
    assertThat(act).isEqualTo("01:30:00");
  }

  @Test
  public void test_getText_with_curstom_formatter_ignoring_seconds() {
    // Given:
    // @formatter:off
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .appendValue(ChronoField.HOUR_OF_DAY, 2)
        .appendLiteral(':')
        .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
        .toFormatter();
    // @formatter:on
    underTest.setFormatter(formatter, underTest.getParser());
    underTest.setLocalTime(LocalTime.of(1, 30));

    // When:
    String act = underTest.getText();

    // Then:
    assertThat(act).isEqualTo("01:30");
  }

}

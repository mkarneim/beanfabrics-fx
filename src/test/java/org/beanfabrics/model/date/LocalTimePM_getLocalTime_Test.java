package org.beanfabrics.model.date;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;
import org.beanfabrics.testenv.TestBase;
import org.junit.Before;
import org.junit.Test;

public class LocalTimePM_getLocalTime_Test extends TestBase {
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
  public void test_getLocalTime_for_zero_time() {
    // Given:
    underTest.setText("00:00:00");

    // When:
    LocalTime act = underTest.getLocalTime();

    // Then:
    assertThat(act).isEqualTo(LocalTime.of(00, 00));
  }

  @Test
  public void test_getLocalTime_for_time_in_24_hour_style() {
    // Give
    underTest.setText("23:59:01");

    // When:
    LocalTime act = underTest.getLocalTime();

    // Then:
    assertThat(act).isEqualTo(LocalTime.of(23, 59, 01));
  }

  @Test
  public void test_getLocalTime_parsing_only_hours_and_minutes() {
    // Given:
    // @formatter:off
    DateTimeFormatter parser = new DateTimeFormatterBuilder()
        .appendValue(ChronoField.HOUR_OF_DAY, 2)
        .appendLiteral(':')
        .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
        .toFormatter();
    // @formatter:on
    underTest.setFormatter(underTest.getFormatter(), parser);
    underTest.setText("14:30");

    // When:
    LocalTime act = underTest.getLocalTime();

    // Then:
    assertThat(act).isEqualTo(LocalTime.of(14, 30));
  }

}

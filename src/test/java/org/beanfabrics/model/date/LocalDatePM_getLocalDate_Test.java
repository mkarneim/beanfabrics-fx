package org.beanfabrics.model.date;

import java.time.LocalDate;
import java.util.Locale;
import org.beanfabrics.model.ConversionException;
import org.beanfabrics.testenv.TestBase;
import org.junit.Before;
import org.junit.Test;

public class LocalDatePM_getLocalDate_Test extends TestBase {
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
  public void test_getLocalDate_for_year_with_four_digits() {
    // Given:
    underTest.setText("1.1.2001");

    // When:
    LocalDate act = underTest.getLocalDate();

    // Then:
    assertThat(act).isEqualTo(LocalDate.of(2001, 01, 01));
  }

  @Test
  public void test_getLocalDate_for_year_with_two_digits_having_value_20() {
    // Give
    underTest.setText("1.1.20");

    // When:
    LocalDate act = underTest.getLocalDate();

    // Then:
    assertThat(act).isEqualTo(LocalDate.of(2020, 01, 01));
  }

  @Test
  public void test_getLocalDate_for_year_with_two_digits_having_value_99() {
    // Give
    underTest.setText("1.1.99");

    // When:
    LocalDate act = underTest.getLocalDate();

    // Then:
    assertThat(act).isEqualTo(LocalDate.of(2099, 01, 01));
  }

  @Test
  public void test_getLocalDate_for_year_with_two_digits_having_value_70() {
    // Give
    underTest.setText("1.1.70");

    // When:
    LocalDate act = underTest.getLocalDate();

    // Then:
    assertThat(act).isEqualTo(LocalDate.of(2070, 01, 01));
  }

  @Test
  public void test_getLocalDate_xxx1() {
    // Given:
    underTest.setText("27.02.2020");

    // When:
    LocalDate act = underTest.getLocalDate();


    // Then:
    assertThat(act).isEqualTo(LocalDate.of(2020, 02, 27));
  }

  @Test
  public void test_getLocalDate_for_leap_day() {
    // Given:
    underTest.setText("29.02.2020");

    // When:
    LocalDate act = underTest.getLocalDate();

    // Then:
    assertThat(act).isEqualTo(LocalDate.of(2020, 02, 29));
  }

  @Test
  public void test_getLocalDate_for_day_greater_than_last_day_of_month() {
    // Given:
    underTest.setText("30.02.2020");

    // Expect:
    assertThatThrownBy(() -> {
      LocalDate act = underTest.getLocalDate();
      System.out.println(act);
    }).isInstanceOf(ConversionException.class);
  }

  @Test
  public void test_getLocalDate_for_day_greater_than_last_day_of_month_and_year_with_two_digits() {
    // Given:
    underTest.setText("30.02.20");

    // Expect:
    assertThatThrownBy(() -> {
      LocalDate act = underTest.getLocalDate();
      System.out.println(act);
    }).isInstanceOf(ConversionException.class);
  }

}

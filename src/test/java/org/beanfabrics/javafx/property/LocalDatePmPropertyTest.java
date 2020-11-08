package org.beanfabrics.javafx.property;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import org.beanfabrics.model.date.LocalDatePM;
import org.beanfabrics.testenv.TestBase;
import org.junit.Test;

public class LocalDatePmPropertyTest extends TestBase {

  LocalDatePmProperty underTest = new LocalDatePmProperty();

  @Test
  public void test_getLocalDate_without_assigned_pm() {
    // Given:

    // When:
    LocalDate actual = underTest.getLocalDate();

    // Then:
    assertThat(actual).isNull();
  }

  @Test
  public void test_getLocalDate_returns_initial_value_from_assigned_pm() {
    // Given:
    LocalDate expected = LocalDate.of(2000, 01, 01);
    LocalDatePM pm = new LocalDatePM();
    pm.setLocalDate(expected);
    underTest.set(pm);

    // When:
    LocalDate actual = underTest.getLocalDate();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_getLocalDate_returns_changed_value_from_assigned_pm() {
    // Given:
    LocalDatePM pm = new LocalDatePM();
    LocalDate initial = LocalDate.of(2000, 01, 01);
    pm.setLocalDate(initial);
    underTest.set(pm);
    LocalDate expected = LocalDate.of(1999, 12, 31);
    pm.setLocalDate(expected);

    // When:
    LocalDate actual = underTest.getLocalDate();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_getLocalDate_returns_initial_value_from_bound_property() {
    // Given:
    Property<LocalDate> property = new SimpleObjectProperty<>();
    LocalDate expected = LocalDate.of(2000, 01, 01);
    property.setValue(expected);
    underTest.localDateProperty().bindBidirectional(property);

    // When:
    LocalDate actual = underTest.getLocalDate();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_getLocalDate_returns_changed_value_from_bound_property() {
    // Given:
    Property<LocalDate> property = new SimpleObjectProperty<>();
    LocalDate initial = LocalDate.of(2000, 01, 01);
    property.setValue(initial);
    underTest.localDateProperty().bindBidirectional(property);
    LocalDate expected = LocalDate.of(1999, 12, 31);
    property.setValue(expected);

    // When:
    LocalDate actual = underTest.getLocalDate();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_setLocalDate_pushes_new_value_to_assigned_pm() {
    // Given:
    LocalDatePM pm = new LocalDatePM();
    underTest.set(pm);
    LocalDate expected = LocalDate.of(2000, 01, 01);

    // When:
    underTest.setLocalDate(expected);

    // Then:
    LocalDate actual = pm.getLocalDate();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_setLocalDate_fires_Event() {
    // Given:
    List<LocalDate> newValues = new ArrayList<>();
    underTest.localDateProperty().addListener((obs, oldV, newV) -> {
      newValues.add(newV);
    });
    LocalDate expected = LocalDate.of(2000, 01, 01);

    // When:
    underTest.setLocalDate(expected);

    // Then:
    assertThat(newValues).containsOnly(expected);
  }

  @Test
  public void test_setLocalDate_pushes_new_value_to_bound_property() {
    // Given:
    Property<LocalDate> property = new SimpleObjectProperty<>();
    underTest.localDateProperty().bindBidirectional(property);
    LocalDate expected = LocalDate.of(2000, 01, 01);

    // When:
    underTest.setLocalDate(expected);

    // Then:
    assertThat(property.getValue()).isEqualTo(expected);
  }

}

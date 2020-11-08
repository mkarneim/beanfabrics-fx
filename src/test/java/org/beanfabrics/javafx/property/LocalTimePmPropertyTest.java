package org.beanfabrics.javafx.property;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.beanfabrics.model.date.LocalTimePM;
import org.beanfabrics.testenv.TestBase;
import org.junit.Test;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

public class LocalTimePmPropertyTest extends TestBase {

  LocalTimePmProperty underTest = new LocalTimePmProperty();

  @Test
  public void test_getLocalTime_without_assigned_pm() {
    // Given:

    // When:
    LocalTime actual = underTest.getLocalTime();

    // Then:
    assertThat(actual).isNull();
  }

  @Test
  public void test_getLocalTime_returns_initial_value_from_assigned_pm() {
    // Given:
    LocalTime expected = LocalTime.of(14, 25, 59);
    LocalTimePM pm = new LocalTimePM();
    pm.setLocalTime(expected);
    underTest.set(pm);

    // When:
    LocalTime actual = underTest.getLocalTime();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_getLocalTime_returns_changed_value_from_assigned_pm() {
    // Given:
    LocalTimePM pm = new LocalTimePM();
    LocalTime initial = LocalTime.of(14, 25, 59);
    pm.setLocalTime(initial);
    underTest.set(pm);
    LocalTime expected = LocalTime.of(13, 30, 15);
    pm.setLocalTime(expected);

    // When:
    LocalTime actual = underTest.getLocalTime();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_getLocalTime_returns_initial_value_from_bound_property() {
    // Given:
    Property<LocalTime> property = new SimpleObjectProperty<>();
    LocalTime expected = LocalTime.of(14, 25, 59);
    property.setValue(expected);
    underTest.localTimeProperty().bindBidirectional(property);

    // When:
    LocalTime actual = underTest.getLocalTime();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_getLocalTime_returns_changed_value_from_bound_property() {
    // Given:
    Property<LocalTime> property = new SimpleObjectProperty<>();
    LocalTime initial = LocalTime.of(14, 25, 59);
    property.setValue(initial);
    underTest.localTimeProperty().bindBidirectional(property);
    LocalTime expected = LocalTime.of(13, 30, 15);
    property.setValue(expected);

    // When:
    LocalTime actual = underTest.getLocalTime();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_setLocalTime_pushes_new_value_to_assigned_pm() {
    // Given:
    LocalTimePM pm = new LocalTimePM();
    underTest.set(pm);
    LocalTime expected = LocalTime.of(14, 25, 59);

    // When:
    underTest.setLocalTime(expected);

    // Then:
    LocalTime actual = pm.getLocalTime();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_setLocalTime_fires_Event() {
    // Given:
    List<LocalTime> newValues = new ArrayList<>();
    underTest.localTimeProperty().addListener((obs, oldV, newV) -> {
      newValues.add(newV);
    });
    LocalTime expected = LocalTime.of(14, 25, 59);

    // When:
    underTest.setLocalTime(expected);

    // Then:
    assertThat(newValues).containsOnly(expected);
  }

  @Test
  public void test_setLocalTime_pushes_new_value_to_bound_property() {
    // Given:
    Property<LocalTime> property = new SimpleObjectProperty<>();
    underTest.localTimeProperty().bindBidirectional(property);
    LocalTime expected = LocalTime.of(14, 25, 59);

    // When:
    underTest.setLocalTime(expected);

    // Then:
    assertThat(property.getValue()).isEqualTo(expected);
  }

}

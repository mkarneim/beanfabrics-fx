package org.beanfabrics.javafx.property;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.testenv.TestBase;
import org.junit.Test;

public class BooleanPmPropertyTest extends TestBase {

  BooleanPmProperty underTest = new BooleanPmProperty();

  @Test
  public void test_isSelected_without_assigned_pm() {
    // Given:

    // When:
    boolean actual = underTest.isSelected();

    // Then:
    assertThat(actual).isEqualTo(false);
  }

  @Test
  public void test_isSelected_returns_initial_value_from_assigned_pm() {
    // Given:
    boolean expected = true;
    BooleanPM pm = new BooleanPM();
    pm.setBoolean(expected);
    underTest.set(pm);

    // When:
    boolean actual = underTest.isSelected();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_isSelected_returns_changed_value_from_assigned_pm() {
    // Given:
    boolean initial = true;
    BooleanPM pm = new BooleanPM();
    pm.setBoolean(initial);
    underTest.set(pm);
    boolean expected = true;
    pm.setBoolean(expected);

    // When:
    boolean actual = underTest.isSelected();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_isSelected_returns_initial_value_from_bound_property() {
    // Given:
    Property<Boolean> property = new SimpleBooleanProperty();
    boolean expected = true;
    property.setValue(expected);
    underTest.selectedProperty().bindBidirectional(property);

    // When:
    boolean actual = underTest.isSelected();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_isSelected_returns_changed_value_from_bound_property() {
    // Given:
    Property<Boolean> property = new SimpleBooleanProperty();
    boolean initial = true;
    property.setValue(initial);
    underTest.selectedProperty().bindBidirectional(property);
    boolean expected = false;
    property.setValue(expected);

    // When:
    boolean actual = underTest.isSelected();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_setSelected_pushes_new_value_to_assigned_pm() {
    // Given:
    BooleanPM pm = new BooleanPM();
    underTest.set(pm);
    boolean expected = true;

    // When:
    underTest.setSelected(expected);

    // Then:
    Boolean actual = pm.getBoolean();
    assertThat(actual).isEqualTo(expected);
  }


  @Test
  public void test_setSelected_fires_Event() {
    // Given:
    List<Boolean> newValues = new ArrayList<>();
    underTest.selectedProperty().addListener((obs, oldV, newV) -> {
      newValues.add(newV);
    });
    boolean expected = true;

    // When:
    underTest.setSelected(expected);

    // Then:
    assertThat(newValues).containsOnly(expected);
  }

  @Test
  public void test_setSelected_pushes_value_to_bound_property() {
    // Given:
    Property<Boolean> property = new SimpleBooleanProperty();
    underTest.selectedProperty().bindBidirectional(property);
    boolean expected = true;

    // When:
    underTest.setSelected(expected);

    // Then:
    assertThat(property.getValue()).isEqualTo(expected);
  }

}

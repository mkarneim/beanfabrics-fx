package org.beanfabrics.javafx.property;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import org.beanfabrics.model.Options;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.testenv.TestBase;
import org.junit.Test;

public class TextPmPropertyTest extends TestBase {

  TextPmProperty underTest = new TextPmProperty();

  @Test
  public void test_getText_without_assigned_pm() {
    // Given:

    // When:
    String actual = underTest.getText();

    // Then:
    assertThat(actual).isNull();
  }

  @Test
  public void test_getText_returns_initial_value_from_assigned_pm() {
    // Given:
    String expected = "some text";
    TextPM pm = new TextPM();
    pm.setText(expected);
    underTest.set(pm);

    // When:
    String actual = underTest.getText();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_getText_returns_changed_value_from_assigned_pm() {
    // Given:
    TextPM pm = new TextPM();
    String initial = "some initial text";
    pm.setText(initial);
    underTest.set(pm);
    String expected = "some updated text";
    pm.setText(expected);

    // When:
    String actual = underTest.getText();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_getText_returns_initial_value_from_bound_property() {
    // Given:
    Property<String> property = new SimpleStringProperty();
    String expected = "some text";
    property.setValue(expected);
    underTest.textProperty().bindBidirectional(property);

    // When:
    String actual = underTest.getText();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_getText_returns_changed_value_from_bound_property() {
    // Given:
    Property<String> property = new SimpleStringProperty();
    String initial = "some text";
    property.setValue(initial);
    underTest.textProperty().bindBidirectional(property);
    String expected = "some other text";
    property.setValue(expected);

    // When:
    String actual = underTest.getText();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_setText_pushes_new_value_to_assigned_pm() {
    // Given:
    TextPM pm = new TextPM();
    underTest.set(pm);
    String expected = "some text";

    // When:
    underTest.setText(expected);

    // Then:
    String actual = pm.getText();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void test_setText_fires_Event() {
    // Given:
    List<String> newValues = new ArrayList<>();
    underTest.textProperty().addListener((obs, oldV, newV) -> {
      newValues.add(newV);
    });
    String expected = "some text";

    // When:
    underTest.setText(expected);

    // Then:
    assertThat(newValues).containsOnly(expected);
  }

  @Test
  public void test_setText_pushes_new_value_to_bound_property() {
    // Given:
    Property<String> property = new SimpleStringProperty();
    underTest.textProperty().bindBidirectional(property);
    String expected = "some text";

    // When:
    underTest.setText(expected);

    // Then:
    assertThat(property.getValue()).isEqualTo(expected);
  }


  //// options

  @Test
  public void test_getOptions() {
    // Given:
    String[] expected = arrayOf("alpha", "beta", "gamma");
    TextPM pm = new TextPM();
    pm.setOptions(Options.create(expected));
    underTest.set(pm);

    // When:
    List<String> actual = underTest.getOptions();

    // Then:
    assertThat(actual).containsExactly(expected);
  }

  @Test
  public void test_getOptions2() {
    // Given:
    String[] initial = arrayOf("alpha", "beta", "gamma");
    TextPM pm = new TextPM();
    underTest.set(pm);
    Options<String> options = Options.create(initial);
    pm.setOptions(options);
    options.put("new", "new");

    // When:
    List<String> actual = underTest.getOptions();

    // Then:
    assertThat(actual).containsExactlyElementsOf(listOf(listOf(initial), "new"));
  }
}

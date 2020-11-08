package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.testenv.TestBase;
import org.junit.Test;
import javafx.beans.property.SimpleIntegerProperty;

public class IntegerPropertyBinderTest extends TestBase {
  private static class DummyPM extends AbstractPM {
    private final IntegerPM value = new IntegerPM();

    public DummyPM() {
      PMManager.setup(this);
    }
  }

  private final IntegerPropertyBinder underTest = new IntegerPropertyBinder();
  private final SimpleIntegerProperty view = new SimpleIntegerProperty();
  private final IModelProvider modelProvider = new ModelProvider();
  private final Path path = new Path("value");
  private final DummyPM pm = new DummyPM();

  @Override
  public void before() {
    super.before();
    modelProvider.setPresentationModel(pm);
  }

  @Test
  public void test_initial_binding_copies_value_from_pm_to_view() {
    // Given:
    Integer expected = 1;
    view.set(-5);
    pm.value.setInteger(expected);

    // When:
    underTest.bind(view, modelProvider, path);

    // Then:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_unconvertible_text_will_be_converted_to_0() {
    // Given:
    Integer expected = 0;
    view.set(-5);
    pm.value.setText("no number");

    // When:
    underTest.bind(view, modelProvider, path);

    // Then:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_pm_to_property() {
    // Given:
    underTest.bind(view, modelProvider, path);
    Integer expected = 6;

    // When:
    pm.value.setInteger(expected);

    // Then:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_property_to_pm() {
    // Given:
    underTest.bind(view, modelProvider, path);
    Integer expected = 1;

    // When:
    view.set(expected);

    // Then:
    assertThat(pm.value.getInteger()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_pm_to_property_after_gc() {
    // Given:
    underTest.bind(view, modelProvider, path);
    Integer expected = 6;
    System.gc();

    // When:
    pm.value.setInteger(expected);

    // Then:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_property_to_pm_after_gc() {
    // Given:
    underTest.bind(view, modelProvider, path);
    Integer expected = 1;
    System.gc();

    // When:
    view.set(expected);

    // Then:
    assertThat(pm.value.getInteger()).isEqualTo(expected);
  }
}

package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.DecimalPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.testenv.TestBase;
import org.junit.Test;
import javafx.beans.property.SimpleFloatProperty;

public class FloatPropertyBinderTest extends TestBase {
  private static class DummyPM extends AbstractPM {
    private final DecimalPM value = new DecimalPM();

    public DummyPM() {
      PMManager.setup(this);
    }
  }

  private final FloatPropertyBinder underTest = new FloatPropertyBinder();
  private final SimpleFloatProperty view = new SimpleFloatProperty();
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
    Float expected = 1.2f;
    view.set(-5.1f);
    pm.value.setFloat(expected);

    // When:
    underTest.bind(view, modelProvider, path);

    // Then:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_unconvertible_text_will_be_converted_to_0() {
    // Given:
    Float expected = 0.0f;
    view.set(-5.1f);
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
    Float expected = 6.3f;

    // When:
    pm.value.setFloat(expected);

    // Then:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_property_to_pm() {
    // Given:
    underTest.bind(view, modelProvider, path);
    Float expected = 0.1f;

    // When:
    view.set(expected);

    // Then:
    assertThat(pm.value.getFloat()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_pm_to_property_after_gc() {
    // Given:
    underTest.bind(view, modelProvider, path);
    Float expected = 6.3f;
    System.gc();

    // When:
    pm.value.setFloat(expected);

    // Then:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_property_to_pm_after_gc() {
    // Given:
    underTest.bind(view, modelProvider, path);
    Float expected = 0.1f;
    System.gc();

    // When:
    view.set(expected);

    // Then:
    assertThat(pm.value.getFloat()).isEqualTo(expected);
  }
}

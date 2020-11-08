package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.DecimalPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.testenv.TestBase;
import org.junit.Test;
import javafx.beans.property.SimpleDoubleProperty;

public class DoublePropertyBinderTest extends TestBase {
  private static class DummyPM extends AbstractPM {
    private final DecimalPM value = new DecimalPM();

    public DummyPM() {
      PMManager.setup(this);
    }
  }

  private final DoublePropertyBinder underTest = new DoublePropertyBinder();
  private final SimpleDoubleProperty view = new SimpleDoubleProperty();
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
    Double expected = 1.2d;
    view.set(-5.1d);
    pm.value.setDouble(expected);

    // When:
    underTest.bind(view, modelProvider, path);

    // Then:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_unconvertible_text_will_be_converted_to_0() {
    // Given:
    Double expected = 0.0d;
    view.set(-5.1d);
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
    Double expected = 6.3d;

    // When:
    pm.value.setDouble(expected);

    // Then:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_property_to_pm() {
    // Given:
    underTest.bind(view, modelProvider, path);
    Double expected = 0.1d;

    // When:
    view.set(expected);

    // Then:
    assertThat(pm.value.getDouble()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_pm_to_property_after_gc() {
    // Given:
    underTest.bind(view, modelProvider, path);
    Double expected = 6.3d;
    System.gc();

    // When:
    pm.value.setDouble(expected);

    // Then:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_property_to_pm_after_gc() {
    // Given:
    underTest.bind(view, modelProvider, path);
    Double expected = 0.1d;
    System.gc();

    // When:
    view.set(expected);

    // Then:
    assertThat(pm.value.getDouble()).isEqualTo(expected);
  }
}

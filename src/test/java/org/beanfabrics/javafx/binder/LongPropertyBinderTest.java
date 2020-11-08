package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.testenv.TestBase;
import org.junit.Test;
import javafx.beans.property.SimpleLongProperty;

public class LongPropertyBinderTest extends TestBase {
  private static class DummyPM extends AbstractPM {
    private final IntegerPM value = new IntegerPM();

    public DummyPM() {
      PMManager.setup(this);
    }
  }

  private final LongPropertyBinder underTest = new LongPropertyBinder();
  private final SimpleLongProperty view = new SimpleLongProperty();
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
    Long expected = 1L;
    view.set(-5);
    pm.value.setLong(expected);

    // When:
    underTest.bind(view, modelProvider, path);

    // Then:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_unconvertible_text_will_be_converted_to_0() {
    // Given:
    Long expected = 0L;
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
    Long expected = 6L;

    // When:
    pm.value.setLong(expected);

    // Then:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_property_to_pm() {
    // Given:
    underTest.bind(view, modelProvider, path);
    Long expected = 1L;

    // When:
    view.set(expected);

    // Then:
    assertThat(pm.value.getLong()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_pm_to_property_after_gc() {
    // Given:
    underTest.bind(view, modelProvider, path);
    Long expected = 6L;
    System.gc();

    // When:
    pm.value.setLong(expected);

    // Then:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_property_to_pm_after_gc() {
    // Given:
    underTest.bind(view, modelProvider, path);
    Long expected = 1L;
    System.gc();

    // When:
    view.set(expected);

    // Then:
    assertThat(pm.value.getLong()).isEqualTo(expected);
  }
}

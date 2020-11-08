package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.testenv.TestBase;
import org.junit.Test;
import javafx.beans.property.SimpleBooleanProperty;

public class BooleanPropertyBinderTest extends TestBase {
  private static class DummyPM extends AbstractPM {
    private final BooleanPM value = new BooleanPM();

    public DummyPM() {
      value.setBoolean(false);
      PMManager.setup(this);
    }
  }

  private final BooleanPropertyBinder underTest = new BooleanPropertyBinder();
  private final SimpleBooleanProperty view = new SimpleBooleanProperty();
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
    boolean expected = false;
    view.set(true);
    pm.value.setBoolean(expected);
    underTest.bind(view, modelProvider, path);

    // Expect:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_pm_to_property() {
    // Given:
    underTest.bind(view, modelProvider, path);

    // When:
    pm.value.setBoolean(true);

    // Then:
    assertThat(view.get()).isTrue();
  }

  @Test
  public void test_can_sync_state_from_property_to_pm() {
    // Given:
    underTest.bind(view, modelProvider, path);

    // When:
    view.set(true);

    // Then:
    assertThat(pm.value.getBoolean()).isTrue();
  }

  @Test
  public void test_can_sync_state_from_pm_to_property_after_gc() {
    // Given:
    underTest.bind(view, modelProvider, path);
    System.gc();

    // When:
    pm.value.setBoolean(true);

    // Then:
    assertThat(view.get()).isTrue();
  }

  @Test
  public void test_can_sync_state_from_property_to_pm_after_gc() {
    // Given:
    underTest.bind(view, modelProvider, path);
    System.gc();

    // When:
    view.set(true);

    // Then:
    assertThat(pm.value.getBoolean()).isTrue();
  }
}

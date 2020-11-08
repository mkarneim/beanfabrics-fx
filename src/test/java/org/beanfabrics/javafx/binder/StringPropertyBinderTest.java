package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.testenv.TestBase;
import org.junit.Test;
import javafx.beans.property.SimpleStringProperty;

public class StringPropertyBinderTest extends TestBase {
  private static class DummyPM extends AbstractPM {
    private final TextPM value = new TextPM();

    public DummyPM() {
      PMManager.setup(this);
    }
  }

  private final StringPropertyBinder underTest = new StringPropertyBinder();
  private final SimpleStringProperty view = new SimpleStringProperty();
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
    String expected = "initial value";
    view.set("other value");
    pm.value.setText(expected);

    // When:
    underTest.bind(view, modelProvider, path);

    // Then:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_pm_to_property() {
    // Given:
    underTest.bind(view, modelProvider, path);
    String expected = "hello";

    // When:
    pm.value.setText(expected);

    // Then:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_property_to_pm() {
    // Given:
    underTest.bind(view, modelProvider, path);
    String expected = "hello";

    // When:
    view.set(expected);

    // Then:
    assertThat(pm.value.getText()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_pm_to_property_after_gc() {
    // Given:
    underTest.bind(view, modelProvider, path);
    String expected = "hello";
    System.gc();

    // When:
    pm.value.setText(expected);

    // Then:
    assertThat(view.get()).isEqualTo(expected);
  }

  @Test
  public void test_can_sync_state_from_property_to_pm_after_gc() {
    // Given:
    underTest.bind(view, modelProvider, path);
    String expected = "hello";
    System.gc();

    // When:
    view.set(expected);

    // Then:
    assertThat(pm.value.getText()).isEqualTo(expected);
  }
}

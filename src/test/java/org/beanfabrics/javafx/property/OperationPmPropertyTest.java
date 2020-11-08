package org.beanfabrics.javafx.property;

import org.beanfabrics.model.OperationPM;
import org.beanfabrics.testenv.TestBase;
import org.junit.Test;

public class OperationPmPropertyTest extends TestBase {

  OperationPmProperty underTest = new OperationPmProperty();

  @Test
  public void test_isEnabled_without_assigned_pm() {
    // Given:

    // When:
    boolean actual = underTest.isEnabled();

    // Then:
    assertThat(actual).isEqualTo(false);
  }

  @Test
  public void test_isEnabled_with_assigned_pm() {
    // Given:
    OperationPM pm = new OperationPM();
    underTest.set(pm);

    // When:
    boolean actual = underTest.isEnabled();

    // Then:
    assertThat(actual).isEqualTo(false);
  }

  @Test
  public void test_isEnabled_with_assigned_pm_having_an_execution_method() {
    // Given:
    OperationPM pm = new OperationPM();
    pm.addExecutionMethod(() -> false);
    underTest.set(pm);

    // When:
    boolean actual = underTest.isEnabled();

    // Then:
    assertThat(actual).isEqualTo(true);
  }

}

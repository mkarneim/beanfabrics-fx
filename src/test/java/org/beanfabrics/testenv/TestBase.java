package org.beanfabrics.testenv;

import org.junit.Before;

public class TestBase extends TestDslBase {

  @Before
  public void before() {
    // Make sure that expections don't get lost.
    // To understand this, please see com.sun.javafx.binding.ExpressionHelper.SingleChange.fireValueChangedEvent()
    Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
      throw new UncaughtException(e);
    });
  }


}

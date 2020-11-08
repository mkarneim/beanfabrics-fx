package org.beanfabrics.javafx.validation;

import static com.google.common.base.Preconditions.checkNotNull;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;
import javafx.stage.Window;

public abstract class AbstractGraphicValidationSupport {

  protected final Control control;

  public AbstractGraphicValidationSupport(Control control) {
    this.control = checkNotNull(control, "control == null!");
    control.sceneProperty().addListener((obs, oldv, newv) -> {
      ChangeListener<Window> windowListener = new ChangeListener<Window>() {
        @Override
        public void changed(ObservableValue<? extends Window> observable, Window oldValue, Window newValue) {
          if (newValue != null) {
            updateDecoration();
          }
        }
      };
      if (oldv != null) {
        oldv.windowProperty().removeListener(windowListener);
      }
      if (newv != null) {
        newv.windowProperty().addListener(windowListener);
        if (newv.getWindow() != null) {
          updateDecoration();
        }
      }
    });
  }

  protected abstract void updateDecoration();

}

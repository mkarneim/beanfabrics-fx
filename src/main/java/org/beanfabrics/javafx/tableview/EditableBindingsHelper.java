package org.beanfabrics.javafx.tableview;

import java.beans.PropertyChangeListener;
import org.beanfabrics.model.IValuePM;
import org.beanfabrics.model.PresentationModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class EditableBindingsHelper {

  public static <PM extends PresentationModel, C extends TableView<PM>> ObservableBooleanValue selectTableViewEditable(
      ObservableValue<C> property) {
    boolean initialValue = property.getValue() == null ? false : property.getValue().isEditable();
    SimpleBooleanProperty result = new SimpleBooleanProperty(initialValue);
    property.addListener(new ChangeListener<C>() {
      C value;
      ChangeListener<? super Boolean> listener = (obs, oldV, newV) -> {
        onEditableChanged();
      };

      @Override
      public void changed(ObservableValue<? extends C> observable, C oldValue, C newValue) {
        if (value != null) {
          value.editableProperty().removeListener(listener);
        }
        value = newValue;
        if (value != null) {
          value.editableProperty().addListener(listener);
        }
        onEditableChanged();
      }

      private void onEditableChanged() {
        result.set(value == null ? false : value.isEditable());
      }

    });
    return result;
  }

  public static <PM extends PresentationModel, T extends PresentationModel, C extends TableColumn<PM, T>> ObservableBooleanValue selectTableColumnEditable(
      ObservableValue<C> property) {
    boolean initialValue = property.getValue() == null ? false : property.getValue().isEditable();
    SimpleBooleanProperty result = new SimpleBooleanProperty(initialValue);
    property.addListener(new ChangeListener<C>() {
      C value;
      ChangeListener<? super Boolean> listener = (obs, oldV, newV) -> {
        onEditableChanged();
      };

      @Override
      public void changed(ObservableValue<? extends C> observable, C oldValue, C newValue) {
        if (value != null) {
          value.editableProperty().removeListener(listener);
        }
        value = newValue;
        if (value != null) {
          value.editableProperty().addListener(listener);
        }
        onEditableChanged();
      }

      private void onEditableChanged() {
        result.set(value == null ? false : value.isEditable());
      }

    });
    return result;
  }

  public static <T extends PresentationModel> ObservableBooleanValue selectEditable(ObservableValue<T> property) {
    boolean initialValue = property.getValue() == null ? false : isEditable(property.getValue());
    SimpleBooleanProperty result = new SimpleBooleanProperty(initialValue);
    property.addListener(new ChangeListener<T>() {
      T value;
      PropertyChangeListener listener = e -> onEditableChanged();

      @Override
      public void changed(ObservableValue<? extends T> obs, T oldV, T newV) {
        if (value != null) {
          value.removePropertyChangeListener("editable", listener);
        }
        value = newV;
        if (value != null) {
          value.addPropertyChangeListener("editable", listener);
        }
        onEditableChanged();
      }

      private void onEditableChanged() {
        result.set(isEditable(value));
      }
    });
    return result;
  }

  private static boolean isEditable(PresentationModel pm) {
    if (pm instanceof IValuePM) {
      return ((IValuePM) pm).isEditable();
    }
    return false;
  }
}

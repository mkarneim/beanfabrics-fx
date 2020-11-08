package org.beanfabrics.javafx.tableview;

import org.beanfabrics.javafx.validation.ValidationErrorStyling;
import org.beanfabrics.model.IBooleanPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.validation.ValidationState;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableBooleanValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

public class BnFxCheckBoxTableCell<PM extends PresentationModel, T extends IBooleanPM> extends TableCell<PM, T> {
  private final CheckBox checkBox = new CheckBox();

  public BnFxCheckBoxTableCell() {
    ValidationErrorStyling.install(getStylesheets());
    setAlignment(Pos.CENTER);
    ObservableBooleanValue tableViewEditable = EditableBindingsHelper.selectTableViewEditable(tableViewProperty());
    ObservableBooleanValue tableColumnEditable = EditableBindingsHelper.selectTableColumnEditable(tableColumnProperty());
    ObservableBooleanValue itemEditable = EditableBindingsHelper.selectEditable(itemProperty());
    editableProperty().bind(Bindings.createBooleanBinding(() -> {
      return tableViewEditable.get() && tableColumnEditable.get() && itemEditable.get();
    }, tableViewEditable, tableColumnEditable, itemEditable));
    checkBox.disableProperty().bind(editableProperty().not());
    checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
      T cellPm = getItem();
      if (cellPm != null) {
        cellPm.setBoolean(newValue);
      }
    });
  }

  @Override
  public void updateItem(T cellPm, boolean empty) {
    super.updateItem(cellPm, empty);
    if (empty || cellPm == null) {
      setGraphic(null);
    } else {
      setGraphic(checkBox);
      checkBox.setSelected(cellPm.getBoolean());
    }
    updateStyle();
  }

  private void updateStyle() {
    T cellPm = getItem();
    ValidationState v = cellPm == null ? null : cellPm.getValidationState();
    ValidationErrorStyling.setValidationError(this, v != null);
  }
}

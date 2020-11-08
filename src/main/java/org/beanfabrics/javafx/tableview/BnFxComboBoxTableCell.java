package org.beanfabrics.javafx.tableview;

import org.beanfabrics.javafx.binder.ComboBoxBinding;
import org.beanfabrics.javafx.property.TextPmProperty;
import org.beanfabrics.javafx.validation.ValidationErrorStyling;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.validation.ValidationState;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableBooleanValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;

public class BnFxComboBoxTableCell<PM extends PresentationModel, T extends ITextPM> extends TableCell<PM, T> {
  private final ComboBox<String> comboBox = new ComboBox<>();
  private final TextPmProperty textPmProperty = new TextPmProperty();
  private ComboBoxBinding binding;
  private boolean pending_updateItem = false;

  public BnFxComboBoxTableCell() {
    this(false);
  }

  public BnFxComboBoxTableCell(boolean editableComboBox) {
    ValidationErrorStyling.install(getStylesheets());
    ObservableBooleanValue tableViewEditable = EditableBindingsHelper.selectTableViewEditable(tableViewProperty());
    ObservableBooleanValue tableColumnEditable =
        EditableBindingsHelper.selectTableColumnEditable(tableColumnProperty());
    ObservableBooleanValue itemEditable = EditableBindingsHelper.selectEditable(itemProperty());
    editableProperty().bind(Bindings.createBooleanBinding(() -> {
      return tableViewEditable.get() && tableColumnEditable.get() && itemEditable.get();
    }, tableViewEditable, tableColumnEditable, itemEditable));
    setAlignment(Pos.CENTER);
    setPadding(new Insets(0));
    comboBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    comboBox.setMinSize(5, 5);
    comboBox.editableProperty().bind(Bindings.createBooleanBinding(() -> {
      return editableComboBox && isEditable();
    }, editableProperty()));

    comboBox.disableProperty().bind(editableProperty().not());
    comboBox.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
      if (isEditing() && !pending_updateItem) {
        getItem().setText(newValue);
        commitEdit(getItem());
      }
    });
  }

  protected ComboBox<String> getComboBox() {
    return comboBox;
  }

  @Override
  public void startEdit() {
    super.startEdit();
    updateItem(getItem(), false);
  }

  @Override
  public void commitEdit(T newValue) {
    super.commitEdit(newValue);
    updateItem(getItem(), false);
    reformat(newValue);
    stopEdit();
  }

  private void reformat(T newValue) {
    if (newValue instanceof ITextPM) {
      ((ITextPM) newValue).reformat();
    }
  }

  @Override
  public void cancelEdit() {
    super.cancelEdit();
    updateItem(getItem(), false);
    stopEdit();
  }

  private void stopEdit() {
    if (getTableView() != null) {
      getTableView().requestFocus();
    }
  }

  @Override
  public void updateItem(T cellPm, boolean empty) {
    super.updateItem(cellPm, empty);
    try {
      pending_updateItem = true;
      if (binding != null) {
        binding.dispose();
      }
      textPmProperty.set(null);
      if (empty || cellPm == null) {
        setGraphic(null);
        setText(null);
      } else {
        if (isEditing()) {
          setText(null);
          setGraphic(comboBox);
          textPmProperty.set(cellPm);
          binding = new ComboBoxBinding(comboBox, textPmProperty);
          binding.getGvs().setOffsetX(6);
          binding.getGvs().setOffsetY(-6);
        } else {
          setGraphic(null);
          setText(cellPm.getText());
        }
      }
      updateStyle();
    } finally {
      pending_updateItem = false;
    }
  }

  private void updateStyle() {
    T cellPm = getItem();
    ValidationState v = cellPm == null ? null : cellPm.getValidationState();
    ValidationErrorStyling.setValidationError(this, v != null);
  }

}

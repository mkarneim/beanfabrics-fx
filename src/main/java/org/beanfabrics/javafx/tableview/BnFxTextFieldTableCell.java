package org.beanfabrics.javafx.tableview;

import javax.annotation.Nullable;
import org.beanfabrics.javafx.binder.TextInputControlBinding;
import org.beanfabrics.javafx.property.ImagePmProperty;
import org.beanfabrics.javafx.property.TextPmProperty;
import org.beanfabrics.javafx.validation.ValidationErrorStyling;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.IValuePM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.model.image.IImagePM;
import org.beanfabrics.validation.ValidationState;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class BnFxTextFieldTableCell<PM extends PresentationModel, T extends PresentationModel>
    extends TableCell<PM, T> {
  private final TextField textField = new TextField();
  private final TextPmProperty textPmProperty = new TextPmProperty();
  private TextInputControlBinding binding;
  private @Nullable String oldText;
  private @Nullable ImagePmProperty imagePmProperty;
  private @Nullable ImageView imageView;

  public BnFxTextFieldTableCell() {
    getStyleClass().add("text-field-table-cell");
    ValidationErrorStyling.install(getStylesheets());
    ObservableBooleanValue tableViewEditable = EditableBindingsHelper.selectTableViewEditable(tableViewProperty());
    ObservableBooleanValue tableColumnEditable =
        EditableBindingsHelper.selectTableColumnEditable(tableColumnProperty());
    ObservableBooleanValue itemEditable = EditableBindingsHelper.selectEditable(itemProperty());
    editableProperty().bind(Bindings.createBooleanBinding(() -> {
      return tableViewEditable.get() && tableColumnEditable.get() && itemEditable.get();
    }, tableViewEditable, tableColumnEditable, itemEditable));
  }

  @Override
  public void startEdit() {
    if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
      return;
    }
    if (binding != null) {
      binding.dispose();
    }
    super.startEdit();
    T cellPm = getItem();
    if (cellPm != null) {
      if (cellPm instanceof ITextPM) {
        oldText = ((ITextPM) cellPm).getText();
      } else {
        oldText = null;
      }
      if (isEditing()) {
        textField.setOnAction(event -> {
          commitEdit(getItem());
          event.consume();
        });
        textField.setOnKeyReleased(t -> {
          if (t.getCode() == KeyCode.ESCAPE) {
            cancelEdit();
            t.consume();
          }
        });
        textField.focusedProperty().addListener((obs, oldV, newV) -> {
          if (newV == false) {
            commitEdit(getItem());
          }
        });

        if (cellPm instanceof ITextPM) {
          binding = new TextInputControlBinding(textField, textPmProperty);
          textPmProperty.setPresentationModel((ITextPM) cellPm);
        } else {
          textField.setDisable(true);
        }
        setText(null);
        setGraphic(textField);
        textField.selectAll();
        textField.requestFocus();
      }
    }
    ValidationErrorStyling.setValidationError(this, false);
  }

  @Override
  public void cancelEdit() {
    super.cancelEdit();
    reset();
    stopEditing();
  }

  @Override
  public void commitEdit(T newValue) {
    super.commitEdit(newValue);
    reformat(newValue);
    stopEditing();
  }

  private void reformat(T newValue) {
    if (newValue instanceof ITextPM) {
      ((ITextPM) newValue).reformat();
    }
  }

  @Override
  public void updateItem(T item, boolean empty) {
    super.updateItem(item, empty);
    if (empty || item == null) {
      setText(null);
      setGraphic(null);
      setTooltip(null);
      if (imagePmProperty != null) {
        imagePmProperty.unbind();
      }
      if (imageView != null) {
        imageView.imageProperty().unbind();
      }
    } else {
      if (isEditing()) {
        setText(null);
        setGraphic(textField);
        setTooltip(null);
      } else {
        T cellPm = getItem();
        setText(getText(cellPm));
        String description = getDescription(cellPm);
        if (description != null && !description.trim().isEmpty()) {
          setTooltip(new Tooltip(description));
        } else {
          setTooltip(null);
        }
        if (cellPm instanceof IImagePM) {
          if (imagePmProperty == null) {
            imagePmProperty = new ImagePmProperty();
          }
          if (imageView == null) {
            imageView = new ImageView();
          }
          imagePmProperty.setPresentationModel((IImagePM) cellPm);
          imageView.imageProperty().bind(imagePmProperty.imageProperty());
          setGraphic(imageView);
        } else {
          setGraphic(null);
        }
      }
    }
    updateStyle();
  }

  private @Nullable String getText(T cellPm) {
    if (cellPm instanceof ITextPM) {
      return ((ITextPM) cellPm).getText();
    }
    return null;
  }

  private @Nullable String getDescription(T cellPm) {
    if (cellPm instanceof IValuePM) {
      return ((IValuePM) cellPm).getDescription();
    }
    return null;
  }

  private void stopEditing() {
    T cellPm = getItem();
    if (cellPm != null) {
      if (cellPm instanceof ITextPM) {
        setText(((ITextPM) cellPm).getText());
      } else {
        setText(null);
      }
    } else {
      setText(null);
    }
    setGraphic(null);
    updateStyle();
    textPmProperty.setPresentationModel(null);
    if (getTableView() != null) {
      getTableView().requestFocus();
    }
  }

  private void reset() {
    T cellPm = getItem();
    if (cellPm != null) {
      if (cellPm instanceof ITextPM) {
        ((ITextPM) cellPm).setText(oldText);
      }
    }
  }

  private void updateStyle() {
    T cellPm = getItem();
    ValidationState v = cellPm == null ? null : cellPm.getValidationState();
    ValidationErrorStyling.setValidationError(this, v != null);
  }

}

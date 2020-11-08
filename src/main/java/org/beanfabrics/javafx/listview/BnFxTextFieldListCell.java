package org.beanfabrics.javafx.listview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.annotation.Nullable;
import org.beanfabrics.Path;
import org.beanfabrics.PathObservation;
import org.beanfabrics.javafx.binder.TextInputControlBinder;
import org.beanfabrics.javafx.property.TextPmProperty;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.support.PropertySupport;
import org.beanfabrics.validation.ValidationState;
import javafx.collections.ObservableList;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class BnFxTextFieldListCell<PM extends PresentationModel, T extends ITextPM> extends ListCell<PM> {

  private static final TextInputControlBinder BINDER = new TextInputControlBinder();
  private final Path cellPath;
  private final TextField textField = new TextField();
  private final TextPmProperty textPmProperty = new TextPmProperty();

  private String oldText;
  @Nullable
  private PathObservation observation;
  private final PropertyChangeListener pcl = new PropertyChangeListener() {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      PM item = getItem();
      updateItem(item, item == null);
    }
  };
  private ITextPM target;
  /**
   * Work-Around für das Problem, dass cancelEdit() auch während commitEdit() aufgerufen wird, wenn
   * sich das unterliegende Modell ändert.
   */
  private boolean ignore_cancelEdit;

  public BnFxTextFieldListCell(Path cellPath) {
    this.cellPath = cellPath;
    textField.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
      if (e.getCode() == KeyCode.ESCAPE) {
        cancelEdit();
      }
    });
    textField.setOnAction(e -> {
      commitEdit(getItem());
    });
    setGraphic(textField);
    BINDER.bind(textField, textPmProperty);
  }

  @Override
  public void updateItem(PM item, boolean empty) {
    boolean changed = getItem() != item;
    if (changed && getItem() != null) {
      observation.stop();
    }
    super.updateItem(item, empty);
    if (changed && item != null) {
      observation = new PathObservation(item, cellPath);
      observation.addPropertyChangeListener("target", evt -> {
        PresentationModel target = observation.getTarget();
        updateTarget((ITextPM) target);
      });
      updateTarget((ITextPM) observation.getTarget());
    }

    if (isEditing()) {
      ITextPM cellPm = item == null ? null : (ITextPM) PropertySupport.get(item).getProperty(cellPath);
      String text = cellPm == null ? null : cellPm.getText();
      setText(text);
      textField.setText(text);
      setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    } else {
      setContentDisplay(ContentDisplay.TEXT_ONLY);
      if (empty) {
        setText(null);
      } else {
        ITextPM cellPm = item == null ? null : (ITextPM) PropertySupport.get(item).getProperty(cellPath);
        String text = cellPm == null ? null : cellPm.getText();
        setText(text);
      }
      updateStyle();
    }
    updateEditableFlag();
  }

  private void updateTarget(ITextPM target) {
    boolean changed = this.target != target;
    if (changed && this.target != null) {
      this.target.removePropertyChangeListener(pcl);
    }
    this.target = target;
    if (changed && this.target != null) {
      this.target.addPropertyChangeListener(pcl);
    }
  }

  private void updateStyle() {
    PM rowPm = getItem();
    ITextPM cellPm = rowPm == null ? null : (ITextPM) PropertySupport.get(rowPm).getProperty(cellPath);
    ValidationState v = cellPm == null ? null : cellPm.getValidationState();
    ObservableList<String> styles = getStyleClass();
    if (v != null) {
      if (!styles.contains("validation-error")) {
        styles.add("validation-error");
      }
    } else {
      styles.remove("validation-error");
    }
  }

  private void updateEditableFlag() {
    /*
     * Hier gibt es leider einen Bug, für den wir keinen Work-Around kennen.
     *
     * Das Setzten der Editierbarkeit auf "true" wird von der Gui nicht beachtet, wenn diese Zelle
     * aktuell selektiert ist, den Fokus hat, und kurz vorher versucht wurde, die Zelle zu editieren.
     *
     */
    PM rowPm = getItem();
    ITextPM cellPm = rowPm == null ? null : (ITextPM) PropertySupport.get(rowPm).getProperty(cellPath);
    boolean editable = cellPm == null ? false : cellPm.isEditable();
    setEditable(editable);
  }

  @Override
  public void startEdit() {
    if (!isEditable() || !getListView().isEditable()) {
      return;
    }
    super.startEdit();

    PM rowPm = getItem();
    ITextPM cellPm = rowPm == null ? null : (ITextPM) PropertySupport.get(rowPm).getProperty(cellPath);
    String text = cellPm == null ? null : cellPm.getText();
    oldText = text;

    textPmProperty.setPresentationModel(cellPm);
    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    textField.requestFocus();
    textField.selectAll();
  }

  @Override
  public void commitEdit(PM newValue) {
    ignore_cancelEdit = true;
    try {
      super.commitEdit(newValue);

      PM rowPm = newValue;
      ITextPM cellPm = rowPm == null ? null : (ITextPM) PropertySupport.get(rowPm).getProperty(cellPath);
      if (cellPm != null) {
        cellPm.setText(textField.getText());
      }
      setText(textField.getText());
      setContentDisplay(ContentDisplay.TEXT_ONLY);
      textPmProperty.setPresentationModel(null);
    } finally {
      ignore_cancelEdit = false;
    }
  }

  @Override
  public void cancelEdit() {
    if (ignore_cancelEdit) {
      return;
    }
    super.cancelEdit();

    PM rowPm = getItem();
    ITextPM cellPm = rowPm == null ? null : (ITextPM) PropertySupport.get(rowPm).getProperty(cellPath);
    cellPm.setText(oldText);
    setText(oldText);
    setContentDisplay(ContentDisplay.TEXT_ONLY);
    textPmProperty.setPresentationModel(null);
  }

}

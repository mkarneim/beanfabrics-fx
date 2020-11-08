package org.beanfabrics.javafx.binder;

import static java.util.Objects.requireNonNull;
import javax.annotation.Nullable;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.tableview.BnFxCheckBoxTableCell;
import org.beanfabrics.javafx.tableview.BnFxComboBoxTableCell;
import org.beanfabrics.javafx.tableview.BnFxTextFieldTableCell;
import org.beanfabrics.model.IBooleanPM;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.PresentationModel;
import com.google.common.base.Strings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * This {@link TableColumnBuilder} is a Builder for conveniently Dictionary creating a
 * {@link TableColumn}. Internally the {@link TableViewBinding#createTableColumn(String, Path)}
 * method is used as the factory method.
 */
public class TableColumnBuilder<ROW extends PresentationModel, CELL extends PresentationModel> {
  private final Path path;

  private @Nullable Pos alignment;
  private @Nullable Callback<TableColumn<ROW, CELL>, TableCell<ROW, CELL>> cellFactory;
  private @Nullable ContextMenu contextMenu;
  private @Nullable Node graphic;
  private @Nullable Double maxWidth;
  private @Nullable Double minWidth;
  private @Nullable Double prefWidth;
  private @Nullable Boolean resizable;
  private @Nullable Boolean sortable;
  private @Nullable String text;
  private @Nullable Boolean visible;

  public TableColumnBuilder(String path) {
    this.path = new Path(path);
  }

  public TableColumnBuilder(Path path) {
    this.path = requireNonNull(path, "path");
  }

  protected TableColumnBuilder( //
      @Nullable Path path, //
      @Nullable Pos alignment, //
      @Nullable Callback<TableColumn<ROW, CELL>, TableCell<ROW, CELL>> cellFactory, //
      @Nullable ContextMenu contextMenu, //
      @Nullable Node graphic, //
      @Nullable Double maxWidth, //
      @Nullable Double minWidth, //
      @Nullable Double prefWidth, //
      @Nullable Boolean resizable, //
      @Nullable Boolean sortable, //
      @Nullable String text, //
      @Nullable Boolean visible //
  ) {
    this.path = path;
    this.alignment = alignment;
    this.cellFactory = cellFactory;
    this.contextMenu = contextMenu;
    this.graphic = graphic;
    this.maxWidth = maxWidth;
    this.minWidth = minWidth;
    this.prefWidth = prefWidth;
    this.resizable = resizable;
    this.sortable = sortable;
    this.text = text;
    this.visible = visible;
  }

  public TableColumnBuilder<ROW, CELL> withAlignment(@Nullable Pos pos) {
    alignment = pos;
    return this;
  }

  public TableColumnBuilder<ROW, CELL> withContextMenu(@Nullable ContextMenu contextMenu) {
    this.contextMenu = contextMenu;
    return this;
  }

  public TableColumnBuilder<ROW, CELL> withGraphic(@Nullable Node graphic) {
    this.graphic = graphic;
    return this;
  }

  public TableColumnBuilder<ROW, CELL> withMaxWidth(double maxWidth) {
    this.maxWidth = maxWidth;
    return this;
  }

  public TableColumnBuilder<ROW, CELL> withMinWidth(double minWidth) {
    this.minWidth = minWidth;
    return this;
  }

  public TableColumnBuilder<ROW, CELL> withPrefWidth(double prefWidth) {
    this.prefWidth = prefWidth;
    return this;
  }

  public TableColumnBuilder<ROW, CELL> withResizable(boolean resizable) {
    this.resizable = resizable;
    return this;
  }

  public TableColumnBuilder<ROW, CELL> withSortable(boolean sortable) {
    this.sortable = sortable;
    return this;
  }

  public TableColumnBuilder<ROW, CELL> withText(@Nullable String text) {
    this.text = text;
    return this;
  }

  public TableColumnBuilder<ROW, CELL> withVisible(boolean visible) {
    this.visible = visible;
    return this;
  }

  @SuppressWarnings("hiding")
  public <ROW extends PresentationModel, CELL extends IBooleanPM> TableColumn<ROW, CELL> buildAsCheckBox() {
    return this.<ROW, CELL>asCheckBox().build();
  }

  @SuppressWarnings("hiding")
  private <ROW extends PresentationModel, CELL extends IBooleanPM> TableColumnBuilder<ROW, CELL> asCheckBox() {
    return withCellFactory(param -> new BnFxCheckBoxTableCell<>());
  }

  @SuppressWarnings("hiding")
  public <ROW extends PresentationModel, CELL extends ITextPM> TableColumn<ROW, CELL> buildAsComboBox() {
    return this.<ROW, CELL>asComboBox().build();
  }

  @SuppressWarnings("hiding")
  private <ROW extends PresentationModel, CELL extends ITextPM> TableColumnBuilder<ROW, CELL> asComboBox() {
    return withCellFactory(param -> new BnFxComboBoxTableCell<>());
  }

  @SuppressWarnings("hiding")
  public <ROW extends PresentationModel, CELL extends ITextPM> TableColumn<ROW, CELL> buildAsEditableComboBox() {
    return this.<ROW, CELL>asEditableComboBox().build();
  }

  @SuppressWarnings("hiding")
  private <ROW extends PresentationModel, CELL extends ITextPM> TableColumnBuilder<ROW, CELL> asEditableComboBox() {
    return withCellFactory(param -> new BnFxComboBoxTableCell<>(true));
  }

  @SuppressWarnings("hiding")
  public <ROW extends PresentationModel, CELL extends ITextPM> TableColumn<ROW, CELL> buildAsTextField() {
    return this.<ROW, CELL>asTextField().build();
  }

  @SuppressWarnings("hiding")
  private <ROW extends PresentationModel, CELL extends ITextPM> TableColumnBuilder<ROW, CELL> asTextField() {
    return withCellFactory(param -> new BnFxTextFieldTableCell<>());
  }

  @SuppressWarnings("hiding")
  public <ROW extends PresentationModel, CELL extends PresentationModel> TableColumn<ROW, CELL> buildWithCellFactory(
      Callback<TableColumn<ROW, CELL>, TableCell<ROW, CELL>> cellFactory) {
    return withCellFactory(cellFactory).build();
  }

  @SuppressWarnings("hiding")
  private <ROW extends PresentationModel, CELL extends PresentationModel> TableColumnBuilder<ROW, CELL> withCellFactory(
      Callback<TableColumn<ROW, CELL>, TableCell<ROW, CELL>> cellFactory) {
    return new TableColumnBuilder<>(path, alignment, cellFactory, contextMenu, graphic, maxWidth, minWidth, prefWidth,
        resizable, sortable, text, visible);
  }

  private TableColumn<ROW, CELL> build() {
    TableColumn<ROW, CELL> result = TableViewBinding.createTableColumn(path);
    if (alignment != null) {
      String value = alignment.name().replaceAll("_", "-");
      addStyle(result, String.format("-fx-alignment: %s;", value));
    }
    if (cellFactory != null) {
      result.setCellFactory(cellFactory);
    }
    if (contextMenu != null) {
      result.setContextMenu(contextMenu);
    }
    if (graphic != null) {
      result.setGraphic(graphic);
    }
    if (maxWidth != null) {
      result.setMaxWidth(maxWidth);
    }
    if (minWidth != null) {
      result.setMinWidth(minWidth);
    }
    if (prefWidth != null) {
      result.setPrefWidth(prefWidth);
    }
    if (resizable != null) {
      result.setResizable(resizable);
    }
    if (sortable != null) {
      result.setSortable(sortable);
    }
    if (text != null) {
      result.setText(text);
    }
    if (visible != null) {
      result.setVisible(visible);
    }
    return result;
  }

  private static void addStyle(TableColumn<?, ?> column, String value) {
    requireNonNull(value, "value");
    String oldStyle = Strings.nullToEmpty(column.getStyle());
    column.setStyle(oldStyle + value);
  }
}

package org.beanfabrics.javafx.binder;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.requireNonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.event.ElementChangedEvent;
import org.beanfabrics.event.ElementsAddedEvent;
import org.beanfabrics.event.ElementsDeselectedEvent;
import org.beanfabrics.event.ElementsRemovedEvent;
import org.beanfabrics.event.ElementsReplacedEvent;
import org.beanfabrics.event.ElementsSelectedEvent;
import org.beanfabrics.event.ListListener;
import org.beanfabrics.javafx.property.ListPmProperty;
import org.beanfabrics.javafx.property.PresentationModelProperty;
import org.beanfabrics.model.IListPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.model.SortKey;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.util.Callback;

public class TableViewBinding<IT extends PresentationModel> implements BnBinding {
  public static final String PROP_COLUMN_PATH = "TableViewBinder.column.path";

  private final TableView<IT> control;
  private final ListPmProperty property;
  private final ChangeListener<IListPM<?>> changeListender = new ChangeListener<IListPM<?>>() {
    @SuppressWarnings("unchecked")
    @Override
    public void changed(ObservableValue<? extends IListPM<?>> observable, IListPM<?> oldValue, IListPM<?> newValue) {
      if (oldValue != null) {
        disconnect((IListPM<IT>) oldValue);
      }
      if (newValue != null) {
        connect((IListPM<IT>) newValue);
      }
    }
  };
  ListChangeListener<? super TableColumn<IT, ?>> columnsChangelistener =
      (ListChangeListener<? super TableColumn<IT, ?>>) (e) -> {
        @SuppressWarnings("unchecked")
        IListPM<IT> listPm = (IListPM<IT>) getProperty().get();
        if (listPm != null) {
          applySelectionToControl(listPm);
        }
      };
  private ListChangeListener<? super IT> sListener;
  private ChangeListener<TableViewSelectionModel<IT>> cListener;
  private ListListener lListener;

  public TableViewBinding(TableView<IT> control, IModelProvider modelProvider, Path path) {
    this(control, new ListPmProperty(modelProvider, path));
  }

  @SuppressWarnings("unchecked")
  public TableViewBinding(TableView<IT> control, ListPmProperty property) {
    this.control = checkNotNull(control, "control == null!");
    this.property = checkNotNull(property, "property == null!");
    this.property.addListener(changeListender);
    IListPM<?> listPm = property.get();
    if (listPm != null) {
      connect((IListPM<IT>) listPm);
    }

    control.getColumns().addListener(columnsChangelistener);
  }

  private ListPmProperty getProperty() {
    return property;
  }

  @Override
  public void dispose() {
    control.setSortPolicy(null);
    control.setItems(FXCollections.emptyObservableList());
    control.setDisable(true);
    @SuppressWarnings("unchecked")
    IListPM<IT> listPm = (IListPM<IT>) property.get();
    if (listPm != null) {
      disconnect(listPm);
    }
  }

  private void disconnect(IListPM<IT> listPm) {
    control.getSelectionModel().getSelectedItems().removeListener(sListener);
    control.selectionModelProperty().removeListener(cListener);
    control.getColumns().removeListener(columnsChangelistener);
    listPm.removeListListener(lListener);
  }

  private void connect(IListPM<IT> listPm) {
    ObservableList<IT> sourceList = FXCollections.observableArrayList();
    AtomicBoolean isSorting = new AtomicBoolean(false);
    AtomicBoolean pendingSelectionChange = new AtomicBoolean(false);

    sListener = new ListChangeListener<IT>() {
      @Override
      public void onChanged(Change<? extends IT> c) {
        boolean isSingleSelectionMode = control.getSelectionModel().getSelectionMode() == SelectionMode.SINGLE;
        if (!isSingleSelectionMode && pendingSelectionChange.get()) {
          return;
        }
        pendingSelectionChange.set(true);
        try {
          while (c.next()) {
            if (c.wasPermutated()) {
              int from = c.getFrom();
              int to = c.getTo();
              listPm.getSelection().setInterval(from, to);
            } else if (c.wasUpdated()) {
              // TODO ???
              throw new IllegalStateException();
            } else {
              if (c.getRemovedSize() > 0) {
                List<? extends IT> removed = c.getRemoved();
                listPm.getSelection().removeAll(removed);
              }
              if (c.getAddedSize() > 0) {
                List<? extends IT> added = c.getAddedSubList();
                if (isSingleSelectionMode) {
                  listPm.getSelection().clear();
                }
                listPm.getSelection().addAll(added);
              }
            }
          }
        } finally {
          // Work Around Start for issue #42 - Syncing of TableView selection sometimes erroneous

          // System.out.println(" listPm.getSelection()=" + listPm.getSelection().toCollection());
          // System.out.println(
          // " control.getSelectionModel().getSelectedItems()=" +
          // control.getSelectionModel().getSelectedItems());
          if (listPm.getSelection().size() == 2) {
            int[] selIndices = toIntArray(control.getSelectionModel().getSelectedIndices());
            listPm.getSelection().setIndexes(selIndices);
          }
          // Work Around End
          pendingSelectionChange.set(false);
        }
      }

      private int[] toIntArray(ObservableList<Integer> intList) {
        int[] result = new int[intList.size()];
        int index = 0;
        for (Integer element : intList) {
          result[index++] = element;
        }
        return result;
      }
    };
    control.getSelectionModel().getSelectedItems().addListener(sListener);
    /////
    cListener = new ChangeListener<TableViewSelectionModel<IT>>() {
      @Override
      public void changed(ObservableValue<? extends TableViewSelectionModel<IT>> obs, TableViewSelectionModel<IT> oldV,
          TableViewSelectionModel<IT> newV) {
        oldV.getSelectedItems().removeListener(sListener);
        oldV.getSelectedItems().addListener(sListener);
      }
    };
    control.selectionModelProperty().addListener(cListener);
    /////
    lListener = new ListListener() {

      @Override
      public void elementsSelected(ElementsSelectedEvent evt) {
        if (pendingSelectionChange.get()) {
          return;
        }
        pendingSelectionChange.set(true);
        try {
          int begin = evt.getBeginIndex();
          if (evt.getLength() == 1) {
            control.getSelectionModel().select(begin);
          } else if (evt.getLength() > 1) {
            control.getSelectionModel().selectRange(begin, begin + evt.getLength());
          }
          focusRow(begin);
        } finally {
          pendingSelectionChange.set(false);
        }
      }

      @Override
      public void elementsDeselected(ElementsDeselectedEvent evt) {
        if (pendingSelectionChange.get()) {
          return;
        }
        pendingSelectionChange.set(true);
        try {
          int begin = evt.getBeginIndex();
          if (evt.getLength() == 1) {
            control.getSelectionModel().clearSelection(begin);
          } else if (evt.getLength() > 1) {
            int end = begin + evt.getLength();
            for (int i = begin; i < end; ++i) {
              control.getSelectionModel().clearSelection(i);
            }
          }
        } finally {
          pendingSelectionChange.set(false);
        }
      }

      @Override
      public void elementsAdded(ElementsAddedEvent evt) {
        if (pendingSelectionChange.get()) {
          return;
        }
        pendingSelectionChange.set(true);
        try {
          int begin = evt.getBeginIndex();
          if (evt.getLength() == 1) {
            IT elem = listPm.getAt(begin);
            sourceList.add(elem);
          } else if (evt.getLength() > 1) {
            int end = begin + evt.getLength();
            List<IT> toAdd = new ArrayList<>(evt.getLength());
            for (int i = begin; i < end; ++i) {
              IT elem = listPm.getAt(i);
              toAdd.add(elem);
            }
            sourceList.addAll(toAdd);
          }
          if (!isSorting.get()) {
            control.getSortOrder().clear();
          }
        } finally {
          pendingSelectionChange.set(false);
        }
      }

      @Override
      public void elementsRemoved(ElementsRemovedEvent evt) {
        if (pendingSelectionChange.get()) {
          return;
        }
        pendingSelectionChange.set(true);
        try {
          int begin = evt.getBeginIndex();
          if (evt.getLength() == 1) {
            sourceList.remove(begin);
          } else if (evt.getLength() > 1) {
            int end = begin + evt.getLength();
            sourceList.remove(begin, end);
          }
          if (!isSorting.get()) {
            control.getSortOrder().clear();
          }
        } finally {
          pendingSelectionChange.set(false);
        }
      }

      @Override
      public void elementsReplaced(ElementsReplacedEvent evt) {
        if (pendingSelectionChange.get()) {
          return;
        }
        pendingSelectionChange.set(true);
        try {
          int begin = evt.getBeginIndex();
          if (evt.getLength() == 1) {
            IT elem = listPm.getAt(begin);
            sourceList.set(begin, elem);
          } else if (evt.getLength() > 1) {
            int end = begin + evt.getLength();
            for (int i = begin; i < end; ++i) {
              IT elem = listPm.getAt(i);
              sourceList.set(i, elem);
            }
          }
          if (!isSorting.get()) {
            control.getSortOrder().clear();
          }
          applySelectionToControl(listPm);
        } finally {
          pendingSelectionChange.set(false);
        }
      }

      @Override
      public void elementChanged(ElementChangedEvent evt) {
        // FIXME (mka, 14.4.2018) We have to do something here (or at another place), since changes inside
        // the
        // row models are not reflected in the GUI unless the cells are scrolled out and in, or edited.
      }
    };
    listPm.addListListener(lListener);
    /////
    sourceList.setAll(listPm.toCollection());
    control.setItems(sourceList);
    applySelectionToControl(listPm);
    /////
    Callback<TableView<IT>, Boolean> callback = new Callback<TableView<IT>, Boolean>() {
      @Override
      public Boolean call(TableView<IT> table) {
        ObservableList<TableColumn<IT, ?>> sortOrder = table.getSortOrder();
        List<SortKey> newSortKeys = new ArrayList<>();
        for (TableColumn<IT, ?> col : sortOrder) {
          Path path = (Path) col.getProperties().get(PROP_COLUMN_PATH);
          checkNotNull(path, "path == null!");
          boolean ascending = col.getSortType() == SortType.ASCENDING;
          newSortKeys.add(new SortKey(ascending, path));
        }
        isSorting.set(true);
        try {
          listPm.sortBy(newSortKeys);
        } finally {
          isSorting.set(false);
        }
        return true;
      }
    };
    control.setSortPolicy(callback);
    control.setDisable(false);
  }

  private void applySelectionToControl(IListPM<IT> listPm) {
    int[] selectedIndexes = listPm.getSelection().getIndexes();
    if (selectedIndexes.length == 1) {
      if (!control.getSelectionModel().isSelected(selectedIndexes[0])) {
        control.getSelectionModel().selectIndices(selectedIndexes[0]);
      }
      focusRow(selectedIndexes[0]);
    } else if (selectedIndexes.length > 1) {
      int[] rest = new int[selectedIndexes.length - 1];
      System.arraycopy(selectedIndexes, 1, rest, 0, rest.length);
      // replace first and last element since selectIndices(other, rest) expects the index of the main
      // selected item to be the last element of rest.
      int other = rest[rest.length - 1];
      rest[rest.length - 1] = selectedIndexes[0];
      control.getSelectionModel().selectIndices(other, rest);
      focusRow(selectedIndexes[0]);
    }
  }

  private void focusRow(int index) {
    if (!control.getColumns().isEmpty()) {
      TableColumn<IT, ?> column = control.getColumns().get(0);
      control.getFocusModel().focus(index, column);
    }
  }

  public static <ROW extends PresentationModel, CELL extends PresentationModel> TableColumn<ROW, CELL> createTableColumn(
      Path path) {
    requireNonNull(path, "path");
    TableColumn<ROW, CELL> result = new TableColumn<>();
    result.setCellValueFactory(param -> new PresentationModelProperty<>(new ModelProvider(param.getValue()), path));
    result.getProperties().put(PROP_COLUMN_PATH, path);
    return result;
  }
}

package org.beanfabrics.javafx.binder;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.event.ElementChangedEvent;
import org.beanfabrics.event.ElementsAddedEvent;
import org.beanfabrics.event.ElementsDeselectedEvent;
import org.beanfabrics.event.ElementsRemovedEvent;
import org.beanfabrics.event.ElementsReplacedEvent;
import org.beanfabrics.event.ElementsSelectedEvent;
import org.beanfabrics.event.ListListener;
import org.beanfabrics.javafx.listview.BnFxTextFieldListCell;
import org.beanfabrics.javafx.property.ListPmProperty;
import org.beanfabrics.model.IListPM;
import org.beanfabrics.model.PresentationModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.util.Callback;

public class ListViewBinding<IT extends PresentationModel> implements BnBinding {

  private final ListView<IT> control;
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
  private ListChangeListener<? super IT> sListener;
  private ChangeListener<? super MultipleSelectionModel<IT>> cListener;
  private ListListener lListener;

  public ListViewBinding(ListView<IT> control, IModelProvider IModelProvider, Path path) {
    this(control, new ListPmProperty(IModelProvider, path));
  }

  @SuppressWarnings("unchecked")
  public ListViewBinding(ListView<IT> control, ListPmProperty property) {
    this.control = checkNotNull(control, "control == null!");
    this.property = checkNotNull(property, "property == null!");
    this.property.addListener(changeListender);
    IListPM<?> listPm = property.get();
    if (listPm != null) {
      connect((IListPM<IT>) listPm);
    }
  }

  @Override
  public void dispose() {
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
    listPm.removeListListener(lListener);
  }

  private void connect(IListPM<IT> listPm) {
    ObservableList<IT> sourceList = FXCollections.observableArrayList();
    AtomicBoolean pendingSelectionChange = new AtomicBoolean(false);
    /////
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
          pendingSelectionChange.set(false);
        }
      }
    };
    control.getSelectionModel().getSelectedItems().addListener(sListener);
    /////
    cListener = new ChangeListener<MultipleSelectionModel<IT>>() {
      @Override
      public void changed(ObservableValue<? extends MultipleSelectionModel<IT>> obs, MultipleSelectionModel<IT> oldV,
          MultipleSelectionModel<IT> newV) {
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
          applySelectionToControl(listPm);
        } finally {
          pendingSelectionChange.set(false);
        }
      }

      @Override
      public void elementChanged(ElementChangedEvent evt) {
        // We don't need to do anything here since the ListView and our cell factory already handle the
        // updates
        // correctly.
      }
    };
    listPm.addListListener(lListener);
    /////
    sourceList.setAll(listPm.toCollection());
    control.setItems(sourceList);
    applySelectionToControl(listPm);
    control.setDisable(false);
  }

  private void applySelectionToControl(IListPM<IT> listPm) {
    int[] selectedIndexes = listPm.getSelection().getIndexes();
    if (selectedIndexes.length == 1) {
      control.getSelectionModel().selectIndices(selectedIndexes[0]);
    } else if (selectedIndexes.length > 1) {
      int[] rest = new int[selectedIndexes.length - 1];
      System.arraycopy(selectedIndexes, 1, rest, 0, rest.length);
      control.getSelectionModel().selectIndices(selectedIndexes[0], rest);
    }
  }

  public static <IT extends PresentationModel> Callback<ListView<IT>, ListCell<IT>> createListViewCellFactory(
      Path cellPath) {
    Callback<ListView<IT>, ListCell<IT>> cfactory = new Callback<ListView<IT>, ListCell<IT>>() {
      @Override
      public ListCell<IT> call(ListView<IT> param) {
        return new BnFxTextFieldListCell<>(cellPath);
      }
    };
    return cfactory;
  }
}

package org.beanfabrics.javafx;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import org.assertj.core.api.Assertions;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.binder.BnFxBinder;
import org.beanfabrics.model.MapPM;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class BnFxBinder_TableView_Test extends ApplicationTest {

  RowPM[] row;
  MapPM<Integer, RowPM> mapPm = new MapPM<>();
  ModelProvider modelProvider = new ModelProvider();
  Path path = new Path("this");
  TableView<RowPM> control;

  @Before
  public void before() {
    row = new RowPM[3];
    row[0] = new RowPM(0, "zero");
    row[1] = new RowPM(1, "one");
    row[2] = new RowPM(2, "two");
    mapPm.put(0, row[0]);
    mapPm.put(1, row[1]);
    modelProvider.setPresentationModel(mapPm);
    control = new TableView<>();
    BnFxBinder.bind(control, modelProvider, path);
  }

  @Test
  public void test_items_size() {
    // Given:

    // When:

    // Then:
    Assertions.assertThat(control.getItems().size()).isEqualTo(2);
  }

  @Test
  public void test_selectedItem() {
    // Given:

    // When:
    mapPm.getSelectedKeys().add(0);

    // Then:
    Assertions.assertThat(control.getSelectionModel().getSelectedItem()).isEqualTo(row[0]);
  }

  @Test
  public void test_selectedItems_single_selection() {
    // Given:

    // When:
    mapPm.getSelectedKeys().add(0);

    // Then:
    Assertions.assertThat(control.getSelectionModel().getSelectedItems()).containsExactly(row[0]);
  }

  @Test
  public void test_selectedItems_multiple_selections() {
    // Given:
    control.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    // When:
    mapPm.getSelectedKeys().add(0);
    mapPm.getSelectedKeys().add(1);

    // Then:
    Assertions.assertThat(control.getSelectionModel().getSelectedItems()).containsOnly(row[0], row[1]);
  }

  @Test
  public void test_selectedItems_multiple_selections_with_single_selection_mode() {
    // Given:
    control.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    // When:
    mapPm.getSelectedKeys().add(0);
    mapPm.getSelectedKeys().add(1);

    // Then:
    Assertions.assertThat(control.getSelectionModel().getSelectedItems()).containsOnly(row[1]);
    Assertions.assertThat(mapPm.getSelection()).containsOnly(row[1]);
  }
}

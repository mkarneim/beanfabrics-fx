package org.beanfabrics.javafx;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.binder.BnFxBinder;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Operation;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TableViewTestApp2 extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    SamplePM pm = new SamplePM();
    ModelProvider localModelProvider = new ModelProvider();
    localModelProvider.setPresentationModel(pm);

    // Left Table
    TableView<RowPM> tableView = new TableView<>();
    BnFxBinder.bind(tableView, localModelProvider, new Path("this.rows"));

    tableView.getColumns()
        .add(BnFxBinder.newTableColumn("text").withText("text").withPrefWidth(200).buildAsTextField());
    tableView.getColumns()
        .add(BnFxBinder.newTableColumn("number").withText("Number").withAlignment(Pos.CENTER_RIGHT).buildAsTextField());
    tableView.getColumns().add(BnFxBinder.newTableColumn("bool").withText("Bool").withPrefWidth(100).buildAsCheckBox());

    tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    TextArea taStatus = new TextArea();
    BnFxBinder.bind(taStatus, localModelProvider, new Path("status"));
    taStatus.setPadding(new Insets(20));

    Button btnReload = new Button("reload");
    BnFxBinder.bind(btnReload, localModelProvider, new Path("reload"));
    Node vbox = new VBox(btnReload);
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(vbox);
    borderPane.setCenter(tableView);
    borderPane.setBottom(taStatus);

    ScrollPane root = new ScrollPane();
    root.setContent(borderPane);

    stage.setScene(new Scene(root));
    stage.show();
  }

  private static class SamplePM extends AbstractPM {
    TextPM status = new TextPM();
    ListPM<RowPM> rows = new ListPM<>();
    OperationPM reload = new OperationPM();

    public SamplePM() {
      PMManager.setup(this);
      reload();
    }

    @Operation
    public void reload() {
      rows.clear();
      for (int i = 0; i < 100; ++i) {
        rows.add(new RowPM(i));
      }
    }

    @OnChange(path = "rows")
    void onChange() {
      status.setText(
          "selection size=" + rows.getSelection().size() + "\n" + "selection=" + rows.getSelection().toCollection());
    }
  }

  private static class RowPM extends AbstractPM {
    TextPM text = new TextPM();
    IntegerPM number = new IntegerPM();
    BooleanPM bool = new BooleanPM();

    public RowPM(int num) {
      PMManager.setup(this);
      number.setInteger(num);
      text.setText(String.format("string-%s", num));
      bool.setBoolean(true);
    }

  }

}

package org.beanfabrics.javafx;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.binder.BnFxBinder;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.Options;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Validation;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TableViewTestApp extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    ListPM<RowPM> pm = new ListPM<>();
    for (int i = 1; i < 100; ++i) {
      pm.add(new RowPM(i));
    }
    ModelProvider localModelProvider = new ModelProvider();
    localModelProvider.setPresentationModel(pm);

    // Left Table
    TableView<RowPM> tableViewLeft = new TableView<>();
    BnFxBinder.bind(tableViewLeft, localModelProvider, new Path("this"));

    Label graphicLeft = new Label("Column Header Text");
    graphicLeft.setTooltip(new Tooltip("Some tooltip visible when hovering above the column header"));

    // This expands the header's sensible zone for
    graphicLeft.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    tableViewLeft.getColumns()
        .add(BnFxBinder.newTableColumn("text").withGraphic(graphicLeft).withPrefWidth(200).buildAsTextField());
    tableViewLeft.getColumns()
        .add(BnFxBinder.newTableColumn("number").withText("Number").withAlignment(Pos.CENTER_RIGHT).buildAsTextField());
    tableViewLeft.getColumns()
        .add(BnFxBinder.newTableColumn("choice").withText("Choice").withPrefWidth(100).buildAsComboBox());
    tableViewLeft.getColumns()
        .add(BnFxBinder.newTableColumn("bool").withText("Bool").withPrefWidth(100).buildAsCheckBox());
    tableViewLeft.getColumns()
        .add(BnFxBinder.newTableColumn("bool").withText("Bool 2").withPrefWidth(100).buildAsCheckBox());
    tableViewLeft.getColumns()
        .add(BnFxBinder.newTableColumn("textIsValid").withText("Text is valid").withPrefWidth(100).buildAsCheckBox());

    tableViewLeft.setEditable(true);

    // Right Table
    TableView<RowPM> tableViewRight = new TableView<>();
    BnFxBinder.bind(tableViewRight, localModelProvider, new Path("this"));

    Label graphicRight = new Label("Column Header Text");
    graphicRight.setTooltip(new Tooltip("Some tooltip visible when hovering above the column header"));

    // This expands the header's sensible zone for
    graphicRight.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    tableViewRight.getColumns()
        .add(BnFxBinder.newTableColumn("text").withGraphic(graphicRight).withPrefWidth(200).buildAsTextField());
    tableViewRight.getColumns()
        .add(BnFxBinder.newTableColumn("number").withText("Number").withAlignment(Pos.CENTER_RIGHT).buildAsTextField());
    tableViewRight.getColumns()
        .add(BnFxBinder.newTableColumn("choice").withText("Choice").withPrefWidth(100).buildAsComboBox());
    tableViewRight.getColumns()
        .add(BnFxBinder.newTableColumn("bool").withText("Bool").withPrefWidth(100).buildAsCheckBox());
    tableViewRight.getColumns()
        .add(BnFxBinder.newTableColumn("bool").withText("Bool 2").withPrefWidth(100).buildAsCheckBox());
    tableViewRight.getColumns()
        .add(BnFxBinder.newTableColumn("textIsValid").withText("Text is valid").withPrefWidth(100).buildAsCheckBox());

    tableViewRight.setEditable(true);


    CheckBox cb = new CheckBox("Table is Editable");
    cb.setSelected(true);
    CheckBox cb2 = new CheckBox("Check/Uncheck all");
    cb2.setSelected(true);
    cb2.setOnAction((e) -> {
      for (RowPM rowPM : pm) {
        rowPM.bool.setBoolean(cb2.isSelected());
      }
    });
    cb.selectedProperty().bindBidirectional(tableViewLeft.editableProperty());
    HBox hbox = new HBox(cb, cb2);
    hbox.setSpacing(20);

    TextArea ta = new TextArea();
    ta.setPadding(new Insets(20));

    SplitPane splitPane = new SplitPane();
    splitPane.getItems().addAll(tableViewLeft, tableViewRight);

    BorderPane borderPane = new BorderPane();
    borderPane.setTop(hbox);
    borderPane.setCenter(splitPane);
    borderPane.setBottom(ta);

    ScrollPane root = new ScrollPane();
    root.setContent(borderPane);

    stage.setScene(new Scene(root));
    stage.show();
  }

  private static class RowPM extends AbstractPM {
    TextPM text = new TextPM();
    IntegerPM number = new IntegerPM();
    TextPM choice = new TextPM();
    BooleanPM bool = new BooleanPM();
    BooleanPM textIsValid = new BooleanPM();

    public RowPM(int num) {
      Options<String> options = Options.create("one", "two", "three");
      options.put(null, "");
      choice.setEditable(false);
      choice.setOptions(options);
      choice.setText("asdkjajdajdkljalkjdlka");
      number.setEditable(true);
      number.setInteger(num);
      text.setEditable(false);
      text.setText(String.format("string-%s", num));
      PMManager.setup(this);
      bool.setEditable(true);
      bool.setBoolean(true);
    }

    @OnChange(path = "bool")
    void updateChoice() {
      choice.setEditable(bool.getBoolean());
    }

    @OnChange(path = "bool")
    void updateText() {
      text.setEditable(bool.getBoolean());
    }

    @OnChange(path = "choice.text")
    void updateNumber() {
      if (!number.isEmpty() && number.isValid()) {
        number.setInteger(number.getInteger() + 1);
      }
    }

    @Validation(path = "text", message = "xxxxxxxxxxx")
    boolean isTextValid() {
      if (!choice.isEmpty() && choice.isValid() && choice.getText().equals("one")) {
        return text.getText().equals("1");
      }
      return true;
    }

    @OnChange(path = "text")
    void updateTextIsValid() {
      textIsValid.setBoolean(text.isValid());
    }

  }

}

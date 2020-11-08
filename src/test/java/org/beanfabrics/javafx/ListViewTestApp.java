package org.beanfabrics.javafx;

import java.time.LocalDate;
import java.util.UUID;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.binder.BnFxBinder;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.model.date.LocalDatePM;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Validation;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ListViewTestApp extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    SchedulePM pm = new SchedulePM();
    pm.fillDummy();

    ModelProvider localModelProvider = new ModelProvider();
    localModelProvider.setPresentationModel(pm);

    ListView<EntryPM> listView = new ListView<>();
    BnFxBinder.bind(listView, localModelProvider, new Path("entries"));
    listView.setCellFactory(BnFxBinder.createListViewCellFactory(new Path("description")));
    listView.setEditable(true);

    TableView<EntryPM> tableView1 = new TableView<>();
    tableView1.setMinWidth(400);
    BnFxBinder.bind(tableView1, localModelProvider, new Path("entries"));
    // tableView1.getColumns().add(BnFxBinder.createTableColumn("Date", new Path("date")));
    tableView1.getColumns().add(BnFxBinder.newTableColumn("date").withText("Date").withPrefWidth(200d)
        .withAlignment(Pos.CENTER_RIGHT).withResizable(false).buildAsTextField());
    tableView1.getColumns().add(BnFxBinder.newTableColumn("topic").withText("Topic").buildAsTextField());
    tableView1.getColumns().add(BnFxBinder.newTableColumn("description").withText("Description").buildAsTextField());
    tableView1.getColumns().add(BnFxBinder.newTableColumn("address.street").withText("Street").buildAsTextField());
    tableView1.getColumns().add(BnFxBinder.newTableColumn("bool").withText("Bool").buildAsCheckBox());
    tableView1.setEditable(true);

    TableView<EntryPM> tableView2 = new TableView<>();
    tableView2.setMinWidth(400);
    BnFxBinder.bind(tableView2, localModelProvider, new Path("entries"));
    tableView2.getColumns().add(BnFxBinder.newTableColumn("date").withText("Date").withPrefWidth(200d)
        .withAlignment(Pos.CENTER_RIGHT).withResizable(false).buildAsTextField());
    tableView2.getColumns().add(BnFxBinder.newTableColumn("topic").withText("Topic").buildAsTextField());
    tableView2.getColumns().add(BnFxBinder.newTableColumn("description").withText("Description").buildAsTextField());
    tableView2.getColumns().add(BnFxBinder.newTableColumn("address.street").withText("Street").buildAsTextField());
    tableView2.getColumns().add(BnFxBinder.newTableColumn("bool").withText("Bool").buildAsCheckBox());
    tableView2.setEditable(true);

    HBox hbox = new HBox();
    hbox.setPadding(new Insets(20));
    hbox.setSpacing(20);
    hbox.getChildren().addAll(listView, tableView1, tableView2);
    Parent root = hbox;
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.setWidth(1100);
    primaryStage.setHeight(400);
  }


  private static class SchedulePM extends AbstractPM {
    private final ListPM<EntryPM> entries = new ListPM<>();

    public SchedulePM() {
      PMManager.setup(this);
    }

    public void fillDummy() {
      for (int i = 0; i < 10; ++i) {
        LocalDate date = LocalDate.of(2000, 05, i + 1);
        String topic = "some topic " + (i + 1);
        entries.add(new EntryPM(date, topic));
      }
    }
  }
  private static class EntryPM extends AbstractPM {
    LocalDatePM date = new LocalDatePM();
    TextPM topic = new TextPM();
    TextPM description = new TextPM();
    AddressPM address = new AddressPM();
    BooleanPM bool = new BooleanPM();

    public EntryPM(LocalDate date, String topic) {
      PMManager.setup(this);
      this.date.setLocalDate(date);
      this.topic.setText(topic);
      address.city.setText(UUID.randomUUID().toString());
      address.street.setText(UUID.randomUUID().toString());
      bool.setBoolean(Math.random() < 0.5);
    }

    @OnChange(path = {"date", "topic"})
    void updateDescription() {
      description.setText(date.getText() + ": " + topic.getText());
    }

    @Validation(path = "description")
    boolean isDescriptionValid() {
      return description.getText().equals(date.getText() + ": " + topic.getText());
    }
  }

  private static class AddressPM extends AbstractPM {
    TextPM city = new TextPM();
    TextPM street = new TextPM();

    public AddressPM() {
      PMManager.setup(this);
    }
  }
}

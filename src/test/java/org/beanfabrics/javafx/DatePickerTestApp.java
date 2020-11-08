package org.beanfabrics.javafx;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.binder.BnFxBinder;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.date.LocalDatePM;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class DatePickerTestApp extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    AppointmentPM pm = new AppointmentPM();
    ModelProvider localModelProvider = new ModelProvider();
    localModelProvider.setPresentationModel(pm);

    DatePicker datePicker1 = new DatePicker();
    BnFxBinder.bind(datePicker1, localModelProvider, new Path("date1"));
    DatePicker datePicker2 = new DatePicker();
    datePicker2.setEditable(false);
    BnFxBinder.bind(datePicker2, localModelProvider, new Path("date2"));

    TextField dateTextField1 = new TextField();
    BnFxBinder.bind(dateTextField1, localModelProvider, new Path("date1"));
    TextField dateTextField2 = new TextField();
    BnFxBinder.bind(dateTextField2, localModelProvider, new Path("date2"));

    HBox hbox = new HBox();
    hbox.setPadding(new Insets(20));
    hbox.setSpacing(20);
    hbox.getChildren().addAll(datePicker1, datePicker2, dateTextField1, dateTextField2);
    Parent root = hbox;
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.setWidth(600);
    primaryStage.setHeight(400);
  }

  private static class AppointmentPM extends AbstractPM {
    LocalDatePM date1 = new LocalDatePM();
    LocalDatePM date2 = new LocalDatePM();

    public AppointmentPM() {
      PMManager.setup(this);
      DateTimeFormatter parser = new DateTimeFormatterBuilder().parseLenient().appendValue(ChronoField.DAY_OF_MONTH, 2)
          .appendLiteral('.').appendValue(ChronoField.MONTH_OF_YEAR, 2).appendLiteral('.')
          .appendValueReduced(ChronoField.YEAR, 2, 2, LocalDate.of(1940, 1, 1)).toFormatter();
      date1.setFormatter(date1.getFormatter(), parser);
      date2.setFormatter(date2.getFormatter(), parser);
    }

  }
}

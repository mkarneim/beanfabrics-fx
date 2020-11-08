package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.PresentationModel;
import com.google.auto.service.AutoService;
import javafx.scene.control.TableView;

@AutoService(Binder.class)
public class TableViewBinder<IT extends PresentationModel> implements Binder<TableView<IT>> {

  @Override
  public BnBinding bind(TableView<IT> control, IModelProvider modelProvider, Path path) {
    return new TableViewBinding<>(control, modelProvider, path);
  }

}

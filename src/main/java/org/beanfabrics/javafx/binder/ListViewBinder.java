package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.PresentationModel;
import com.google.auto.service.AutoService;
import javafx.scene.control.ListView;

@AutoService(Binder.class)
public class ListViewBinder<IT extends PresentationModel> implements Binder<ListView<IT>> {

  @Override
  public BnBinding bind(ListView<IT> control, IModelProvider modelProvider, Path path) {
    return new ListViewBinding<>(control, modelProvider, path);
  }

}

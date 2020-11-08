package org.beanfabrics.javafx.binder;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.beanfabrics.BnModelObserver;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.BooleanPmProperty;
import org.beanfabrics.javafx.property.TextPmProperty;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.PresentationModel;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class BnFxBinder {

  private static final BinderRepository BINDER_REPO = new BinderRepository();

  /**
   * Binds the given view object to the PM accessible at the end of the given path relative to the PM
   * provided by the given model provider
   *
   * @param <T>
   * @param view
   * @param modelProvider
   * @param path
   * @return the new {@link BnBinding}
   */
  public static <T> BnBinding bind(T view, IModelProvider modelProvider, Path path) {
    checkNotNull(view, "view == null!");
    @SuppressWarnings("unchecked")
    Class<T> cls = (Class<T>) view.getClass();
    Binder<? super T> binder = BINDER_REPO.getBinder(cls);
    if (binder == null) {
      throw new IllegalArgumentException(format("Couldn't find a registered Binder for %s!", cls.getName()));
    }
    BnBinding result = binder.bind(view, modelProvider, path);
    return result;
  }

  public static <P extends PresentationModel> BnBinding observe(ModelProvider modelProvider, Path path,
      PmChangeListener<P> listener) {
    checkNotNull(listener, "listener == null!");
    BnModelObserver observer = new BnModelObserver();
    PropertyChangeListener pcl = new PropertyChangeListener() {

      @SuppressWarnings("unchecked")
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        PresentationModel pm = observer.getPresentationModel();
        listener.changed((P) pm);
      }
    };
    observer.addPropertyChangeListener("presentationModel", pcl);
    BnBinding binding = BnFxBinder.bind(observer, modelProvider, path);
    return new BnBinding() {
      @Override
      public void dispose() {
        observer.removePropertyChangeListener("presentationModel", pcl);
        binding.dispose();
      }
    };
  }

  /**
   * Creates a {@link TableColumnBuilder} with the given path.
   *
   * @param <ROW>
   * @param <CELL>
   * @param path
   * @return the new {@link TableColumnBuilder}
   */
  public static <ROW extends PresentationModel, CELL extends PresentationModel> TableColumnBuilder<ROW, CELL> newTableColumn(
      String path) {
    return new TableColumnBuilder<>(path);
  }

  /**
   * Creates a {@link TableColumnBuilder} with the given path.
   *
   * @param <ROW>
   * @param <CELL>
   * @param path
   * @return the new {@link TableColumnBuilder}
   */
  public static <ROW extends PresentationModel, CELL extends PresentationModel> TableColumnBuilder<ROW, CELL> newTableColumn(
      Path path) {
    return new TableColumnBuilder<>(path);
  }

  public static <IT extends PresentationModel> Callback<ListView<IT>, ListCell<IT>> createListViewCellFactory(
      Path cellPath) {
    return ListViewBinding.createListViewCellFactory(cellPath);
  }

  public static <PM extends PresentationModel> TextPmProperty asTextPmProperty(ITextPM pm) {
    return new TextPmProperty(pm);
  }

  public static <PM extends PresentationModel> TextPmProperty asTextPmProperty(PM pm, Path path) {
    ModelProvider provider = new ModelProvider(pm);
    return new TextPmProperty(provider, path);
  }

  public static <PM extends PresentationModel> BooleanPmProperty asBooleanPmProperty(PM pm, Path path) {
    ModelProvider provider = new ModelProvider(pm);
    return new BooleanPmProperty(provider, path);
  }

  /**
   * Binds the given StringProperty to the PM accessible at the end of the given path relative to the
   * PM provided by the given model provider
   *
   * @param property
   * @param modelProvider
   * @param path
   * @deprecated use {@link #bind(Object, IModelProvider, Path)} instead
   */
  @Deprecated
  public static void bindText(StringProperty property, ModelProvider modelProvider, Path path) {
    checkNotNull(property, "property == null!");
    property.bindBidirectional(new TextPmProperty(modelProvider, path).textProperty());
  }

}

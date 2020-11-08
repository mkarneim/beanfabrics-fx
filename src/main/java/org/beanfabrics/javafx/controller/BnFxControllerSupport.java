package org.beanfabrics.javafx.controller;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.PresentationModel;
import com.google.common.reflect.TypeToken;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * The {@link BnFxControllerSupport} is a convenient supporter class ment to be used internally by
 * classes that want to implement the {@link BnFxController} interface.
 *
 * @param <PM>
 */
public class BnFxControllerSupport<PM extends PresentationModel> {

  private final Link link;
  private final Class<PM> pmClass;
  private final ModelProvider localModelProvider;
  private final ObjectProperty<PM> presentationModelProperty = new SimpleObjectProperty<>();

  public BnFxControllerSupport(BnFxController<PM> controller) {
    link = new Link(controller);
    @SuppressWarnings("serial")
    TypeToken<PM> token = new TypeToken<PM>(controller.getClass()) {};
    @SuppressWarnings("unchecked")
    Class<PM> rawType = (Class<PM>) token.getRawType();
    pmClass = rawType;
    localModelProvider = new ModelProvider();
    localModelProvider.setPresentationModelType(getPmClass());
    localModelProvider.addPropertyChangeListener("presentationModel", (e) -> {
      @SuppressWarnings("unchecked")
      PM newValue = (PM) e.getNewValue();
      presentationModelProperty.set(newValue);
    });
    presentationModelProperty.addListener((obs, oldV, newV) -> {
      localModelProvider.setPresentationModel(newV);
    });
  }

  public ObjectProperty<PM> presentationModelProperty() {
    return presentationModelProperty;
  }

  public Class<PM> getPmClass() {
    return pmClass;
  }

  public ModelProvider getLocalModelProvider() {
    return localModelProvider;
  }

  public PM getPresentationModel() {
    return getLocalModelProvider().getPresentationModel();
  }

  public void setPresentationModel(PM pModel) {
    getLocalModelProvider().setPresentationModel(pModel);
  }

  public IModelProvider getModelProvider() {
    return this.link.getModelProvider();
  }

  public void setModelProvider(IModelProvider modelProvider) {
    this.link.setModelProvider(modelProvider);
  }

  public Path getPath() {
    return this.link.getPath();
  }

  public void setPath(Path path) {
    this.link.setPath(path);
  }

}

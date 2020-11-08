package org.beanfabrics.javafx.controller;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.PresentationModel;
import javafx.beans.property.ObjectProperty;

/**
 * Convenient base class for JavaFX controller classes with Beanfabrics support.
 *
 * @param <PM>
 */
public abstract class BnFxControllerBase<PM extends PresentationModel> implements BnFxController<PM> {
  private final BnFxControllerSupport<PM> support = new BnFxControllerSupport<PM>(this);

  protected ModelProvider getLocalModelProvider() {
    return support.getLocalModelProvider();
  }

  public ObjectProperty<PM> presentationModelProperty() {
    return support.presentationModelProperty();
  }

  @Override
  public PM getPresentationModel() {
    return support.getPresentationModel();
  }

  @Override
  public void setPresentationModel(PM pModel) {
    support.setPresentationModel(pModel);
  }

  @Override
  public IModelProvider getModelProvider() {
    return support.getModelProvider();
  }

  @Override
  public void setModelProvider(IModelProvider modelProvider) {
    support.setModelProvider(modelProvider);
  }

  @Override
  public Path getPath() {
    return support.getPath();
  }

  @Override
  public void setPath(Path path) {
    support.setPath(path);
  }

}

package org.beanfabrics.javafx.binder;

import static com.google.common.base.Preconditions.checkNotNull;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.javafx.property.ImagePmProperty;
import javafx.scene.image.ImageView;

public class ImageViewBinding implements BnBinding {
  private ImageView control;

  public ImageViewBinding(ImageView control, IModelProvider modelProvider, Path path) {
    this(control, new ImagePmProperty(modelProvider, path));
  }

  public ImageViewBinding(ImageView control, ImagePmProperty property) {
    this.control = checkNotNull(control, "control == null!");
    checkNotNull(property, "property == null!");

    control.imageProperty().bind(property.imageProperty());
  }

  @Override
  public void dispose() {
    control.imageProperty().unbind();
  }

}

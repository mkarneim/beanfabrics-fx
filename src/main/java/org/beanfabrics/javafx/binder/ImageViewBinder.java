package org.beanfabrics.javafx.binder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import com.google.auto.service.AutoService;
import javafx.scene.image.ImageView;

@AutoService(Binder.class)
public class ImageViewBinder implements Binder<ImageView> {

  @Override
  public ImageViewBinding bind(ImageView control, IModelProvider modelProvider, Path path) {
    return new ImageViewBinding(control, modelProvider, path);
  }

}

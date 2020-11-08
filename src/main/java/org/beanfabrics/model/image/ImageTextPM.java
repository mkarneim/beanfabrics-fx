package org.beanfabrics.model.image;

import java.net.URL;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import javafx.scene.image.Image;

public class ImageTextPM extends TextPM implements IImagePM {

  private Image image;

  public ImageTextPM() {
    PMManager.setup(this);
  }

  public void setImage(Image newImage) {
    Image oldImage = this.image;
    this.image = newImage;
    if (!equals(oldImage, newImage)) {
      getPropertyChangeSupport().firePropertyChange("image", oldImage, newImage);
    }
  }

  public Image getImage() {
    return image;
  }

  public void setImageUrl(URL imageUrl) {
    if (imageUrl == null) {
      setImage(null);
    } else {
      setImage(new Image(imageUrl.toExternalForm()));
    }
  }

  @Override
  public boolean isEmpty() {
    return image == null;
  }

}

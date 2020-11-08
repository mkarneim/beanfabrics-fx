package org.beanfabrics.javafx.property;

import java.beans.PropertyChangeListener;
import javax.annotation.Nullable;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.ConversionException;
import org.beanfabrics.model.image.IImagePM;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.image.Image;

/**
 * The {@link ImagePmProperty} is a Java FX {@link ObjectProperty} that holds a {@link IImagePM}
 * instance.
 * <p>
 * It additionally provides bindings for the ITextPM attributes like "text", "description", "title",
 * etc.
 *
 */
public class ImagePmProperty extends IValuePmProperty<IImagePM> {

  public ImagePmProperty() {}

  public ImagePmProperty(@Nullable IImagePM presentationModel) {
    super(presentationModel);
  }

  public ImagePmProperty(@Nullable IModelProvider provider, @Nullable Path path) {
    super(provider, path);
  }



  //// pmPropertyChangeListener ////
  private PropertyChangeListener pmPropertyChangeListener;

  private PropertyChangeListener getPmPropertyChangeListener() {
    if (pmPropertyChangeListener == null) {
      pmPropertyChangeListener = evt -> {
        if ("image".equals(evt.getPropertyName())) {
          try {
            Image value = getPresentationModel().getImage();
            setImage(value);
          } catch (ConversionException ex) {
            // we can't sync this value, so we ignore it
          }
        }
      };
    }
    return pmPropertyChangeListener;
  }

  @Override
  protected void onChange(@Nullable IImagePM oldValue, @Nullable IImagePM newValue) {
    super.onChange(oldValue, newValue);
    if (oldValue != null) {
      oldValue.removePropertyChangeListener(getPmPropertyChangeListener());
    }
    if (newValue != null) {
      newValue.addPropertyChangeListener(getPmPropertyChangeListener());
      try {
        Image value = newValue.getImage();
        setImage(value);
      } catch (ConversionException e) {
        // we can't sync this value, so we ignore it
      }
    }
  }



  //// image ////

  private ReadOnlyObjectWrapper<Image> image;

  private ReadOnlyObjectWrapper<Image> _image() {
    if (image == null) {
      image = new ReadOnlyObjectWrapper<>();
    }
    return image;
  }

  public ReadOnlyObjectProperty<Image> imageProperty() {
    return _image().getReadOnlyProperty();
  }

  public Image getImage() {
    return _image().get();
  }

  private void setImage(Image image) {
    _image().set(image);
  }

}

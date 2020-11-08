package org.beanfabrics.javafx.property;

import java.beans.PropertyChangeListener;
import javax.annotation.Nullable;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.validation.ValidationState;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * The {@link PresentationModelProperty} is a Java FX {@link ObjectProperty} that holds a
 * {@link PresentationModel} instance.
 * <p>
 * It additionally provides bindings for the PresentationModel attributes like "validationState".
 *
 */
public class PresentationModelProperty<PM extends PresentationModel> extends SimpleObjectProperty<PM>
    implements View<PM>, ModelSubscriber {

  private final Link link = new Link(this);


  public PresentationModelProperty() {
    this.addListener((obs, oldV, newV) -> {
      onChange(oldV, newV);
    });
  }

  public PresentationModelProperty(@Nullable PM presentationModel) {
    this();
    setPresentationModel(presentationModel);
  }

  public PresentationModelProperty(@Nullable IModelProvider provider, @Nullable Path path) {
    this();
    setModelProvider(provider);
    setPath(path);
  }

  @Override
  public void setPresentationModel(@Nullable PM presentationModel) {
    setValue(presentationModel);
  }

  @Override
  public @Nullable PM getPresentationModel() {
    return getValue();
  }

  @Override
  public @Nullable IModelProvider getModelProvider() {
    return link.getModelProvider();
  }

  @Override
  public void setModelProvider(@Nullable IModelProvider provider) {
    link.setModelProvider(provider);
  }

  @Override
  public @Nullable Path getPath() {
    return link.getPath();
  }

  @Override
  public void setPath(@Nullable Path path) {
    link.setPath(path);
  }

  protected void onChange(@Nullable PM oldValue, @Nullable PM newValue) {
    if (oldValue != null) {
      oldValue.removePropertyChangeListener(getPmPropertyChangeListener());
    }
    if (newValue != null) {
      newValue.addPropertyChangeListener(getPmPropertyChangeListener());
      setValidationState(newValue.getValidationState());
    }
  }



  //// pmPropertyChangeListener ////
  private PropertyChangeListener pmPropertyChangeListener;

  private PropertyChangeListener getPmPropertyChangeListener() {
    if (pmPropertyChangeListener == null) {
      pmPropertyChangeListener = evt -> {
        if ("validationState".equals(evt.getPropertyName())) {
          setValidationState((ValidationState) evt.getNewValue());
        }
      };

    }
    return pmPropertyChangeListener;
  }



  //// validationState ////
  public interface ValidationStateProperty extends ReadOnlyProperty<ValidationState> {
    StringBinding asString();
  }

  private class ValidationStatePropertyWrapper extends SimpleObjectProperty<ValidationState> {
    private ReadOnlyPropertyImpl readOnlyProperty;

    public ValidationStateProperty getReadOnlyProperty() {
      if (readOnlyProperty == null) {
        readOnlyProperty = new ReadOnlyPropertyImpl();
      }
      return readOnlyProperty;
    }

    @Override
    protected void fireValueChangedEvent() {
      super.fireValueChangedEvent();
      if (readOnlyProperty != null) {
        readOnlyProperty.fireValueChangedEvent();
      }
      PresentationModelProperty.this.fireValueChangedEvent();
    }

    private class ReadOnlyPropertyImpl extends ReadOnlyObjectPropertyBase<ValidationState>
        implements ValidationStateProperty {

      @Override
      public @Nullable ValidationState get() {
        return ValidationStatePropertyWrapper.this.get();
      }

      @Override
      public Object getBean() {
        return ValidationStatePropertyWrapper.this.getBean();
      }

      @Override
      public String getName() {
        return ValidationStatePropertyWrapper.this.getName();
      }

      @Override
      protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
      }

      @Override
      public StringBinding asString() {
        return Bindings.createStringBinding(() -> {
          ValidationState v = getValue();
          return v == null ? null : v.getMessage();
        }, this);
      }
    };
  }

  private ValidationStatePropertyWrapper validationState;

  private ValidationStatePropertyWrapper _validationState() {
    if (validationState == null) {
      validationState = new ValidationStatePropertyWrapper();
    }
    return validationState;
  }

  public ValidationStateProperty validationStateProperty() {
    return _validationState().getReadOnlyProperty();
  }

  public @Nullable ValidationState getValidationState() {
    return _validationState().get();
  }

  private void setValidationState(@Nullable ValidationState validationState) {
    _validationState().set(validationState);
    _validationState().fireValueChangedEvent();
  }

}

package org.beanfabrics.javafx.property;

import java.beans.PropertyChangeListener;
import java.lang.reflect.UndeclaredThrowableException;
import javax.annotation.Nullable;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.IOperationPM;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringPropertyBase;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;

/**
 * The {@link OperationPmProperty} is a Java FX {@link ObjectProperty} that holds a
 * {@link IOperationPM} instance.
 * <p>
 * It additionally provides bindings for the IOperationPM attributes like "enabled", "description",
 * "title", etc.
 *
 */
public class OperationPmProperty extends PresentationModelProperty<IOperationPM> {

  // TODO (mka, 5.4.2018) Should we support the "icon" property as well, although this is Swing
  // specific?

  public OperationPmProperty() {}

  public OperationPmProperty(@Nullable IOperationPM presentationModel) {
    super(presentationModel);
  }

  public OperationPmProperty(@Nullable IModelProvider provider, @Nullable Path path) {
    super(provider, path);
  }

  @Override
  protected void onChange(@Nullable IOperationPM oldValue, @Nullable IOperationPM newValue) {
    super.onChange(oldValue, newValue);
    if (oldValue != null) {
      oldValue.removePropertyChangeListener(getPmPropertyChangeListener());
    }
    if (newValue != null) {
      newValue.addPropertyChangeListener(getPmPropertyChangeListener());
      setDescription(newValue.getDescription());
      setTitle(newValue.getTitle());
      setOnAction((e) -> {
        try {
          newValue.execute();
        } catch (Throwable e1) {
          throw new UndeclaredThrowableException(e1);
        }
      });
    }
  }


  //// pmPropertyChangeListener ////
  private PropertyChangeListener pmPropertyChangeListener;

  private PropertyChangeListener getPmPropertyChangeListener() {
    if (pmPropertyChangeListener == null) {
      pmPropertyChangeListener = evt -> {
        if ("description".equals(evt.getPropertyName())) {
          setDescription((String) evt.getNewValue());
        }
        if ("title".equals(evt.getPropertyName())) {
          setTitle((String) evt.getNewValue());
        }
      };
    }
    return pmPropertyChangeListener;
  }



  //// description ////
  public interface DescriptionProperty extends ReadOnlyProperty<String> {
    ObjectBinding<Tooltip> asTooltip();
  }
  private class DescriptionPropertyWrapper extends SimpleStringProperty {
    private ReadOnlyPropertyImpl readOnlyProperty;

    public DescriptionProperty getReadOnlyProperty() {
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
    }

    private class ReadOnlyPropertyImpl extends ReadOnlyStringPropertyBase implements DescriptionProperty {

      @Override
      public @Nullable String get() {
        return DescriptionPropertyWrapper.this.get();
      }

      @Override
      public Object getBean() {
        return DescriptionPropertyWrapper.this.getBean();
      }

      @Override
      public String getName() {
        return DescriptionPropertyWrapper.this.getName();
      }

      @Override
      protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
      }

      @Override
      public ObjectBinding<Tooltip> asTooltip() {
        return Bindings.createObjectBinding(() -> {
          String value = getValue();
          return value == null ? null : new Tooltip(value);
        }, this);
      }
    };
  }

  private DescriptionPropertyWrapper description;

  private DescriptionPropertyWrapper _description() {
    if (description == null) {
      description = new DescriptionPropertyWrapper();
    }
    return description;
  }

  public DescriptionProperty descriptionProperty() {
    return _description().getReadOnlyProperty();
  }

  public @Nullable String getDescription() {
    return _description().get();
  }

  private void setDescription(@Nullable String description) {
    _description().set(description);
  }



  //// title ////
  private ReadOnlyStringWrapper title;

  private ReadOnlyStringWrapper _title() {
    if (title == null) {
      title = new ReadOnlyStringWrapper();
    }
    return title;
  }

  public ReadOnlyStringProperty titleProperty() {
    return _title().getReadOnlyProperty();
  }

  public @Nullable String getTitle() {
    return titleProperty().get();
  }

  private void setTitle(@Nullable String title) {
    _title().set(title);
  }



  //// enabled ////
  public BooleanBinding enabledProperty() {
    return Bindings.createBooleanBinding(() -> {
      return (OperationPmProperty.this.isBound() || OperationPmProperty.this.getValue() != null)
          && validationStateProperty().getValue() == null;
    }, validationStateProperty(), OperationPmProperty.this);
  }

  public boolean isEnabled() {
    return enabledProperty().get();
  }



  //// onAction ////
  private ReadOnlyObjectWrapper<EventHandler<ActionEvent>> onAction;

  private ReadOnlyObjectWrapper<EventHandler<ActionEvent>> _onActionProperty() {
    if (onAction == null) {
      onAction = new ReadOnlyObjectWrapper<>();
    }
    return onAction;
  }

  public ReadOnlyProperty<? extends EventHandler<ActionEvent>> onActionProperty() {
    return _onActionProperty().getReadOnlyProperty();
  }

  public EventHandler<ActionEvent> getOnAction() {
    return onActionProperty().getValue();
  }

  private void setOnAction(EventHandler<ActionEvent> value) {
    _onActionProperty().set(value);
  }

}

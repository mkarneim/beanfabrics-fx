package org.beanfabrics.javafx.property;

import java.beans.PropertyChangeListener;
import javax.annotation.Nullable;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.model.IValuePM;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringPropertyBase;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Tooltip;

/**
 * The {@link IValuePmProperty} is a Java FX {@link ObjectProperty} that holds a {@link IValuePM}
 * instance.
 * <p>
 * It additionally provides bindings for the IValuePM attributes like "description", "title", etc.
 *
 */
public class IValuePmProperty<PM extends IValuePM> extends PresentationModelProperty<PM>
    implements View<PM>, ModelSubscriber {

  public IValuePmProperty() {}

  public IValuePmProperty(@Nullable PM presentationModel) {
    super(presentationModel);
  }

  public IValuePmProperty(@Nullable IModelProvider provider, @Nullable Path path) {
    super(provider, path);
  }

  @Override
  protected void onChange(@Nullable PM oldValue, @Nullable PM newValue) {
    super.onChange(oldValue, newValue);
    if (oldValue != null) {
      oldValue.removePropertyChangeListener(getPmPropertyChangeListener());
    }
    if (newValue != null) {
      newValue.addPropertyChangeListener(getPmPropertyChangeListener());
      setDescription(newValue.getDescription());
      setTitle(newValue.getTitle());
      setEditable(newValue.isEditable());
      setMandatory(newValue.isMandatory());
      setEmpty(newValue.isEmpty());
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
        if ("editable".equals(evt.getPropertyName())) {
          setEditable((boolean) evt.getNewValue());
        }
        if ("mandatory".equals(evt.getPropertyName())) {
          setMandatory((boolean) evt.getNewValue());
        }
        if ("empty".equals(evt.getPropertyName())) {
          setEmpty((boolean) evt.getNewValue());
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
      IValuePmProperty.this.fireValueChangedEvent();
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
    _description().fireValueChangedEvent();
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



  //// editable ////
  private ReadOnlyBooleanWrapper editable;

  private ReadOnlyBooleanWrapper _editable() {
    if (editable == null) {
      editable = new ReadOnlyBooleanWrapper();
    }
    return editable;
  }

  public ReadOnlyBooleanProperty editableProperty() {
    return _editable().getReadOnlyProperty();
  }

  public boolean isEditable() {
    return editableProperty().get();
  }

  private void setEditable(boolean editable) {
    _editable().set(editable);
  }



  //// mandatory ////
  private ReadOnlyBooleanWrapper mandatory;

  private ReadOnlyBooleanWrapper _mandatory() {
    if (mandatory == null) {
      mandatory = new ReadOnlyBooleanWrapper();
    }
    return mandatory;
  }

  public ReadOnlyBooleanProperty mandatoryProperty() {
    return _mandatory().getReadOnlyProperty();
  }

  public boolean isMandatory() {
    return mandatoryProperty().get();
  }

  private void setMandatory(boolean mandatory) {
    _mandatory().set(mandatory);
  }



  //// empty ////
  private ReadOnlyBooleanWrapper empty = new ReadOnlyBooleanWrapper();

  private ReadOnlyBooleanWrapper _empty() {
    if (empty == null) {
      empty = new ReadOnlyBooleanWrapper();
    }
    return empty;
  }


  public ReadOnlyBooleanProperty emptyProperty() {
    return _empty().getReadOnlyProperty();
  }

  public boolean isEmpty() {
    return emptyProperty().get();
  }

  private void setEmpty(boolean empty) {
    _empty().set(empty);
  }


}

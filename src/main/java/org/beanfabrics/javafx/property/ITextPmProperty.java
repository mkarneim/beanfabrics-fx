package org.beanfabrics.javafx.property;

import java.beans.PropertyChangeListener;
import javax.annotation.Nullable;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.event.OptionsEvent;
import org.beanfabrics.event.OptionsListener;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.Options;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The {@link ITextPmProperty} is a Java FX {@link ObjectProperty} that holds a {@link ITextPM}
 * instance.
 * <p>
 * It additionally provides bindings for the PM attributes like "text", "description", "title", etc.
 *
 */
public class ITextPmProperty<PM extends ITextPM> extends IValuePmProperty<PM> {


  public ITextPmProperty() {}

  public ITextPmProperty(@Nullable PM presentationModel) {
    super(presentationModel);
  }

  public ITextPmProperty(@Nullable IModelProvider provider, @Nullable Path path) {
    super(provider, path);
  }


  //// pmPropertyChangeListener ////
  private PropertyChangeListener pmPropertyChangeListener;

  private PropertyChangeListener getPmPropertyChangeListener() {
    if (pmPropertyChangeListener == null) {
      pmPropertyChangeListener = evt -> {
        if ("text".equals(evt.getPropertyName())) {
          String newValue = (String) evt.getNewValue();
          setText(newValue);
        }
        if ("options".equals(evt.getPropertyName())) {
          if (evt.getOldValue() != null || evt.getNewValue() != null) {
            setOptions((Options<?>) evt.getNewValue());
          }
        }
      };
    }
    return pmPropertyChangeListener;
  }

  @Override
  protected void onChange(@Nullable PM oldValue, @Nullable PM newValue) {
    super.onChange(oldValue, newValue);
    if (oldValue != null) {
      oldValue.removePropertyChangeListener(getPmPropertyChangeListener());
    }
    if (newValue != null) {
      newValue.addPropertyChangeListener(getPmPropertyChangeListener());
      setText(newValue.getText());
      setOptions(newValue.getOptions());
    }
  }

  //// text ////
  private ExtStringProperty text;

  public ExtStringProperty textProperty() {
    if (text == null) {
      text = new ExtSimpleStringProperty() {
        @Override
        protected void invalidated() {
          PM pm = getPresentationModel();
          if (pm != null) {
            String value = getValue();
            pm.setText(value);
            ITextPmProperty.this.fireValueChangedEvent();
          }
        }
      };
    }
    return text;
  }

  public @Nullable String getText() {
    return textProperty().get();
  }

  public void setText(@Nullable String text) {
    textProperty().set(text);
  }


  //// options ////
  public interface OptionsProperty extends ReadOnlyProperty<ObservableList<String>> {

  }

  private class OptionsPropertyWrapper extends SimpleObjectProperty<ObservableList<String>> {
    private ReadOnlyPropertyImpl readOnlyProperty;
    private OptionsBridge bridge;

    public OptionsPropertyWrapper() {
      setOptions(null);
    }

    public void setOptions(@Nullable Options<?> options) {
      if (bridge != null) {
        bridge.release();
      }
      bridge = new OptionsBridge(options);
      set(bridge.getTarget());
    }

    public OptionsProperty getReadOnlyProperty() {
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
      ITextPmProperty.this.fireValueChangedEvent();
    }

    private class ReadOnlyPropertyImpl extends ReadOnlyObjectPropertyBase<ObservableList<String>>
        implements OptionsProperty {

      @Override
      public @Nullable ObservableList<String> get() {
        return OptionsPropertyWrapper.this.get();
      }

      @Override
      public Object getBean() {
        return OptionsPropertyWrapper.this.getBean();
      }

      @Override
      public String getName() {
        return OptionsPropertyWrapper.this.getName();
      }

      @Override
      protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
      }
    };
  }

  private OptionsPropertyWrapper options;

  private OptionsPropertyWrapper _options() {
    if (options == null) {
      options = new OptionsPropertyWrapper();
    }
    return options;
  }

  public OptionsProperty optionsProperty() {
    return _options().getReadOnlyProperty();
  }

  public ObservableList<String> getOptions() {
    return _options().get();
  }

  private void setOptions(Options<?> options) {
    _options().setOptions(options);
    _options().fireValueChangedEvent();
  }


  private static class OptionsBridge {
    private final @Nullable Options<?> source;
    private final ObservableList<String> internalTarget;
    private final ObservableList<String> target;
    private final OptionsListener optionsListener = new OptionsListener() {
      @Override
      public void changed(OptionsEvent evt) {
        copyEntries();
      }
    };

    public OptionsBridge(@Nullable Options<?> source) {
      this.source = source;
      if (source != null) {
        internalTarget = FXCollections.observableArrayList();
        target = FXCollections.unmodifiableObservableList(internalTarget);
        source.addOptionsListener(optionsListener);
        copyEntries();
      } else {
        internalTarget = null;
        target = FXCollections.emptyObservableList();
      }
    }

    public void release() {
      if (source != null) {
        source.removeOptionsListener(optionsListener);
      }
    }

    public ObservableList<String> getTarget() {
      return target;
    }

    private void copyEntries() {
      String[] values = source.getValues();
      internalTarget.setAll(values);
    }

  }

}

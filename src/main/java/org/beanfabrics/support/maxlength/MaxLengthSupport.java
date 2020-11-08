package org.beanfabrics.support.maxlength;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.Nullable;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.support.Property;
import org.beanfabrics.support.PropertySupport;
import org.beanfabrics.support.Support;
import org.beanfabrics.support.Supportable;
import org.beanfabrics.util.ResourceBundleFactory;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

public class MaxLengthSupport implements Support {
  private static final String KEY_MESSAGE_VALIDATION_FAILED = "MaxLengthSupport.text-too_long";
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundleFactory.getBundle(MaxLengthSupport.class);

  public static MaxLengthSupport get(PresentationModel model) {
    Supportable s = (Supportable) model;

    MaxLengthSupport support = s.getSupportMap().get(MaxLengthSupport.class);
    if (support == null) {
      support = new MaxLengthSupport(model);
      s.getSupportMap().put(MaxLengthSupport.class, support);
    }
    return support;
  }

  private final PresentationModel model;
  private Map<Field, MaxLengthFieldSupport> map = new HashMap<>();

  public MaxLengthSupport(PresentationModel model) {
    this.model = model;
  }

  public void setup(Field field) {
    if (map.containsKey(field) == false) {
      MaxLengthFieldSupport support = support(model, field);
      map.put(field, support);
    }
  }

  private static MaxLengthFieldSupport support(PresentationModel pModel, Field field) {
    MaxLength anno = field.getAnnotation(MaxLength.class);
    int value = anno.value();
    MaxLengthFieldSupport result = new MaxLengthFieldSupport(pModel, value, field);
    return result;
  }

  private static class MaxLengthFieldSupport {
    private final PresentationModel owner;
    private final int maxLength;
    private final String name;
    private final PropertyChangeListener pcListener = new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        updateValidation();
      }
    };
    private final ValidationRule validationRule = new ValidationRule() {
      @Override
      public ValidationState validate() {
        if (target == null) {
          return null;
        } else {
          String text = target.getText();
          if (text == null) {
            return null;
          }
          int len = text.length();
          if (len <= maxLength) {
            return null;
          }
          return ValidationState.create(getMessage(len, maxLength));
        }
      }
    };
    private @Nullable ITextPM target;

    public MaxLengthFieldSupport(PresentationModel owner, int maxLength, Field annotatedField) {
      this.owner = owner;
      this.maxLength = maxLength;
      if (!ITextPM.class.isAssignableFrom(annotatedField.getType())) {
        throw new IllegalArgumentException(
            String.format("Field %s must be of type %s", annotatedField.getName(), ITextPM.class.getSimpleName()));
      }
      Property propertyAnno = annotatedField.getAnnotation(Property.class);
      this.name = propertyAnno != null ? propertyAnno.value() : annotatedField.getName();
      owner.addPropertyChangeListener(name, this.pcListener);
    }

    private void updateValidation() {
      ITextPM newTarget = (ITextPM) PropertySupport.get(owner).getProperty(name);
      if (newTarget == target) {
        return;
      }
      if (target != null) {
        target.getValidator().remove(validationRule);
      }
      target = newTarget;
      if (target != null) {
        target.getValidator().add(validationRule);
      }
    }

    private String getMessage(int actualLen, int maxLen) {
      String message = RESOURCE_BUNDLE.getString(KEY_MESSAGE_VALIDATION_FAILED);
      return String.format(message, actualLen, maxLen);
    }


  }
}

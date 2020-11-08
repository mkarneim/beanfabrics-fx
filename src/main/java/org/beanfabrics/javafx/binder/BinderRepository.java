package org.beanfabrics.javafx.binder;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import javax.annotation.Nullable;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.util.GenericType;

/**
 * The {@link BinderRepository} holds all registered {@link Binder} instances.
 */
public class BinderRepository {

  private final Map<Class<?>, Binder<?>> binderMap = new HashMap<>();
  private final ModelSubscriberBinder modelSubscriberBinder = new ModelSubscriberBinder();

  @SuppressWarnings("rawtypes")
  private ServiceLoader<Binder> serviceLoader;

  @SuppressWarnings("rawtypes")
  public BinderRepository() {
    ServiceLoader<Binder> instances = getBinderInstances();
    for (Binder binder : instances) {
      TypeVariable<Class<Binder>> typeVar = Binder.class.getTypeParameters()[0];
      GenericType classType = new GenericType(binder.getClass());
      GenericType typeParam = classType.getTypeParameter(typeVar);
      Class<?> paramClass = typeParam.asClass();
      binderMap.put(paramClass, binder);
    }
  }

  @SuppressWarnings("rawtypes")
  private ServiceLoader<Binder> getBinderInstances() {
    if (serviceLoader == null) {
      serviceLoader = ServiceLoader.load(Binder.class);
    }
    return serviceLoader;
  }

  /**
   * Puts the given {@link Binder} into this repository and marks it as responsible for binding
   * instances of the given class.
   *
   * @param cls
   * @param binder
   */
  public <T> void putBinder(Class<? super T> cls, Binder<? super T> binder) {
    binderMap.put(cls, binder);
  }

  /**
   * Returns a {@link Binder} responsible for binding instances of the given class.
   *
   * @param cls
   * @return the Binder
   */
  public <T> Binder<? super T> getBinder(Class<? super T> cls) {
    Binder<? super T> result = findBinder(cls);
    return result;
  }

  @SuppressWarnings("unchecked")
  private <T> Binder<? super T> findBinder(Class<? super T> cls) {
    if (cls == null || Object.class.equals(cls)) {
      return null;
    }
    Binder<? super T> result = (Binder<T>) binderMap.get(cls);
    if (result == null) {
      result = getDeclaredBinder(cls);
      if (result == null) {
        if (ModelSubscriber.class.isAssignableFrom(cls)) {
          result = (Binder<T>) modelSubscriberBinder;
        } else {
          result = findBinder(cls.getSuperclass());
          if (result == null) {
            for (Class<?> interf : cls.getInterfaces()) {
              result = findBinder((Class<? super T>) interf);
            }
          }
        }
      }
    }
    if (result != null) {
      putBinder(cls, result);
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  private @Nullable <T> Binder<T> getDeclaredBinder(Class<? super T> cls) {
    try {
      UseBinder binderAnno = cls.getAnnotation(UseBinder.class);
      if (binderAnno != null) {
        return (Binder<T>) binderAnno.value().newInstance();
      }
      return null;
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

}

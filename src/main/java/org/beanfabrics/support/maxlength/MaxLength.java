package org.beanfabrics.support.maxlength;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.beanfabrics.support.Processor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Processor(MaxLengthProcessor.class)
public @interface MaxLength {
  int value();
}

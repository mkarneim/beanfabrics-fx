package org.beanfabrics.support.maxlength;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import org.beanfabrics.log.Logger;
import org.beanfabrics.log.LoggerFactory;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.support.AnnotatedFieldProcessor;
import org.beanfabrics.support.SupportUtil;

public class MaxLengthProcessor implements AnnotatedFieldProcessor {
  private static Logger LOG = LoggerFactory.getLogger(MaxLengthProcessor.class);

  public void process(PresentationModel object, Field field, Annotation annotation) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Processing " + SupportUtil.format(field));
    }
    MaxLengthSupport.get(object).setup(field);
  }
}

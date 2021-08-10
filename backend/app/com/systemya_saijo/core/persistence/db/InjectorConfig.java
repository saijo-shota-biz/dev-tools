package com.systemya_saijo.core.persistence.db;

import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;

@AnnotateWith(annotations = {
  @Annotation(target = AnnotationTarget.CONSTRUCTOR, type = javax.inject.Inject.class),
  @Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = javax.inject.Named.class, elements = "\"config\"") })
public @interface InjectorConfig {
}

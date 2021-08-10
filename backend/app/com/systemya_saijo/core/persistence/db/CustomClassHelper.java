package com.systemya_saijo.core.persistence.db;

import org.seasar.doma.internal.WrapException;
import org.seasar.doma.jdbc.ClassHelper;
import play.Application;

import javax.inject.Inject;

public class CustomClassHelper implements ClassHelper {

  private Application application;

  @Inject
  public CustomClassHelper(Application application) {
    this.application = application;
  }

  @Override public <T> Class<T> forName(String className) throws Exception {
    try {
      ClassLoader classLoader = application.classloader();
      if (classLoader == null) {
        return (Class<T>) Class.forName(className);
      } else {
        try {
          return (Class<T>) classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
          return (Class<T>) Class.forName(className);
        }
      }
    } catch (ClassNotFoundException e) {
      throw new WrapException(e);
    }
  }
}

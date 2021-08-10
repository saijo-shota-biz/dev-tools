package com.systemya_saijo

import com.google.inject.name.Names
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.tx.TransactionManager
import play.Application
import play.inject.{BindingKey, Injector}
import play.test.Helpers.fakeApplication

trait TestApplicationTrait {
  def setUpApplication(): (Application, Injector, Config, TransactionManager) = {
    val fakeApp = fakeApplication(TestConfig.configMap)
    val injector = fakeApp.injector
    val bindingKey = new BindingKey(classOf[Config]).qualifiedWith(Names.named("config")).asScala()
    val domaConfig = injector.instanceOf(bindingKey)
    val tm = domaConfig.getTransactionManager

    (fakeApp, injector, domaConfig, tm)
  }
}

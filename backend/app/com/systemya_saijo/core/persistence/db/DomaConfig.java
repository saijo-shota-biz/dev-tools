package com.systemya_saijo.core.persistence.db;

import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionManager;
import org.seasar.doma.jdbc.tx.TransactionManager;
import play.db.Database;

import javax.inject.Inject;
import javax.sql.DataSource;

public class DomaConfig implements Config {

  private final Dialect dialect;

  private final LocalTransactionDataSource dataSource;

  private final TransactionManager transactionManager;

  private final CustomClassHelper classHelper;

  @Inject
  public DomaConfig(Database database, CustomClassHelper classHelper) {
    this.dialect = new PostgresDialect();
    this.dataSource = new LocalTransactionDataSource(database.getDataSource());
    this.transactionManager =
        new LocalTransactionManager(dataSource.getLocalTransaction(getJdbcLogger()));
    this.classHelper = classHelper;
  }

  @Override
  public TransactionManager getTransactionManager() {
    return this.transactionManager;
  }

  @Override
  public DataSource getDataSource() {
    return this.dataSource;
  }

  @Override
  public Dialect getDialect() {
    return this.dialect;
  }

  @Override public ClassHelper getClassHelper() {
    return this.classHelper;
  }
}

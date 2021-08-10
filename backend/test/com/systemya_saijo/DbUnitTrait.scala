package com.systemya_saijo

import org.dbunit.database.{DatabaseConfig, DatabaseConnection, IDatabaseConnection}
import org.dbunit.dataset.excel.XlsDataSet
import org.dbunit.dataset.{IDataSet, ReplacementDataSet}
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory
import org.dbunit.operation.DatabaseOperation
import play.api.Logger

import java.sql.Connection

trait DbUnitTrait {

  def cleanInsert(conn: Connection, paths: Seq[String]): Unit = {
    val (dbUnitConn, dataSets) = setupDatabaseConnection(conn, paths)
    for (dataSet <- dataSets) {
      DatabaseOperation.CLEAN_INSERT.execute(dbUnitConn, dataSet)
    }
  }

  private def setupDatabaseConnection(conn: Connection, paths: Seq[String]): (IDatabaseConnection, List[IDataSet]) = {
    val dbUnitConn = new DatabaseConnection(conn)
    dbUnitConn.getConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new PostgresqlDataTypeFactory)
    dbUnitConn.getConfig.setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true)
    var dataSets: List[IDataSet] = List.empty
    for (path <- paths) {
      val is = getClass.getClassLoader.getResourceAsStream(path)
      try {
        val dataSet = new XlsDataSet(is)
        val rDataSet = new ReplacementDataSet(dataSet)
        dataSets = rDataSet +: dataSets
      } finally {
        is.close()
      }
    }
    (dbUnitConn, dataSets)
  }
}

package com.systemya_saijo

import com.systemya_saijo.core.exception.CustomHttpErrorHandler
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{Assertion, BeforeAndAfterAll, BeforeAndAfterEach}
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.tx.TransactionManager
import play.Application
import play.api.Logger
import play.inject.Injector
import play.libs.Json
import play.mvc.Http.RequestBuilder
import play.mvc.Result
import play.test.Helpers
import play.test.Helpers.route

import scala.compat.java8.FutureConverters.toScala
import scala.concurrent.Future

abstract class BaseControllerSpec extends AsyncFlatSpec with Matchers with BeforeAndAfterEach with BeforeAndAfterAll with TestApplicationTrait with DbUnitTrait {

  val logger: Logger = Logger(this.getClass.getName)

  var fakeApp: Application = _
  var injector: Injector = _
  var domaConfig: Config = _
  var tm: TransactionManager = _

  override def beforeAll(): Unit = {
    val (f, i, d, t) = setUpApplication()
    fakeApp = f
    injector = i
    domaConfig = d
    tm = t
  }

  override def afterAll(): Unit = {
    Helpers.stop(fakeApp)
  }

  override def beforeEach(): Unit = {
    val testDataExcelSeq = getTestDataExcelSeq
    if (testDataExcelSeq == null) {
      return
    }
    tm.required(new Runnable {
      override def run(): Unit = () -> {
        cleanInsert(domaConfig.getDataSource.getConnection(), testDataExcelSeq.map(path => s"com/systemya_saijo/${path}"))
      }
    })
  }

  def getTestDataExcelSeq: Seq[String]

  // call request
  def callRequest(request: RequestBuilder): Result = {
    route(fakeApp, request)
  }

  // assert
  def assertResponseBody(result: Result, expect: String): Assertion = {
    val responseBody = Helpers.contentAsString(result)
    assert(Json.parse(responseBody) == Json.parse(expect))
  }

  def assertErrorResponse(requestBuilder: RequestBuilder, throwable: Throwable, assert: Result => Assertion): Future[Assertion] = {
    val errorHandler = new CustomHttpErrorHandler()
    val cs = errorHandler.onServerError(requestBuilder.build(), new RuntimeException(throwable))
    toScala(cs) map {
      assert
    }
  }
}

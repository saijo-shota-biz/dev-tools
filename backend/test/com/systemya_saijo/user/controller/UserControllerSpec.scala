package com.systemya_saijo.user.controller

import com.systemya_saijo.BaseControllerSpec
import com.systemya_saijo.user.domain.User
import com.systemya_saijo.user.domain.vo.UserId
import org.mockito.Mockito
import org.seasar.doma.jdbc.builder.SelectBuilder
import play.libs.Json
import play.mvc.Http.RequestBuilder
import play.test.Helpers.fakeRequest

import scala.util.Using

class UserControllerSpec extends BaseControllerSpec {

  override def getTestDataExcelSeq: Seq[String] = {
    Seq(
      "user/controller/UserControllerSpec.xlsx"
    )
  }

  behavior of "GET /users list"

  def listRequestBuilder(): RequestBuilder = {
    fakeRequest()
      .method("GET")
      .uri(s"""/api/v1/users""")
  }

  it should "全てのユーザーを取得する" in {
    val requestBuilder = listRequestBuilder()
    val result = callRequest(requestBuilder)
    assert(result.status == 200)
    assertResponseBody(result,
      s"""
         |[
         |  {
         |    "id": "1",
         |    "name": "test_name1",
         |    "email": "test_email1@example.com",
         |    "version": 1,
         |    "link": "http://localhost/api/v1/users/1"
         |  },
         |  {
         |    "id": "2",
         |    "name": "test_name2",
         |    "email": "test_email2@example.com",
         |    "version": 2,
         |    "link": "http://localhost/api/v1/users/2"
         |  }
         |]
         |""".stripMargin)
  }

  behavior of "GET users/:userId show"

  def showRequestBuilder(userId: String): RequestBuilder = {
    fakeRequest()
      .method("GET")
      .uri(s"""/api/v1/users/${userId}""")
  }

  it should "指定のユーザーが存在しない場合、エラーとなること" in {
    val requestBuilder = showRequestBuilder("99")
    val caught: Throwable = the[Throwable] thrownBy callRequest(requestBuilder)
    assertErrorResponse(requestBuilder, caught, result => {
      assert(result.status == 404)
      assertResponseBody(result,
        s"""
           |{
           |  "message": "ユーザーIDが99のユーザーが見つかりません"
           |}
           |""".stripMargin)
    })
  }

  it should "指定のユーザーが存在する場合、ユーザーが取得されること" in {
    val requestBuilder = showRequestBuilder("1")
    val result = callRequest(requestBuilder)
    assert(result.status == 200)
    assertResponseBody(result,
      s"""
         |{
         |  "id": "1",
         |  "name": "test_name1",
         |  "email": "test_email1@example.com",
         |  "version": 1
         |}
         |""".stripMargin)
  }

  behavior of "POST /users create"

  def createRequestBuilder(paramString: String): RequestBuilder = {
    val param = Json.parse(paramString);
    fakeRequest()
      .method("POST")
      .uri(s"""/api/v1/users""")
      .bodyJson(param)
  }

  it should "param.emailがない場合、バリデーションエラーとなること" in {
    val requestBuilder = createRequestBuilder(
      s"""
         |{
         |  "email": ""
         |}
         |""".stripMargin)
    val caught: Throwable = the[Throwable] thrownBy callRequest(requestBuilder)
    assertErrorResponse(requestBuilder, caught, result => {
      assert(result.status == 400)
      assertResponseBody(result,
        s"""
           |{
           |  "message": "入力値が不正です",
           |  "errors": [
           |    {
           |      "field": "email",
           |      "invalidValue": "null",
           |      "message": "空白は許可されていません"
           |    }
           |  ]
           |}
           |""".stripMargin)
    })
  }
  it should "param.emailがemail形式でない場合、バリデーションエラーとなること" in {
    val requestBuilder = createRequestBuilder(
      s"""
         |{
         |  "email": "test"
         |}
         |""".stripMargin)
    val caught: Throwable = the[Throwable] thrownBy callRequest(requestBuilder)
    assertErrorResponse(requestBuilder, caught, result => {
      assert(result.status == 400)
      assertResponseBody(result,
        s"""
           |{
           |  "message": "入力値が不正です",
           |  "errors": [
           |    {
           |      "field": "email",
           |      "invalidValue": "test",
           |      "message": "電子メールアドレスとして正しい形式にしてください"
           |    }
           |  ]
           |}
           |""".stripMargin)
    })
  }

  it should "param.emailのユーザーが登録済みの場合、重複エラーとなること" in {
    val email = "test_email2@example.com"
    val requestBuilder = createRequestBuilder(
      s"""
         |{
         |  "email": "${email}"
         |}
         |""".stripMargin)

    val caught: Throwable = the[Throwable] thrownBy callRequest(requestBuilder)
    assertErrorResponse(requestBuilder, caught, result => {
      assert(result.status == 500)
      assertResponseBody(result,
        s"""
           |{
           |  "message": "emailが重複しています",
           |}
           |""".stripMargin)
    })

    // ユーザーが登録されていないことを確認する
    val user = tm.required(() => {
      SelectBuilder.newInstance(domaConfig)
        .sql(s"select * from users u")
        .getEntityResultList(classOf[User])
    })

    assert(user.size == 2)
  }

  it should "ユーザーが登録されること" in {
    val email = "test3@example.com"
    val requestBuilder = createRequestBuilder(
      s"""
         |{
         |  "email": "${email}"
         |}
         |""".stripMargin)

    val result = callRequest(requestBuilder)
    assert(result.status == 201)

    // ユーザーが登録されていることを確認する
    val user = tm.required(() => {
      SelectBuilder.newInstance(domaConfig)
        .sql(s"select * from users u where u.email = '${email}'")
        .getEntitySingleResult(classOf[User])
    })

    assertResponseBody(result,
      s"""
         |{
         |  "id": "${user.getId}",
         |  "link": "http://localhost/api/v1/users/${user.getId}"
         |}
         |""".stripMargin)


    assert(Json.toJson(user) == Json.parse(
      s"""{
         |  "id": "${user.getId}",
         |  "name": "test3",
         |  "email": "test3@example.com",
         |  "version": 0
         |}
         |""".stripMargin))
  }

  behavior of "POST /users/:userId update"
  def updateRequestBuilder(userId: String, paramString: String): RequestBuilder = {
    val param = Json.parse(paramString);
    fakeRequest()
      .method("PATCH")
      .uri(s"""/api/v1/users/${userId}""")
      .bodyJson(param)
  }

  it should "param.emailがEmailの形式でない場合、バリデーションエラー" in {
    val userId = "1"
    val requestBuilder = updateRequestBuilder(userId,
      s"""
         |{
         |  "email": "test_example.com",
         |  "version": 0
         |}
         |""".stripMargin)
    val caught: Throwable = the[Throwable] thrownBy callRequest(requestBuilder)
    assertErrorResponse(requestBuilder, caught, result => {
      assert(result.status == 400)
      assertResponseBody(result,
        s"""
           |{
           |  "message": "入力値が不正です",
           |  "errors": [
           |    {
           |      "field": "email",
           |      "invalidValue": "test_example.com",
           |      "message": "電子メールアドレスとして正しい形式にしてください"
           |    }
           |  ]
           |}
           |""".stripMargin)
    })
  }
  it should "param.versionがない場合、バリデーションエラー" in {
    val userId = "1"
    val requestBuilder = updateRequestBuilder(userId,
      s"""
         |{
         |  "version": ""
         |}
         |""".stripMargin)
    val caught: Throwable = the[Throwable] thrownBy callRequest(requestBuilder)
    assertErrorResponse(requestBuilder, caught, result => {
      assert(result.status == 400)
      assertResponseBody(result,
        s"""
           |{
           |  "message": "入力値が不正です",
           |  "errors": [
           |    {
           |      "field": "version",
           |      "invalidValue": "null",
           |      "message": "空白は許可されていません"
           |    }
           |  ]
           |}
           |""".stripMargin)
    })
  }
  it should "param.versionが数値でない場合、バリデーションエラー" in {
    val userId = "1"
    val requestBuilder = updateRequestBuilder(userId,
      s"""
         |{
         |  "version": "test"
         |}
         |""".stripMargin)
    val caught: Throwable = the[Throwable] thrownBy callRequest(requestBuilder)
    assertErrorResponse(requestBuilder, caught, result => {
      assert(result.status == 400)
      assertResponseBody(result,
        s"""
           |{
           |  "message": "入力値が不正です",
           |  "errors": [
           |    {
           |      "field": "version",
           |      "invalidValue": "test",
           |      "message": "数値にしてください"
           |    }
           |  ]
           |}
           |""".stripMargin)
    })
  }
  it should "versionが一致していない場合、排他エラーとなること" in {
    val userId = "1"
    val requestBuilder = updateRequestBuilder(userId,
      s"""
         |{
         |  "name": "test",
         |  "version": "0"
         |}
         |""".stripMargin)
    val caught: Throwable = the[Throwable] thrownBy callRequest(requestBuilder)
    assertErrorResponse(requestBuilder, caught, result => {
      assert(result.status == 500)
      assertResponseBody(result,
        s"""
           |{
           |  "message": "排他エラー"
           |}
           |""".stripMargin)
    })
    // ユーザーが更新されていないことを確認する
    val user = tm.required(() => {
      SelectBuilder.newInstance(domaConfig)
        .sql(s"select * from users u where u.id = '${userId}'")
        .getEntitySingleResult(classOf[User])
    })
    assert(Json.toJson(user) == Json.parse(
      s"""{
         |  "id": "1",
         |  "name": "test_name1",
         |  "email": "test_email1@example.com",
         |  "version": 1
         |}
         |""".stripMargin))
  }
  it should "users.nameが更新されること" in {
    val userId = "1"
    val requestBuilder = updateRequestBuilder(userId,
      s"""
         |{
         |  "name": "test",
         |  "version": "1"
         |}
         |""".stripMargin)
    val result = callRequest(requestBuilder)

    assert(result.status == 204)

    // ユーザーが更新されていることを確認する
    val user = tm.required(() => {
      SelectBuilder.newInstance(domaConfig)
        .sql(s"select * from users u where u.id = '${userId}'")
        .getEntitySingleResult(classOf[User])
    })

    assert(Json.toJson(user) == Json.parse(
      s"""{
         |  "id": "1",
         |  "name": "test",
         |  "email": "test_email1@example.com",
         |  "version": 2
         |}
         |""".stripMargin))
  }
  it should "users.emailが更新されること" in {
    val userId = "1"
    val requestBuilder = updateRequestBuilder(userId,
      s"""
         |{
         |  "email": "test@example.com",
         |  "version": "1"
         |}
         |""".stripMargin)
    val result = callRequest(requestBuilder)

    assert(result.status == 204)

    // ユーザーが更新されていることを確認する
    val user = tm.required(() => {
      SelectBuilder.newInstance(domaConfig)
        .sql(s"select * from users u where u.id = '${userId}'")
        .getEntitySingleResult(classOf[User])
    })

    assert(Json.toJson(user) == Json.parse(
      s"""{
         |  "id": "1",
         |  "name": "test_name1",
         |  "email": "test@example.com",
         |  "version": 2
         |}
         |""".stripMargin))
  }
  it should "users.nameとusers.nameが更新されること" in {
    val userId = "1"
    val requestBuilder = updateRequestBuilder(userId,
      s"""
         |{
         |  "name": "test",
         |  "email": "test@example.com",
         |  "version": "1"
         |}
         |""".stripMargin)
    val result = callRequest(requestBuilder)

    assert(result.status == 204)

    // ユーザーが更新されていることを確認する
    val user = tm.required(() => {
      SelectBuilder.newInstance(domaConfig)
        .sql(s"select * from users u where u.id = '${userId}'")
        .getEntitySingleResult(classOf[User])
    })

    assert(Json.toJson(user) == Json.parse(
      s"""{
         |  "id": "1",
         |  "name": "test",
         |  "email": "test@example.com",
         |  "version": 2
         |}
         |""".stripMargin))
  }

  behavior of "DELETE /users/:userId delete"
  def deleteRequestBuilder(userId: String, paramString: String): RequestBuilder = {
    val param = Json.parse(paramString)
    fakeRequest()
      .method("DELETE")
      .uri(s"""/api/v1/users/${userId}""")
      .bodyJson(param)
  }
  it should "versionがない場合、バリデーションエラー" in {
    val userId = "1"
    val requestBuilder = deleteRequestBuilder(userId,
      s"""
         |{
         |  "version": null
         |}
         |""".stripMargin)
    val caught: Throwable = the[Throwable] thrownBy callRequest(requestBuilder)
    assertErrorResponse(requestBuilder, caught, result => {
      assert(result.status == 400)
      assertResponseBody(result,
        s"""
           |{
           |  "message": "入力値が不正です",
           |  "errors": [
           |    {
           |      "field": "version",
           |      "invalidValue": "null",
           |      "message": "null は許可されていません"
           |    }
           |  ]
           |}
           |""".stripMargin)
    })
  }
  it should "versionが数値でない場合、バリデーションエラー" in {
    val userId = "1"
    val requestBuilder = deleteRequestBuilder(userId,
      s"""
         |{
         |  "version": "test"
         |}
         |""".stripMargin)
    val caught: Throwable = the[Throwable] thrownBy callRequest(requestBuilder)
    assertErrorResponse(requestBuilder, caught, result => {
      assert(result.status == 400)
      assertResponseBody(result,
        s"""
           |{
           |  "message": "入力値が不正です",
           |  "errors": [
           |    {
           |      "field": "version",
           |      "invalidValue": "test",
           |      "message": "数値にしてください"
           |    }
           |  ]
           |}
           |""".stripMargin)
    })
  }
  it should "versionが一致しない場合、排他エラーとなること" in {
    val userId = "1"
    val requestBuilder = deleteRequestBuilder(userId,
      s"""
         |{
         |  "version": "0"
         |}
         |""".stripMargin)
    val caught: Throwable = the[Throwable] thrownBy callRequest(requestBuilder)
    assertErrorResponse(requestBuilder, caught, result => {
      assert(result.status == 500)
      assertResponseBody(result,
        s"""
           |{
           |  "message": "排他エラー"
           |}
           |""".stripMargin)
    })
    // ユーザーが削除されていないことを確認する
    val user = tm.required(() => {
      SelectBuilder.newInstance(domaConfig)
        .sql(s"select * from users u where u.id = '${userId}'")
        .getEntitySingleResult(classOf[User])
    })
    assert(user != null)
  }
  it should "userが削除されること" in {
    val userId = "1"
    val requestBuilder = deleteRequestBuilder(userId,
      s"""
         |{
         |  "version": "1"
         |}
         |""".stripMargin)
    val result = callRequest(requestBuilder)

    assert(result.status == 204)

    // ユーザーが削除されていることを確認する
    val user = tm.required(() => {
      SelectBuilder.newInstance(domaConfig)
        .sql(s"select * from users u where u.id = '${userId}'")
        .getEntitySingleResult(classOf[User])
    })

    assert(user == null)
  }

}

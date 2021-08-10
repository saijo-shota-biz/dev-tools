package com.systemya_saijo.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.seasar.doma.jdbc.OptimisticLockException;
import play.http.HttpErrorHandler;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
@Slf4j
public class CustomHttpErrorHandler implements HttpErrorHandler {

  @Override
  public CompletionStage<Result> onClientError(
      Http.RequestHeader request, int statusCode, String message) {
    return CompletableFuture.completedFuture(
        Results.status(statusCode, "A client error occurred: " + message));
  }

  @Override
  public CompletionStage<Result> onServerError(Http.RequestHeader request, Throwable exception) {
    if (exception.getCause() instanceof ApplicationException) {
      var e = (ApplicationException) exception.getCause();
      if (e instanceof ValidationException) {
        var errorResponse =
            new ValidationErrorResponse(e.getMessage(), ((ValidationException) e).getErrors());
        return CompletableFuture.completedFuture(Results.badRequest(Json.toJson(errorResponse)));
      }
      if (e instanceof NotFoundException) {
        var errorResponse = new ApplicationErrorResponse(e.getMessage());
        return CompletableFuture.completedFuture(Results.notFound(Json.toJson(errorResponse)));
      }
      if (e instanceof UniqueKeyException) {
        var errorResponse = new ApplicationErrorResponse(e.getMessage());
        return CompletableFuture.completedFuture(
            Results.internalServerError(Json.toJson(errorResponse)));
      }
    }
    if (exception.getCause() instanceof OptimisticLockException) {
      var errorResponse = new ApplicationErrorResponse("排他エラー");
      return CompletableFuture.completedFuture(
        Results.internalServerError(Json.toJson(errorResponse)));
    }
    return CompletableFuture.completedFuture(Results.internalServerError(exception.getMessage()));
  }
}

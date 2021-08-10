package com.systemya_saijo.user.controller;

import com.systemya_saijo.config.validation.Validator;
import com.systemya_saijo.core.controller.CreatedResponse;
import com.systemya_saijo.core.domain.vo.Link;
import com.systemya_saijo.core.exception.ApplicationErrorResponse;
import com.systemya_saijo.core.exception.NotFoundException;
import com.systemya_saijo.core.exception.ValidationErrorResponse;
import com.systemya_saijo.user.domain.User;
import com.systemya_saijo.user.domain.UserRepository;
import com.systemya_saijo.user.domain.UserService;
import com.systemya_saijo.user.domain.vo.UserId;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Slf4j
@Api(value = "/users")
public class UserController extends Controller {

  private final HttpExecutionContext ec;

  private final UserRepository userRepository;

  private final UserService userService;

  @Inject
  public UserController(
      HttpExecutionContext ec, UserRepository userRepository, UserService userService) {
    this.ec = ec;
    this.userRepository = userRepository;
    this.userService = userService;
  }

  @ApiOperation(value = "全てのユーザーを取得する")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "成功",
        responseContainer = "List",
        response = UserResource.class)
  })
  public CompletionStage<Result> list(Http.Request request) {
    return CompletableFuture.supplyAsync(userRepository::selectAll, ec.current())
        .thenApplyAsync(
            users ->
                users.stream()
                    .map(user -> new UserResource(user, Link.of(request, user.getId())))
                    .collect(Collectors.toList()))
        .thenApplyAsync(users -> ok(Json.toJson(users)), ec.current());
  }

  @ApiOperation(value = "指定のユーザーIDのユーザーを取得する")
  @ApiResponses({
    @ApiResponse(code = 200, message = "成功", response = User.class),
    @ApiResponse(
        code = 404,
        message = "指定のユーザーIDのユーザーが存在しない場合",
        response = ApplicationErrorResponse.class)
  })
  public CompletionStage<Result> show(
      Http.Request request,
      @ApiParam(name = "userId", value = "取得するユーザーID", required = true) String userId) {
    return CompletableFuture.supplyAsync(
            () -> userRepository.selectById(UserId.of(userId)), ec.current())
        .thenApplyAsync(
            userOptional ->
                userOptional.orElseThrow(() -> new NotFoundException("ユーザーIDが" + userId + "のユーザー")))
        .thenApplyAsync(user -> ok(Json.toJson(user)), ec.current());
  }

  @ApiOperation(value = "ユーザーを登録する")
  @ApiResponses({
    @ApiResponse(code = 201, message = "成功", response = CreatedResponse.class),
    @ApiResponse(code = 400, message = "入力値不正の場合", response = ValidationErrorResponse.class),
  })
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "body",
        name = "param",
        dataTypeClass = UserCreateCommand.class,
        required = true),
  })
  public CompletionStage<Result> create(Http.Request request) {
    return CompletableFuture.supplyAsync(() -> UserCreateCommand.of(request), ec.current())
        .thenApplyAsync(Validator::validate, ec.current())
        .thenApplyAsync(userService::create, ec.current())
        .thenApplyAsync(
            userId -> created(Json.toJson(new CreatedResponse(userId, Link.of(request, userId)))),
            ec.current());
  }

  @ApiOperation(value = "ユーザーを更新する")
  @ApiResponses({
    @ApiResponse(code = 201, message = "成功"),
    @ApiResponse(code = 400, message = "入力値不正の場合", response = ValidationErrorResponse.class)
  })
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "body",
        name = "param",
        dataTypeClass = UserUpdateCommand.class,
        required = true),
  })
  public CompletionStage<Result> update(
      Http.Request request,
      @ApiParam(name = "userId", value = "更新するユーザーID", required = true) String userId) {
    return CompletableFuture.supplyAsync(() -> UserUpdateCommand.of(request), ec.current())
        .thenApplyAsync(Validator::validate, ec.current())
        .thenApplyAsync(command -> userService.update(userId, command), ec.current())
        .thenApplyAsync(id -> noContent(), ec.current());
  }

  @ApiOperation(value = "ユーザーを削除する")
  @ApiResponses({
    @ApiResponse(code = 201, message = "成功"),
    @ApiResponse(code = 400, message = "入力値不正の場合", response = ValidationErrorResponse.class)
  })
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "body",
        name = "param",
        dataTypeClass = UserDeleteCommand.class,
        required = true),
  })
  public CompletionStage<Result> delete(
      Http.Request request,
      @ApiParam(name = "userId", value = "削除するユーザーID", required = true) String userId) {
    return CompletableFuture.supplyAsync(() -> UserDeleteCommand.of(request), ec.current())
        .thenApplyAsync(Validator::validate, ec.current())
        .thenApplyAsync(command -> userService.delete(userId, command), ec.current())
        .thenApplyAsync(id -> noContent(), ec.current());
  }
}

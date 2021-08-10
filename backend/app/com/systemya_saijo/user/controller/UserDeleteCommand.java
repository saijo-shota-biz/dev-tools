package com.systemya_saijo.user.controller;

import com.systemya_saijo.core.validation.Numeric;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import play.libs.Json;
import play.mvc.Http;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserDeleteCommand {
  @NotNull @Numeric private String version;

  public static UserDeleteCommand of(Http.Request request) {
    return Json.fromJson(request.body().asJson(), UserDeleteCommand.class);
  }
}

package com.systemya_saijo.user.controller;

import com.systemya_saijo.core.validation.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import play.libs.Json;
import play.mvc.Http;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserCreateCommand {
  @NotBlank @Email private String email;

  public static UserCreateCommand of(Http.Request request) {
    return Json.fromJson(request.body().asJson(), UserCreateCommand.class);
  }
}

package com.systemya_saijo.user.controller;

import com.systemya_saijo.core.validation.Email;
import com.systemya_saijo.core.validation.Numeric;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import play.libs.Json;
import play.mvc.Http;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserUpdateCommand {

  private String name;

  @Email private String email;

  @NotBlank @Numeric private String version;

  public static UserUpdateCommand of(Http.Request request) {
    return Json.fromJson(request.body().asJson(), UserUpdateCommand.class);
  }
}

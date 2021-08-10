package com.systemya_saijo.user.controller;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.systemya_saijo.core.domain.vo.Link;
import com.systemya_saijo.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResource {
  @JsonUnwrapped
  private User user;

  private Link link;
}

package com.systemya_saijo.core.controller;

import com.systemya_saijo.core.domain.vo.Id;
import com.systemya_saijo.core.domain.vo.Link;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreatedResponse {
  private final Id id;
  private final Link link;
}

package com.systemya_saijo.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApplicationErrorResponse {
  private String message;
}

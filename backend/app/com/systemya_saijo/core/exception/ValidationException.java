package com.systemya_saijo.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationException extends ApplicationException {

  private List<ValidateError> errors = new ArrayList<>();

  public ValidationException() {
    super("入力値が不正です");
  }

  public void addValidateError(ValidateError error) {
    errors.add(error);
  }

  @AllArgsConstructor
  @Getter
  public static class ValidateError {
    private String field;
    private String invalidValue;
    private String message;
  }
}

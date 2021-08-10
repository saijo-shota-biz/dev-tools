package com.systemya_saijo.core.exception;

import java.util.List;

public class ValidationErrorResponse extends ApplicationErrorResponse {

  private final List<ValidationException.ValidateError> errors;

  public ValidationErrorResponse(String message, List<ValidationException.ValidateError> errors) {
    super(message);
    this.errors = errors;
  }

  public List<ValidationException.ValidateError> getErrors() {
    return errors;
  }
}

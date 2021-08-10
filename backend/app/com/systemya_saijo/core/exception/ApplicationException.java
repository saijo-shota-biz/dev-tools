package com.systemya_saijo.core.exception;

public class ApplicationException extends RuntimeException {
  private String message;

  public ApplicationException(String message) {
    super(message);
    this.message = message;
  }

  @Override public String getMessage() {
    return this.message;
  }
}

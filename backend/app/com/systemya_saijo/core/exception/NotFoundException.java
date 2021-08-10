package com.systemya_saijo.core.exception;

public class NotFoundException extends ApplicationException {
  public NotFoundException() {
    super("指定のリソースが見つかりません");
  }

  public NotFoundException(String resourceName) {
    super(resourceName + "が見つかりません");
  }
}

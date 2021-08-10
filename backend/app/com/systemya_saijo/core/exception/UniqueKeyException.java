package com.systemya_saijo.core.exception;

public class UniqueKeyException extends ApplicationException {
  public UniqueKeyException() {
    super("ユニークキー重複");
  }
  public UniqueKeyException(String keyName) {
    super(keyName + "が重複しています");
  }
}

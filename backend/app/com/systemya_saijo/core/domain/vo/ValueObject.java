package com.systemya_saijo.core.domain.vo;

public abstract class ValueObject<T> {
  private T value;

  public T getValue() {
    return value;
  }

  public ValueObject(T value) {
    this.value = value;
  }

  @Override public String toString() {
    return value.toString();
  }
}

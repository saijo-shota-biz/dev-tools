package com.systemya_saijo.core.domain.vo;

import java.util.UUID;

public class Id extends StringValueObject{

  public Id() {
    super(UUID.randomUUID().toString());
  }

  public Id(String value) {
    super(value);
  }
}

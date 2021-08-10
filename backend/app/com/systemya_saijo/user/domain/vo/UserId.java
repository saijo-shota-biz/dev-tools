package com.systemya_saijo.user.domain.vo;

import com.systemya_saijo.core.domain.vo.Id;
import org.seasar.doma.Domain;

import java.util.UUID;

@Domain(valueType = String.class, factoryMethod = "of")
public class UserId extends Id {

  private UserId(String value) {
    super(value);
  }

  public static UserId newId() {
    return new UserId("user-" + UUID.randomUUID());
  }

  public static UserId of(String value) {
    return new UserId(value);
  }
}

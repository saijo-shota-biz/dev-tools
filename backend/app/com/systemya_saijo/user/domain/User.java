package com.systemya_saijo.user.domain;

import com.systemya_saijo.user.domain.vo.UserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "users")
@Entity(immutable = true)
public class User {

  @Id private UserId id;

  private String name;

  private String email;

  @Version
  private Integer version;

  public static User newUser(String email) {
    var user = new User();
    user.id = UserId.newId();
    user.name = email.split("@")[0];
    user.email = email;
    user.version = 0;
    return user;
  }

  public static User updateUser(UserId id, String name, String email, Integer version) {
    var user = new User();
    user.id = id;
    user.name = name;
    user.email = email;
    user.version = version;
    return user;
  }

  public static User deleteUser(UserId id, Integer version) {
    var user = new User();
    user.id = id;
    user.version = version;
    return user;
  }
}

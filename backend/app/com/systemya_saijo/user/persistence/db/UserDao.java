package com.systemya_saijo.user.persistence.db;

import com.systemya_saijo.core.persistence.db.InjectorConfig;
import com.systemya_saijo.user.domain.User;
import com.systemya_saijo.user.domain.vo.UserId;
import org.seasar.doma.*;
import org.seasar.doma.jdbc.Result;

import java.util.List;
import java.util.Optional;

@Dao
@InjectorConfig
public interface UserDao {

  @Select
  List<User> selectAll();

  @Select
  Optional<User> selectById(UserId id);

  @Select
  Optional<User> selectByIdAndVersion(UserId id, Integer version);

  @Select
  Optional<User> selectByEmail(String email);

  @Insert
  Result<User> insert(User user);

  @Update(excludeNull = true)
  Result<User> update(User user);

  @Delete
  Result<User> delete(User user);
}

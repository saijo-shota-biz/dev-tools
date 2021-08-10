package com.systemya_saijo.user.domain;

import com.systemya_saijo.user.domain.vo.UserId;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
  List<User> selectAll();
  Optional<User> selectById(UserId id);
  Optional<User> selectByEmail(String email);
  UserId create(User user);
  int update(User user);
  int delete(User user);
}

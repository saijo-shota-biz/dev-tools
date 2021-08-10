package com.systemya_saijo.user.domain;

import com.systemya_saijo.core.exception.UniqueKeyException;
import com.systemya_saijo.user.controller.UserCreateCommand;
import com.systemya_saijo.user.controller.UserDeleteCommand;
import com.systemya_saijo.user.controller.UserUpdateCommand;
import com.systemya_saijo.user.domain.vo.UserId;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class UserService {

  private final UserRepository userRepository;

  @Inject
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserId create(UserCreateCommand command) {
    checkDuplicateEmail(command.getEmail());

    var user = User.newUser(command.getEmail());
    return userRepository.create(user);
  }

  public int update(String id, UserUpdateCommand command) {
    checkDuplicateEmail(command.getEmail());

    var user =
        User.updateUser(
            UserId.of(id),
            command.getName(),
            command.getEmail(),
            Integer.valueOf(command.getVersion()));
    return userRepository.update(user);
  }

  public int delete(String id, UserDeleteCommand command) {
    var user = User.deleteUser(UserId.of(id), Integer.valueOf(command.getVersion()));
    return userRepository.delete(user);
  }

  public void checkDuplicateEmail(String email) {
    var userOptional = userRepository.selectByEmail(email);
    if (userOptional.isPresent()) {
      throw new UniqueKeyException("email");
    }
  }
}

package com.systemya_saijo.user.persistence.db;

import com.systemya_saijo.user.domain.User;
import com.systemya_saijo.user.domain.UserRepository;
import com.systemya_saijo.user.domain.vo.UserId;
import org.seasar.doma.jdbc.Config;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

  Config config;

  UserDao userDao;

  @Inject
  public UserRepositoryImpl(@Named("config") Config config, UserDao userDao) {
    this.config = config;
    this.userDao = userDao;
  }

  @Override
  public List<User> selectAll() {
    var tm = config.getTransactionManager();
    return tm.required(() -> userDao.selectAll());
  }

  @Override
  public Optional<User> selectById(UserId id) {
    var tm = config.getTransactionManager();
    return tm.required(() -> userDao.selectById(id));
  }

  @Override
  public Optional<User> selectByEmail(String email) {
    var tm = config.getTransactionManager();
    return tm.required(() -> userDao.selectByEmail(email));
  }

  @Override
  public UserId create(User user) {
    var tm = config.getTransactionManager();
    return tm.required(
        () -> {
          var result = userDao.insert(user);
          return result.getEntity().getId();
        });
  }

  @Override
  public int update(User user) {
    var tm = config.getTransactionManager();
    return tm.required(() -> {
      var result = userDao.update(user);
      return result.getCount();
    });
  }

  @Override
  public int delete(User user) {
    var tm = config.getTransactionManager();
    return tm.required(() -> {
      var result = userDao.delete(user);
      return result.getCount();
    });
  }
}

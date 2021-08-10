import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.systemya_saijo.config.json.CustomObjectMapper;
import com.systemya_saijo.core.persistence.db.DomaConfig;
import com.systemya_saijo.user.domain.UserRepository;
import com.systemya_saijo.user.persistence.db.UserDao;
import com.systemya_saijo.user.persistence.db.UserDaoImpl;
import com.systemya_saijo.user.persistence.db.UserRepositoryImpl;
import org.seasar.doma.jdbc.Config;

public class Module extends AbstractModule {
  @Override
  public void configure() {
    bind(Config.class).annotatedWith(Names.named("config")).to(DomaConfig.class).asEagerSingleton();
    bind(ObjectMapper.class).toProvider(CustomObjectMapper.class).asEagerSingleton();
    bind(UserRepository.class).to(UserRepositoryImpl.class).asEagerSingleton();
    bind(UserDao.class).to(UserDaoImpl.class).asEagerSingleton();
  }
}

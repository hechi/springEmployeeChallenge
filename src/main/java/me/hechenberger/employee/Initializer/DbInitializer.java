package me.hechenberger.employee.Initializer;

import me.hechenberger.employee.model.ApiUser;
import me.hechenberger.employee.respository.ApiUserRepository;
import me.hechenberger.employee.service.impl.ApiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1) // run this initializer first
@ConditionalOnProperty(name = "app.db-init", havingValue = "true")
public class DbInitializer implements CommandLineRunner {

  @Autowired
  private ApiUserRepository apiUserRepository;

  /**
   * Callback used to run the bean.
   *
   * @param args incoming main method arguments
   * @throws Exception on error
   */
  @Override
  public void run(String... args) throws Exception {

    ApiUser user = apiUserRepository.findByUsername("admin");
    if(user == null){
      // Add default admin user to database
      user = new ApiUser("admin",ApiUserService.passwordEncoder.encode("1234"));
      apiUserRepository.save(user);
    }
  }
}

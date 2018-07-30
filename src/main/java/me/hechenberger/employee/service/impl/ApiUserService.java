package me.hechenberger.employee.service.impl;

import me.hechenberger.employee.model.ApiUser;
import me.hechenberger.employee.respository.ApiUserRepository;
import me.hechenberger.employee.service.IApiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApiUserService extends AbstractGenericServiceImpl<ApiUser> implements IApiUserService {

  //TODO: extract it into a util class
  public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Autowired
  private ApiUserRepository apiUserRepository;

  @Override
  public JpaRepository getRepository() {
    return this.apiUserRepository;
  }

  @Override
  public ApiUser save(ApiUser user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return super.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    ApiUser apiUser = apiUserRepository.findByUsername(username);
    if (apiUser == null) {
      throw new UsernameNotFoundException(String.format("UserData %s does not exist!", username));
    }
    return apiUser;
  }
}

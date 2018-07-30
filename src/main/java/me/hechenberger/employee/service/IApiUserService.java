package me.hechenberger.employee.service;

import me.hechenberger.employee.model.ApiUser;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * The UserDetailsService made sure that the loadUserByUsername is possible
 */
public interface IApiUserService extends GenericService<ApiUser>, UserDetailsService {

  ApiUser registration(ApiUser user);
}

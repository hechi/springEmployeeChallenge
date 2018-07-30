package me.hechenberger.employee.configuration;

import me.hechenberger.employee.security.AuthenticationEntryPoint;
import me.hechenberger.employee.service.IApiUserService;
import me.hechenberger.employee.service.impl.ApiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configure spring security and add dummy user
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private AuthenticationEntryPoint entryPoint;

  @Autowired
  private IApiUserService apiUserService;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//    PasswordEncoder encoder = new BCryptPasswordEncoder();
    //add dummy entries
//    auth.inMemoryAuthentication().passwordEncoder(encoder).withUser("admin").password(encoder.encode("1234")).roles("ADMIN");
    auth.userDetailsService(apiUserService).passwordEncoder(ApiUserService.passwordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // allow all request except those how change an employee
    http
            .csrf().disable()
            .formLogin().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .httpBasic()
            .authenticationEntryPoint(entryPoint)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/v1/employee/**").permitAll()
            .antMatchers(HttpMethod.POST, "/api/v1/employee/**").authenticated()
            .antMatchers(HttpMethod.PUT, "/api/v1/employee/**").authenticated()
            .antMatchers(HttpMethod.DELETE, "/api/v1/employee/**").authenticated()
            .anyRequest().permitAll(); // ex. allow to call swagger-ui
  }
}

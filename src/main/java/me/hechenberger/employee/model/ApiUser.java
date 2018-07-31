package me.hechenberger.employee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This model defines a apiUser. It is separated from the employee model because not every employee need an api access.
 */
@Entity
@ApiModel(description = "User model, to allow different restful operations")
public class ApiUser implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(notes = "Unique uuid to identify a user")
  private Long id;

  @Column(unique = true)
  @NotNull
  @ApiModelProperty(notes = "Username and loginname of a user")
  private String username;

  @NotNull
  @ApiModelProperty(notes = "Password to login. This field will be stored as an hash in the database")
  private String password;

  public ApiUser() {
  }

  public ApiUser(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    return authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  public void setPassword(String newPassword) {
    this.password = newPassword;
  }

  /**
   * {@link UserDetails#isAccountNonExpired()}
   *
   * @return
   */
  @Override
  @JsonIgnore
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * {@link UserDetails#isAccountNonLocked()}
   *
   * @return
   */
  @Override
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * {@link UserDetails#isCredentialsNonExpired()}
   *
   * @return
   */
  @Override
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * {@link UserDetails#isEnabled()}
   *
   * @return
   */
  @Override
  @JsonIgnore
  public boolean isEnabled() {
    return true;
  }
}

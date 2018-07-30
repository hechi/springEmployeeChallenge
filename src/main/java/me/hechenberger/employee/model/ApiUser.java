package me.hechenberger.employee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table
public class ApiUser implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  private Long id;

  @Column(unique = true)
  @NotNull
  private String username;

  @NotNull
  private String password;

  public ApiUser() {
  }

  public ApiUser(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Override
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

  public void setPassword(String newPassword){
    this.password = newPassword;
  }

  /**
   * {@link UserDetails#isAccountNonExpired()}
   * @return
   */
  @Override
  @JsonIgnore
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * {@link UserDetails#isAccountNonLocked()}
   * @return
   */
  @Override
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * {@link UserDetails#isCredentialsNonExpired()}
   * @return
   */
  @Override
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * {@link UserDetails#isEnabled()}
   * @return
   */
  @Override
  @JsonIgnore
  public boolean isEnabled() {
    return true;
  }
}

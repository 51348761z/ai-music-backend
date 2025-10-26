package org.demo.aimusic.module.auth.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.ToString;
import org.demo.aimusic.module.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

@Getter
@ToString
public class LoginUserDetails implements UserDetails {

  private final User user;
  private final Set<GrantedAuthority> authorities;

  public LoginUserDetails(User user) {
    this.user =
        Optional.ofNullable(user)
            .orElseThrow(() -> new IllegalArgumentException("User cannot be null"));
    this.authorities = mapRolesToAuthorities(user.getRole());
  }

  private Set<GrantedAuthority> mapRolesToAuthorities(String role) {
    if (!StringUtils.hasText(role)) {
      return Collections.emptySet();
    }
    return Set.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return user.getPasswordHash();
  }

  /**
   * Get username, which is the email in this case.
   *
   * @return the email of the user
   */
  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return "enable".equalsIgnoreCase(user.getStatus());
  }

  public User getUserEntity() {
    return this.user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LoginUserDetails that = (LoginUserDetails) o;
    return getUsername() != null
        ? getUsername().equals(that.getUsername())
        : that.getUsername() == null;
  }

  @Override
  public int hashCode() {
    return getUsername() != null ? getUsername().hashCode() : 0;
  }
}

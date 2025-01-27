package com.watchtogether.watchtogether.member.security;

import com.watchtogether.watchtogether.member.entity.Role;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Builder
public class CustomUserDetail implements UserDetails {

  private String memberId;
  private Role role;
  private String password;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(role.name()));
    return authorities;
  }

  @Override
  public String getUsername() {
    return this.memberId;
  }
}

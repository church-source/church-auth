package org.churchsource.churchauth.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import lombok.*;
import org.churchsource.churchauth.model.ChurchAuthEntity;
import org.churchsource.churchauth.user.role.Privilege;
import org.churchsource.churchauth.user.role.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = UserNamedQueryConstants.NAME_GET_ALL_USERS,
                query = UserNamedQueryConstants.QUERY_GET_ALL_USERS),
        @NamedQuery(name = UserNamedQueryConstants.NAME_FIND_USER_BY_ID,
                query = UserNamedQueryConstants.QUERY_FIND_USER_BY_ID),
        @NamedQuery(name = UserNamedQueryConstants.NAME_FIND_USER_BY_USERNAME,
                query = UserNamedQueryConstants.QUERY_FIND_USER_BY_USERNAME)
})

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "User")
public class CPUserDetails extends ChurchAuthEntity<Long> implements UserDetails  {

  private String email;
  private String username;
  private String password;
  private boolean enabled;
  private boolean forcePasswordChange;

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
  @JoinTable(name = "UserRole", joinColumns = @JoinColumn(name = "user", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role", referencedColumnName = "id"))
  private List<Role> roles;

  @Builder(builderMethodName = "aCPUserDetails")
  public CPUserDetails(Long id, Date created, Date modified, Boolean deleted, String email, String username, String password, List<Role> roles, boolean isEnabled, boolean forcePasswordChange) {
    super(id, created, modified, deleted);
    this.email = email;
    this.username = username;
    this.password = password;
    this.roles = roles;
    this.enabled = isEnabled;
    this.forcePasswordChange = forcePasswordChange;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return !forcePasswordChange;
  }

  @JsonIgnore
  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if(this.roles==null)
      return null;

    return getGrantedAuthorities(getPrivileges(roles));
  }

  private final List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
    final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    for (final String privilege : privileges) {
      authorities.add(new SimpleGrantedAuthority(privilege));
    }
    return authorities;
  }

  private final List<String> getPrivileges(final Collection<Role> roles) {
    final List<String> privileges = new ArrayList<String>();
    final List<Privilege> collection = new ArrayList<Privilege>();
    for (final Role role : roles) {
      if(role.getPrivileges() != null)
        collection.addAll(role.getPrivileges());
    }
    for (final Privilege item : collection) {
      privileges.add(item.getName());
    }

    return privileges;
  }
}



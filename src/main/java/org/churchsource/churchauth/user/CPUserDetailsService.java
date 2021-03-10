package org.churchsource.churchauth.user;

import org.churchsource.churchauth.security.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;

@Service
public class CPUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserFactory userFactory;

  @Autowired
  private PasswordUtils passwordUtils;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      return userRepository.findUserByUserName(username);
    } catch (NoResultException nre) {
      throw new UsernameNotFoundException("User with username: " + username +" not found.", nre);
    }
  }

  public UserFullViewModel saveNewUser(UserBackingForm form) {
    CPUserDetails userDetails = userFactory.createUserEntity(form);
    // Note this should have been validated prior to this call, but just double checking here.
    if("".equals(form.getPassword()) || form.getPassword() == null) {
      throw new IllegalStateException("Password is required for new users and should be present in the UserBackingForm.");
    }
    userDetails.setForcePasswordChange(true);
    CPUserDetails createdUser = userRepository.save(userDetails);
    if(createdUser != null) {
      return userFactory.createUserFullViewModelFromEntity(createdUser);
    } else {
      return null;
    }
  }

  public UserFullViewModel updateUser(UserBackingForm form) {
    CPUserDetails userDetails = userFactory.createUserEntity(form);

    CPUserDetails updatedUser = userRepository.updateUser(userDetails);
    if(updatedUser != null) {
      return userFactory.createUserFullViewModelFromEntity(updatedUser);
    } else {
      return null;
    }
  }

    public UserFullViewModel changePassword(CPUserDetails user, String newPassword) {
      user.setForcePasswordChange(false);
      user.setPassword(passwordUtils.getEncodedPassword(newPassword));
      return  userFactory.createUserFullViewModelFromEntity(userRepository.updateUser(user));
    }
}



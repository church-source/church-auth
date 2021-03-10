package org.churchsource.churchauth.user;

import org.churchsource.churchauth.security.PasswordUtils;
import org.churchsource.churchauth.user.role.Privilege;
import org.churchsource.churchauth.user.role.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.churchsource.churchauth.user.CPUserDetails.aCPUserDetails;
import static org.churchsource.churchauth.user.role.Privilege.aPrivilege;
import static org.churchsource.churchauth.user.role.Role.aRole;
import static org.churchsource.churchauth.user.UserFullViewModel.aUserFullViewModel;
import static org.churchsource.churchauth.user.helpers.UserFullViewModelMatcher.hasSameStateAsUserFullViewModel;
import static org.churchsource.churchauth.user.helpers.CPUserDetailsMatcher.hasSameStateAsCPUserDetails;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserFactoryTest {

  @InjectMocks
  private UserFactory userFactory;

  @Mock
  private PasswordUtils passwordUtils;

  @Test
  public void testCreateUserFullViewModelFromEntity_shouldReturnCorrectFullViewModel() {
    List<Role> roles = new ArrayList<Role>();
    List<Privilege> privileges = new ArrayList<Privilege>();
    privileges.add(aPrivilege().name("priv1").build());
    privileges.add(aPrivilege().name("priv2").build());
    roles.add(aRole().name("role_1").privileges(privileges).build());

    CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").password("Bar").isEnabled(true).deleted(false).roles(roles).build();

    UserFullViewModel expectedUserFullViewModel = aUserFullViewModel().id(1L).username("Joe").password("Bar").isEnabled(true).isExpired(false).isLocked(false).roles(roles).build();

    UserFullViewModel createdUserFullViewModel = userFactory.createUserFullViewModelFromEntity(aCPUserDetails);

    assertThat(createdUserFullViewModel, hasSameStateAsUserFullViewModel(expectedUserFullViewModel));
  }

  @Test
  public void testCreateUserFullViewModelFromEntityWithoutRoles_shouldReturnCorrectFullViewModel() {
    CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").password("Bar").isEnabled(true).deleted(false).roles(null).build();

    UserFullViewModel expectedUserFullViewModel = aUserFullViewModel().id(1L).username("Joe").password("Bar").isEnabled(true).isExpired(false).isLocked(false).roles(null).build();

    UserFullViewModel createdUserFullViewModel = userFactory.createUserFullViewModelFromEntity(aCPUserDetails);

    assertThat(createdUserFullViewModel, hasSameStateAsUserFullViewModel(expectedUserFullViewModel));
  }

  @Test
  public void testCreateCPUserEntityFromBackingForm_shouldReturnCorrectEntity() {

    List<Role> roles = new ArrayList<Role>();
    roles.add(aRole().name("role_1").build());
    roles.add(aRole().name("role_2").build());

    String password = "password";
    String encodedPassword = "EncodedPassword";
    CPUserDetails expectedCPUserDetails = aCPUserDetails().id(1L).username("Joe").password(encodedPassword).isEnabled(true).deleted(false).roles(roles).build();
    UserBackingForm userBackingForm = UserBackingForm.aUserBackingForm().id(1L).username("Joe").password(password).roles(roles).build();

    when(passwordUtils.getEncodedPassword(password)).thenReturn(encodedPassword);
    CPUserDetails createdUserEntity = userFactory.createUserEntity(userBackingForm);

    assertThat(createdUserEntity, hasSameStateAsCPUserDetails(expectedCPUserDetails));
  }

  @Test
  public void testCreateCPUserEntityFromBackingFormWithNullPassword_shouldReturnCorrectEntity() {

    List<Role> roles = new ArrayList<Role>();
    roles.add(aRole().name("role_1").build());
    roles.add(aRole().name("role_2").build());

    CPUserDetails expectedCPUserDetails = aCPUserDetails().id(1L).username("Joe").password(null).forcePasswordChange(false).isEnabled(true).deleted(false).roles(roles).build();
    UserBackingForm userBackingForm = UserBackingForm.aUserBackingForm().id(1L).username("Joe").password(null).roles(roles).build();

    CPUserDetails createdUserEntity = userFactory.createUserEntity(userBackingForm);
    verify(passwordUtils, never()).getEncodedPassword(null);

    assertThat(createdUserEntity, hasSameStateAsCPUserDetails(expectedCPUserDetails));
  }
}
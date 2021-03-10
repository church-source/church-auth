package org.churchsource.churchauth.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;

import static org.churchsource.churchauth.user.CPUserDetails.aCPUserDetails;
import static org.churchsource.churchauth.user.helpers.CPUserDetailsMatcher.hasSameStateAsCPUserDetails;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CPUserDetailsServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CPUserDetailsService cpUserDetailsService;

  @Test
  public void testFindCPUserDetailsByAnyName_shouldRetreiveNonDeletedUserWithUserName() {
    CPUserDetails aUser = aCPUserDetails().username("Joe").password("Bar").deleted(false).build();
    when(userRepository.findUserByUserName("Joe")).thenReturn(aUser);
    CPUserDetails returnedUser = (CPUserDetails) cpUserDetailsService.loadUserByUsername("Joe");
    assertThat(returnedUser, hasSameStateAsCPUserDetails(aUser));
  }

  @Test(expected = UsernameNotFoundException.class)
  public void testFindCPUserDetailsByNameThatIsDeleted_shouldNotReturnResult() {
    when(userRepository.findUserByUserName("Knob")).thenThrow(new NoResultException());
    cpUserDetailsService.loadUserByUsername("Knob");
  }
}

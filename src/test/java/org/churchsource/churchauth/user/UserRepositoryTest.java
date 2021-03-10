package org.churchsource.churchauth.user;

import org.churchsource.churchauth.user.role.Privilege;
import org.churchsource.churchauth.user.role.Role;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.*;

import static org.churchsource.churchauth.user.role.helpers.RoleMatcher.hasSameStateAsRole;
import static org.churchsource.churchauth.user.CPUserDetails.aCPUserDetails;
import static org.churchsource.churchauth.user.helpers.CPUserDetailsMatcher.hasSameStateAsCPUserDetails;
import static org.churchsource.churchauth.user.role.Privilege.aPrivilege;
import static org.churchsource.churchauth.user.role.Role.aRole;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserRepositoryTest {

  @PersistenceContext
  protected EntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testSaveCPUserDetails_shouldPersistCPUserDetails() {
    List<Role> roles = new ArrayList<Role>();
    List<Privilege> privileges = new ArrayList<Privilege>();
    Privilege priv1 = aPrivilege().name("priv1").deleted(false).build();
    Privilege priv2 = aPrivilege().name("priv2").deleted(false).build();
    entityManager.persist(priv1);
    entityManager.persist(priv2);
    entityManager.flush();
    privileges.add(priv1);
    privileges.add(priv2);
    Role aRole = aRole().name("role_1").privileges(privileges).deleted(false).build();
    entityManager.persist(aRole);
    entityManager.flush();
    roles.add(aRole);

    CPUserDetails aCPUserDetails = aCPUserDetails().username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles).build();

    CPUserDetails savedCPUserDetails = userRepository.save(aCPUserDetails);

    // check that the CPUserDetails entity was persisted correctly
    assertThat(savedCPUserDetails.getId(), is(IsNull.notNullValue()));

    CPUserDetails retrievedCPUserDetails = entityManager.createQuery("SELECT u FROM CPUserDetails u WHERE u.id = :id", CPUserDetails.class)
        .setParameter("id", savedCPUserDetails.getId()).getSingleResult();
    Collection<Role> retrievedRoles = retrievedCPUserDetails.getRoles();

    assertThat(retrievedCPUserDetails, hasSameStateAsCPUserDetails(savedCPUserDetails));
    assertThat(retrievedRoles.size(), is(1));
    assertThat((Role)retrievedRoles.toArray()[0], hasSameStateAsRole(aRole));
  }

  @Test (expected = org.springframework.dao.InvalidDataAccessApiUsageException.class)
  public void testSaveCPUserDetailsWithRoleThatDoesNotExist_shouldNotPersistCPUserDetails() {
    List<Role> roles = new ArrayList<Role>();
    List<Privilege> privileges = new ArrayList<Privilege>();
    Privilege priv1 = aPrivilege().name("priv1").deleted(false).build();
    Privilege priv2 = aPrivilege().name("priv2").deleted(false).build();
    entityManager.persist(priv1);
    entityManager.persist(priv2);
    entityManager.flush();
    privileges.add(priv1);
    privileges.add(priv2);
    Role aRole = aRole().name("role_1").privileges(privileges).deleted(false).build();
    roles.add(aRole);

    CPUserDetails aCPUserDetails = aCPUserDetails().username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles).build();

    CPUserDetails savedCPUserDetails = userRepository.save(aCPUserDetails);
  }

  @Test(expected = java.lang.IllegalStateException.class)
  public void testSaveCPUserDetailsWithRolePriviligeThatDoesNotExist_shouldNotPersistCPUserDetails() {
    List<Role> roles = new ArrayList<Role>();
    List<Privilege> privileges = new ArrayList<Privilege>();
    Privilege priv1 = aPrivilege().name("priv1").deleted(false).build();
    Privilege priv2 = aPrivilege().name("priv2").deleted(false).build();

    privileges.add(priv1);
    privileges.add(priv2);
    Role aRole = aRole().name("role_1").privileges(privileges).deleted(false).build();
    entityManager.persist(aRole);
    entityManager.flush();
    roles.add(aRole);
    CPUserDetails aCPUserDetails = aCPUserDetails().username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles).build();

    CPUserDetails savedCPUserDetails = userRepository.save(aCPUserDetails);
  }

  @Test
  public void testUpdateCPUserDetails_shouldMergeCPUserDetails() {
    List<Role> roles = new ArrayList<Role>();
    List<Privilege> privileges = new ArrayList<Privilege>();
    Privilege priv1 = aPrivilege().name("priv1").deleted(false).build();
    Privilege priv2 = aPrivilege().name("priv2").deleted(false).build();
    entityManager.persist(priv1);
    entityManager.persist(priv2);
    entityManager.flush();
    privileges.add(priv1);
    privileges.add(priv2);
    Role aRole = aRole().name("role_1").privileges(privileges).deleted(false).build();
    entityManager.persist(aRole);
    entityManager.flush();
    roles.add(aRole);

    CPUserDetails aCPUserDetails = aCPUserDetails().username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles).build();

    entityManager.persist(aCPUserDetails);
    entityManager.flush();

    CPUserDetails newUpdatedCPUserDetails = aCPUserDetails().id(aCPUserDetails.getId()).username("JoeUpdated").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles).build();

    CPUserDetails updatedMergedCPUserDetails = userRepository.updateUser(newUpdatedCPUserDetails);

    // check that the CPUserDetails entity was persisted correctly
    assertThat(updatedMergedCPUserDetails.getId(), is(IsNull.notNullValue()));

    CPUserDetails retrievedCPUserDetails = entityManager.createQuery("SELECT u FROM CPUserDetails u WHERE u.id = :id", CPUserDetails.class)
        .setParameter("id", updatedMergedCPUserDetails.getId()).getSingleResult();

    assertThat(retrievedCPUserDetails, hasSameStateAsCPUserDetails(newUpdatedCPUserDetails));
  }

  @Test
  public void testUpdateCPUserDetailsWithoutRoles_shouldMergeCPUserDetailsAndRemoveRoles() {
    List<Role> roles = new ArrayList<Role>();
    List<Privilege> privileges = new ArrayList<Privilege>();
    Privilege priv1 = aPrivilege().name("priv1").deleted(false).build();
    Privilege priv2 = aPrivilege().name("priv2").deleted(false).build();
    entityManager.persist(priv1);
    entityManager.persist(priv2);
    entityManager.flush();
    privileges.add(priv1);
    privileges.add(priv2);
    Role aRole = aRole().name("role_1").privileges(privileges).deleted(false).build();
    entityManager.persist(aRole);
    entityManager.flush();
    roles.add(aRole);

    CPUserDetails aCPUserDetails = aCPUserDetails().username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles).build();

    entityManager.persist(aCPUserDetails);
    entityManager.flush();

    CPUserDetails newUpdatedCPUserDetails = aCPUserDetails().id(aCPUserDetails.getId()).username("JoeUpdated").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(null).build();

    CPUserDetails updatedMergedCPUserDetails = userRepository.updateUser(newUpdatedCPUserDetails);

    // check that the CPUserDetails entity was persisted correctly
    assertThat(updatedMergedCPUserDetails.getId(), is(IsNull.notNullValue()));

    CPUserDetails retrievedCPUserDetails = entityManager.createQuery("SELECT u FROM CPUserDetails u WHERE u.id = :id", CPUserDetails.class)
        .setParameter("id", updatedMergedCPUserDetails.getId()).getSingleResult();

    assertThat(retrievedCPUserDetails, hasSameStateAsCPUserDetails(newUpdatedCPUserDetails));

    List<Role> retrievedRoles = retrievedCPUserDetails.getRoles();

    assertThat(retrievedCPUserDetails, hasSameStateAsCPUserDetails(newUpdatedCPUserDetails));
    assertThat(retrievedRoles, is((List<Role>)null));
  }

  @Test
  public void testUpdateCPUserDetailsWithNullRoles_shouldMergeCPUserDetails() {
    CPUserDetails aCPUserDetails = aCPUserDetails().username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(null).build();

    entityManager.persist(aCPUserDetails);
    entityManager.flush();

    CPUserDetails newUpdatedCPUserDetails = aCPUserDetails().id(aCPUserDetails.getId()).username("JoeUpdated").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(null).build();

    CPUserDetails updatedMergedCPUserDetails = userRepository.updateUser(newUpdatedCPUserDetails);

    // check that the CPUserDetails entity was persisted correctly
    assertThat(updatedMergedCPUserDetails.getId(), is(IsNull.notNullValue()));

    CPUserDetails retrievedCPUserDetails = entityManager.createQuery("SELECT u FROM CPUserDetails u WHERE u.id = :id", CPUserDetails.class)
            .setParameter("id", updatedMergedCPUserDetails.getId()).getSingleResult();

    assertThat(retrievedCPUserDetails, hasSameStateAsCPUserDetails(newUpdatedCPUserDetails));
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void testUpdateCPUserDetailsThatDoesNotExist_shouldThrowException() {
    CPUserDetails CPUserDetails = aCPUserDetails().username("Joe").email("joe@bar.com").password("Bar").deleted(false).build();

    CPUserDetails newUpdatedCPUserDetails = aCPUserDetails().id(999L).username("JoeUpdated").email("joe@bar.com").deleted(CPUserDetails.getDeleted()).build();

    userRepository.updateUser(newUpdatedCPUserDetails);
  }

  @Test
  public void testDeleteCPUserDetails_shouldMarkCPUserDetailsAsDeleted() {
    CPUserDetails aCPUserDetails = aCPUserDetails().username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(null).build();


    entityManager.persist(aCPUserDetails);
    entityManager.flush();

    CPUserDetails deleted = aCPUserDetails()
            .id(aCPUserDetails.getId())
            .username(aCPUserDetails.getUsername())
            .email(aCPUserDetails.getEmail())
            .password(aCPUserDetails.getPassword())
            .isEnabled(aCPUserDetails.isEnabled())
            .roles(aCPUserDetails.getRoles())
            .deleted(true)
            .build();

    userRepository.deleteUser(aCPUserDetails.getId());

    // TODO check that the associated entities has been deleted in a cascade event, if set to do so
    // (NONE YET PRESENT)

    CPUserDetails retrievedCPUserDetails = entityManager.createQuery("SELECT p FROM CPUserDetails p WHERE p.id = :id", CPUserDetails.class)
        .setParameter("id", deleted.getId()).getSingleResult();

    assertThat(retrievedCPUserDetails, hasSameStateAsCPUserDetails(deleted));
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void testDeleteCPUserDetailsThatDoesNotExist_shouldThrowException() {
    userRepository.deleteUser(999L);
  }

  @Test
  public void testFindCPUserDetailsByIdThatExists_shouldFindCPUserDetails() {
    CPUserDetails CPUserDetails = aCPUserDetails().username("Joe").email("joe@bar.com").password("Bar").deleted(false).build();

    entityManager.persist(CPUserDetails);
    entityManager.flush();

    CPUserDetails retrievedCPUserDetails = userRepository.findUserById(CPUserDetails.getId());

    assertThat(retrievedCPUserDetails, hasSameStateAsCPUserDetails(CPUserDetails));
  }

  @Test(expected = NoResultException.class)
  public void testFindCPUserDetailsByIdThatDoesNotExists_shouldThrowAnException() {
    userRepository.findUserById(999L);
  }

  @Test
  public void testFindCPUserDetailsByUserName_shouldRetreiveNonDeletedUserWithUserName() {
    CPUserDetails aUser = aCPUserDetails().username("Joe").email("joe@bar.com").password("Bar").deleted(false).build();
    entityManager.persist(aUser);
    entityManager.flush();

    CPUserDetails aUser2 = aCPUserDetails().username("JoeSoap").email("joesoap@bar.com").password("Bar").deleted(false).build();
    entityManager.persist(aUser2);
    entityManager.flush();

    CPUserDetails aUser3 = aCPUserDetails().username("JoeSoapDish").email("joesoapdish@bar.com").password("Bar").deleted(false).build();
    entityManager.persist(aUser3);
    entityManager.flush();

    CPUserDetails user = userRepository.findUserByUserName("Joe");
    assertThat(user, hasSameStateAsCPUserDetails(aUser));
  }

  @Test
  public void testFindCPUserDetailsByUserNameThatIsEmailAddress_shouldRetreiveNonDeletedUserWithEmailAddress() {
    CPUserDetails aUser = aCPUserDetails().username("Joe").email("joe@bar.com").password("Bar").deleted(false).build();
    entityManager.persist(aUser);
    entityManager.flush();

    CPUserDetails aUser2 = aCPUserDetails().username("JoeSoap").email("joesoap@bar.com").password("Bar").deleted(false).build();
    entityManager.persist(aUser2);
    entityManager.flush();

    CPUserDetails aUser3 = aCPUserDetails().username("JoeSoapDish").email("joesoapdish@bar.com").password("Bar").deleted(false).build();
    entityManager.persist(aUser3);
    entityManager.flush();

    CPUserDetails user = userRepository.findUserByUserName("joe@bar.com");
    assertThat(user, hasSameStateAsCPUserDetails(aUser));
  }

  @Test(expected = NoResultException.class)
  public void testFindCPUserDetailsByNameThatIsDeleted_shouldNotReturnResult() {
    CPUserDetails aUser = aCPUserDetails().username("Joe").email("joe@bar.com").password("Bar").deleted(true).build();
    entityManager.persist(aUser);
    entityManager.flush();

    CPUserDetails aUser2 = aCPUserDetails().username("JoeSoap").email("joesoap@bar.com").password("Bar").deleted(false).build();
    entityManager.persist(aUser2);
    entityManager.flush();

    CPUserDetails aUser3 = aCPUserDetails().username("JoeSoapDish").email("joesoapdish@bar.com").password("Bar").deleted(false).build();
    entityManager.persist(aUser3);
    entityManager.flush();

    CPUserDetails user = userRepository.findUserByUserName("Joe");
  }

  @Test(expected = NoResultException.class)
  public void testFindCPUserDetailsByNameThatIsNotValid_shouldNotReturnResult() {
    CPUserDetails aUser = aCPUserDetails().username("Joe").email("joe@bar.com").password("Bar").deleted(true).build();
    entityManager.persist(aUser);
    entityManager.flush();

    CPUserDetails user = userRepository.findUserByUserName("Knob");
  }
}

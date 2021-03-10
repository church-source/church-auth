package org.churchsource.churchauth.user.role;

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
import java.util.ArrayList;
import java.util.List;

import static org.churchsource.churchauth.user.role.Role.aRole;
import static org.churchsource.churchauth.user.role.helpers.RoleMatcher.hasSameStateAsRole;
import static org.churchsource.churchauth.user.role.helpers.PrivilegeMatcher.hasSameStateAsPrivilege;
import static org.churchsource.churchauth.user.role.Privilege.aPrivilege;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RoleRepositoryTest {

  @PersistenceContext
  protected EntityManager entityManager;

  @Autowired
  private RoleRepository roleRepository;

  @Test
  public void testSaveRole_shouldPersistRole() {
    List<Privilege> privileges = new ArrayList<Privilege>();
    Privilege priv1 = aPrivilege().name("priv1").deleted(false).build();
    Privilege priv2 = aPrivilege().name("priv2").deleted(false).build();
    entityManager.persist(priv1);
    entityManager.persist(priv2);
    entityManager.flush();
    privileges.add(priv1);
    privileges.add(priv2);
    Role aRole = aRole().name("role_1").privileges(privileges).deleted(false).build();

    Role savedRole = roleRepository.save(aRole);

    // check that the Role entity was persisted correctly
    assertThat(savedRole.getId(), is(IsNull.notNullValue()));

    Role retrievedRole = entityManager.createQuery("SELECT r FROM Role r WHERE r.id = :id", Role.class)
        .setParameter("id", savedRole.getId()).getSingleResult();

    assertThat(retrievedRole, hasSameStateAsRole(aRole));
  }

  @Test(expected = org.springframework.dao.InvalidDataAccessApiUsageException.class)
  public void testSaveRoleWithRolePriviligeThatDoesNotExist_shouldNotPersistRole() {
    List<Privilege> privileges = new ArrayList<Privilege>();
    Privilege priv1 = aPrivilege().name("priv1").deleted(false).build();
    Privilege priv2 = aPrivilege().name("priv2").deleted(false).build();

    privileges.add(priv1);
    privileges.add(priv2);
    Role aRole = aRole().name("role_1").privileges(privileges).deleted(false).build();

    roleRepository.save(aRole);
  }


  @Test
  public void testUpdateRole_shouldMergeRole() {
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

    List<Privilege> newPrivileges = new ArrayList<Privilege>();
    Privilege priv3 = aPrivilege().name("priv3").deleted(false).build();
    entityManager.persist(priv3);
    entityManager.flush();
    newPrivileges.add(priv1);
    newPrivileges.add(priv3);

    Role newUpdatedRole = aRole().id(aRole.getId()).name("role_1").privileges(newPrivileges).deleted(false).build();

    Role updatedMergedRole = roleRepository.updateRole(newUpdatedRole);

    // check that the Role entity was persisted correctly
    assertThat(updatedMergedRole.getId(), is(IsNull.notNullValue()));

    Role retrievedRole = entityManager.createQuery("SELECT u FROM Role u WHERE u.id = :id", Role.class)
        .setParameter("id", updatedMergedRole.getId()).getSingleResult();

    assertThat(retrievedRole, hasSameStateAsRole(newUpdatedRole));
  }

  @Test
  public void testUpdateRoleWithoutPrivileges_shouldMergeRoleAndRemovePrivileges() {
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

    Role newUpdatedRole = aRole().id(aRole.getId()).name("role_1").privileges(null).deleted(false).build();

    Role updatedMergedRole = roleRepository.updateRole(newUpdatedRole);

    // check that the Role entity was persisted correctly
    assertThat(updatedMergedRole.getId(), is(IsNull.notNullValue()));

    Role retrievedRole = entityManager.createQuery("SELECT u FROM Role u WHERE u.id = :id", Role.class)
            .setParameter("id", updatedMergedRole.getId()).getSingleResult();

    assertThat(retrievedRole, hasSameStateAsRole(newUpdatedRole));
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void testUpdateRoleThatDoesNotExist_shouldThrowException() {

    Role newUpdatedRole = aRole().id(999L).name("role_1").privileges(null).deleted(false).build();

    roleRepository.updateRole(newUpdatedRole);
  }

  @Test
  public void testDeleteRole_shouldMarkRoleAsDeleted() {
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

    Role deleted = aRole().id(aRole.getId()).name(aRole.getName()).privileges(aRole.getPrivileges()).deleted(true).build();

    roleRepository.deleteRole(aRole.getId());

    Role retrievedRole = entityManager.createQuery("SELECT p FROM Role p WHERE p.id = :id", Role.class)
        .setParameter("id", aRole.getId()).getSingleResult();

    assertThat(retrievedRole, hasSameStateAsRole(deleted));

    // Check that linked privilege has not been deleted
    Privilege privilege = entityManager.createQuery("SELECT p FROM Privilege p WHERE p.id = :id", Privilege.class)
            .setParameter("id", priv1.getId()).getSingleResult();
    assertThat(privilege, hasSameStateAsPrivilege(priv1));
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void testDeleteRoleThatDoesNotExist_shouldThrowException() {
    roleRepository.deleteRole(999L);
  }

  @Test
  public void testFindRoleByIdThatExists_shouldFindRole() {
    Role aRole = aRole().name("role_1").privileges(null).deleted(false).build();

    entityManager.persist(aRole);
    entityManager.flush();

    Role retrievedRole = roleRepository.findRoleById(aRole.getId());

    assertThat(retrievedRole, hasSameStateAsRole(aRole));
  }

  @Test(expected = NoResultException.class)
  public void testFindRoleByIdThatDoesNotExists_shouldThrowAnException() {
    roleRepository.findRoleById(999L);
  }

  @Test
  public void testFindRoleByRoleName_shouldRetreiveNonDeletedRoleWithRoleName() {
    Role aRole = aRole().name("role_1").privileges(null).deleted(false).build();
    entityManager.persist(aRole);
    entityManager.flush();

    Role aRole2 = aRole().name("role_2").privileges(null).deleted(false).build();
    entityManager.persist(aRole2);
    entityManager.flush();

    Role aRole3 = aRole().name("role_3").privileges(null).deleted(false).build();
    entityManager.persist(aRole3);
    entityManager.flush();

    Role role = roleRepository.findRoleByRoleName("role_1");
    assertThat(role, hasSameStateAsRole(aRole));
  }

  @Test(expected = NoResultException.class)
  public void testFindRoleByNameThatIsDeleted_shouldNotReturnResult() {
    Role aRole = aRole().name("role_1").privileges(null).deleted(true).build();
    entityManager.persist(aRole);
    entityManager.flush();

    Role aRole2 = aRole().name("role_2").privileges(null).deleted(false).build();
    entityManager.persist(aRole2);
    entityManager.flush();

    Role aRole3 = aRole().name("role_3").privileges(null).deleted(false).build();
    entityManager.persist(aRole3);
    entityManager.flush();

    Role role = roleRepository.findRoleByRoleName("role_1");
    assertThat(role, hasSameStateAsRole(aRole));
  }

  @Test(expected = NoResultException.class)
  public void testFindRoleByNameThatIsNotValid_shouldNotReturnResult() {
    Role aRole = aRole().name("role_1").privileges(null).deleted(true).build();
    entityManager.persist(aRole);
    entityManager.flush();

    roleRepository.findRoleByRoleName("Different");
  }

  @Test
  public void testGetAllRoles_shouldRetreiveAllRolesPersistedThatAreNotDeleted() {
    List<Role> allRoles = roleRepository.getAllRoles();
    int initialCount = allRoles.size(); // existing Roles

    Role aRole1 = aRole().name("role_1").privileges(null).deleted(false).build();
    entityManager.persist(aRole1);
    entityManager.flush();

    Role aRole2 = aRole().name("role_2").privileges(null).deleted(false).build();
    entityManager.persist(aRole2);
    entityManager.flush();

    Role aRole3 = aRole().name("role_3").privileges(null).deleted(false).build();
    entityManager.persist(aRole3);
    entityManager.flush();

    Role aRole4 = aRole().name("role_4").privileges(null).deleted(true).build();
    entityManager.persist(aRole4);
    entityManager.flush();

    allRoles = roleRepository.getAllRoles();
    assertThat(allRoles.size(), is(3 + initialCount));
    assertThat(allRoles, hasItems(hasSameStateAsRole(aRole1),
            hasSameStateAsRole(aRole2),
            hasSameStateAsRole(aRole3)));

    assertThat(allRoles, not(hasItem(aRole4)));
  }

  @Test
  public void testGetAllPrivileges_shouldRetreiveAllPrivilegesPersistedThatAreNotDeleted() {
    List<Privilege> allPrivileges = roleRepository.getAllPrivileges();
    int initialCount = allPrivileges.size(); // existing Roles

    Privilege aPrivilege = aPrivilege().name("priv_1").deleted(false).build();
    entityManager.persist(aPrivilege);
    entityManager.flush();

    Privilege aPrivilege2 = aPrivilege().name("priv_2").deleted(false).build();
    entityManager.persist(aPrivilege2);
    entityManager.flush();

    Privilege aPrivilege3 = aPrivilege().name("priv_3").deleted(false).build();
    entityManager.persist(aPrivilege3);
    entityManager.flush();

    Privilege aPrivilege4 = aPrivilege().name("priv_4").deleted(true).build();
    entityManager.persist(aPrivilege4);
    entityManager.flush();

    allPrivileges = roleRepository.getAllPrivileges();
    assertThat(allPrivileges.size(), is(3 + initialCount));
    assertThat(allPrivileges, hasItems(hasSameStateAsPrivilege(aPrivilege),
            hasSameStateAsPrivilege(aPrivilege2),
            hasSameStateAsPrivilege(aPrivilege3)));

    assertThat(allPrivileges, not(hasItem(aPrivilege4)));
  }
}

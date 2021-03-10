package org.churchsource.churchauth.user.role;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.churchsource.churchauth.user.role.Role.aRole;
import static org.churchsource.churchauth.user.role.Privilege.aPrivilege;
import static org.churchsource.churchauth.user.role.helpers.RoleMatcher.hasSameStateAsRole;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@SpringBootTest
public class RoleEntityTest {
    @Test
    public void testTwoRoleWithSameIds_shouldBeEqual() {
        Role aRole = aRole().id(1L).build();
        Role aRole2 = aRole().id(1L).build();
        assertThat(aRole, hasSameStateAsRole(aRole2));
    }

    @Test
    public void testTwoRoleWithDifferentIds_shouldNotBeEqual() {
        Role aRole = aRole().id(1L).build();
        Role aRole2 = aRole().id(2L).build();
        assertThat(aRole, not(aRole2));
    }

    @Test
    public void testTwoRoleWithSameIdsButUsingNoArgsConstructor_shouldBeEqual() {
        Role aRole = new Role();
        aRole.setId(1L);
        aRole.setDeleted(false);
        Role aRole2 = aRole().id(1L).deleted(false).build();
        assertThat(aRole, hasSameStateAsRole(aRole2));
    }

    @Test
    public void testIsEqualsForRoleWithAllPropertiesSetTheSame_shouldBeEqual() {
        List<Privilege> privileges = new ArrayList<Privilege>();
        privileges.add(aPrivilege().name("priv1").build());
        privileges.add(aPrivilege().name("priv2").build());

        List<Privilege> privileges2 = new ArrayList<Privilege>();
        privileges2.add(aPrivilege().name("priv1").build());
        privileges2.add(aPrivilege().name("priv2").build());

        Role aRole = aRole().id(1L).name("Role").privileges(privileges).build();
        Role aRole2 = aRole().id(1L).name("Role").privileges(privileges2).build();
        assertThat(aRole, hasSameStateAsRole(aRole2));
    }

    @Test
    public void testIsEqualsForRoleWithAllPropertiesSetTheSameUsingSetterMethods_shouldBeEqual() {
        List<Privilege> privileges = new ArrayList<Privilege>();
        privileges.add(aPrivilege().name("priv1").build());
        privileges.add(aPrivilege().name("priv2").build());
        Role aRole = aRole()
                .id(1L)
                .name("Role")
                .privileges(privileges)
                .deleted(false)
                .build();
        Role aRole2 = aRole().build();
        aRole2.setId(1L);
        aRole2.setName("Role");
        aRole2.setPrivileges(privileges);
        aRole2.setDeleted(false);
        assertThat(aRole, hasSameStateAsRole(aRole2));
    }

    @Test
    public void testGetterMethodsForAllProperties_shouldAllBeValid() {
        List<Privilege> privileges = new ArrayList<Privilege>();
        privileges.add(aPrivilege().name("priv1").build());
        privileges.add(aPrivilege().name("priv2").build());
        Role aRole = aRole()
                .id(1L)
                .name("Role")
                .privileges(privileges)
                .deleted(false)
                .build();
        assertThat(aRole.getId(), is(1L));
        assertThat(aRole.getName(), is("Role"));
        assertThat(aRole.getDeleted(), is(false));
        assertThat(aRole.getPrivileges(), is(privileges));
    }

    @Test
    public void testIsEqualsForRoleWithDifferenId_shouldNotBeEqual() {
        List<Privilege> privileges = new ArrayList<Privilege>();
        privileges.add(aPrivilege().name("priv1").build());
        privileges.add(aPrivilege().name("priv2").build());
        Role aRole = aRole()
                .id(1L)
                .name("Role")
                .privileges(privileges)
                .deleted(false)
                .build();

        Role aRole2 = aRole()
                .id(2L)
                .name("Role")
                .privileges(privileges)
                .deleted(false)
                .build();
        assertThat(aRole, not(hasSameStateAsRole(aRole2)));
    }

    @Test
    public void testIsEqualsForRoleWithDifferentName_shouldNotBeEqual() {
        List<Privilege> privileges = new ArrayList<Privilege>();
        privileges.add(aPrivilege().name("priv1").build());
        privileges.add(aPrivilege().name("priv2").build());
        Role aRole = aRole()
                .id(1L)
                .name("Role")
                .privileges(privileges)
                .deleted(false)
                .build();

        Role aRole2 = aRole()
                .id(1L)
                .name("aDifferentRoleName")
                .privileges(privileges)
                .deleted(false)
                .build();
        assertThat(aRole, not(hasSameStateAsRole(aRole2)));
    }

    @Test
    public void testIsEqualsForRoleWithDifferentDeleted_shouldNotBeEqual() {
        List<Privilege> privileges = new ArrayList<Privilege>();
        privileges.add(aPrivilege().name("priv1").build());
        privileges.add(aPrivilege().name("priv2").build());
        Role aRole = aRole()
                .id(1L)
                .name("Role")
                .privileges(privileges)
                .deleted(false)
                .build();

        Role aRole2 = aRole()
                .id(1L)
                .name("Role")
                .privileges(privileges)
                .deleted(true)
                .build();
        assertThat(aRole, not(hasSameStateAsRole(aRole2)));
    }

    @Test
    public void testIsEqualsForRoleWithDifferentPrivilieges_shouldNotBeEqual() {
        List<Privilege> privileges = new ArrayList<Privilege>();
        privileges.add(aPrivilege().name("priv1").build());
        privileges.add(aPrivilege().name("priv2").build());

        List<Privilege> privileges2 = new ArrayList<Privilege>();
        privileges.add(aPrivilege().name("priv3").build());
        privileges.add(aPrivilege().name("priv4").build());
        Role aRole = aRole()
                .id(1L)
                .name("Role")
                .privileges(privileges)
                .deleted(false)
                .build();

        Role aRole2 = aRole()
                .id(1L)
                .name("Role")
                .privileges(privileges2)
                .deleted(false)
                .build();
        assertThat(aRole, not(hasSameStateAsRole(aRole2)));
    }

    @Test
    public void testEqualsWithNullId_shouldNotBeEqual() {
        Role aRole = aRole().id(1L).name("Role").deleted(false).build();
        Role aRole2 = aRole().id(null).name("Role").deleted(false).build();
        assertThat(aRole, not(hasSameStateAsRole(aRole2)));
    }

    @Test
    public void testEquals_shouldBeEqual() {
        List<Privilege> privileges = new ArrayList<Privilege>();
        privileges.add(aPrivilege().name("priv1").build());
        privileges.add(aPrivilege().name("priv2").build());

        Role aRole = aRole().id(1L).name("Role").deleted(false).privileges(privileges).build();
        Role aRole2 = aRole().id(1L).name("Role").deleted(false).privileges(privileges).build();
        assertThat(aRole.equals(aRole2), is(true));
    }

    @Test
    public void testEquals_shouldNotBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        roles.add(aRole().name("role_1").build());
        Role aRole = aRole().id(1L).name("Role").deleted(false).build();
        Role aRole2 = aRole().id(1L).name("Role2").deleted(true).build();
        assertThat(aRole.equals(aRole2), is(false));
    }

    @Test
    public void testEqualsWithNull_shouldNotBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        roles.add(aRole().name("role_1").build());
        Role aRole = aRole().id(1L).name("Role").deleted(false).build();
        Role aRole2 = null;
        assertThat(aRole.equals(aRole2), is(false));
    }

    @Test
    public void testEqualsWithDifferentObjectType_shouldNotBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        roles.add(aRole().name("role_1").build());
        Role aRole = aRole().id(1L).name("Role").deleted(false).build();
        Object testObject = new Object();
        assertThat(aRole.equals(testObject), is(false));
    }

    @Test
    public void testHashCodeForSameReference_shouldBeEqual() {
        Role aRole = aRole().id(1L).name("Role").deleted(false).build();
        Role aRole2 = aRole;
        assertThat(aRole, hasSameStateAsRole(aRole2));
        int hashCode1 = aRole.hashCode();
        int hashCode2 = aRole2.hashCode();

        assertThat(hashCode1, is (hashCode2));
    }

    @Test
    public void testHashCodeNewObjectsEquals_shouldBeEqual() {
        List<Privilege> privileges = new ArrayList<Privilege>();
        privileges.add(aPrivilege().name("priv1").build());
        privileges.add(aPrivilege().name("priv2").build());

        Role aRole = aRole().id(1L).name("Role").privileges(privileges).deleted(false).build();
        Role aRole2 = aRole().id(1L).name("Role").privileges(privileges).deleted(false).build();
        assertThat(aRole, hasSameStateAsRole(aRole2));

        int hashCode1 = aRole.hashCode();
        int hashCode2 = aRole2.hashCode();

        assertThat(hashCode1, is (hashCode2));
    }

    @Test
    public void testHashCodeNewObjectsWithDifferentValues_shouldNotBeEqual() {
        List<Privilege> privileges = new ArrayList<Privilege>();
        privileges.add(aPrivilege().name("priv1").build());
        privileges.add(aPrivilege().name("priv2").build());

        List<Privilege> privileges2 = new ArrayList<Privilege>();
        privileges.add(aPrivilege().name("priv3").build());
        privileges.add(aPrivilege().name("priv4").build());
        Role aRole = aRole().id(1L).name("Role").privileges(privileges).deleted(false).build();
        Role aRole2 = aRole().id(2L).name("Role2").privileges(privileges2).deleted(false).build();

        int hashCode1 = aRole.hashCode();
        int hashCode2 = aRole2.hashCode();
        assertThat(hashCode1, not (hashCode2));
    }

    @Test
    public void testToString_shouldContainAllValues() {

        Role aRole = aRole().id(999L).name("Role").deleted(false).build();
        String str = aRole.toString();
        assertThat(str, containsString("id=999"));
        assertThat(str, containsString("name=Role"));
        assertThat(str, containsString("deleted=false"));
    }
}
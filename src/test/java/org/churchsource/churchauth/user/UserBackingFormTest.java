package org.churchsource.churchauth.user;

import org.churchsource.churchauth.user.role.Role;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.churchsource.churchauth.user.role.Role.aRole;
import static org.churchsource.churchauth.user.UserBackingForm.aUserBackingForm;
import static org.churchsource.churchauth.user.helpers.UserBackingFormMatcher.hasSameStateAsUserBackingForm;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@SpringBootTest
public class UserBackingFormTest {
    @Test
    public void testTwoUserBackingFormsWithSameIds_shouldBeEqual() {
        UserBackingForm aUserBackingForm = aUserBackingForm().id(1L).build();
        UserBackingForm aUserBackingForm2 = aUserBackingForm().id(1L).build();
        assertThat(aUserBackingForm, hasSameStateAsUserBackingForm(aUserBackingForm2));
    }

    @Test
    public void testTwoUserBackingFormsWithDifferentIds_shouldNotBeEqual() {
        UserBackingForm aUserBackingForm = aUserBackingForm().id(1L).build();
        UserBackingForm aUserBackingForm2 = aUserBackingForm().id(2L).build();
        assertThat(aUserBackingForm, not(aUserBackingForm2));
    }

    @Test
    public void testTwoUserBackingFormsWithSameIdsButUsingNoArgsConstructor_shouldBeEqual() {
        UserBackingForm aUserBackingForm = new UserBackingForm();
        aUserBackingForm.setId(1L);
        UserBackingForm aUserBackingForm2 = aUserBackingForm().id(1L).build();
        assertThat(aUserBackingForm, hasSameStateAsUserBackingForm(aUserBackingForm2));
    }

    @Test
    public void testIsEqualsForUserBackingFormsWithAllPropertiesSetTheSame_shouldBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        Role aRole = aRole().name("role_1").deleted(false).build();
        roles.add(aRole);
        UserBackingForm aUserBackingForm = aUserBackingForm().id(1L).username("Joe").forcePasswordChange(true).email("joe@bar.com").password("Bar").isEnabled(true).roles(roles).build();
        UserBackingForm aUserBackingForm2 = aUserBackingForm().id(1L).username("Joe").forcePasswordChange(true).email("joe@bar.com").password("Bar").isEnabled(true).roles(roles).build();
        assertThat(aUserBackingForm.equals(aUserBackingForm2), is (true));
    }

    @Test
    public void testIsEqualsForUserBackingFormsWithAllPropertiesSetTheSameUsingSetterMethods_shouldBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        Role aRole = aRole().name("role_1").deleted(false).build();
        roles.add(aRole);
        UserBackingForm aUserBackingForm = aUserBackingForm().id(1L).username("Joe").email("joe@bar.com").forcePasswordChange(true).password("Bar").isEnabled(true).roles(roles).build();
        UserBackingForm aUserBackingForm2 = aUserBackingForm().build();
        aUserBackingForm2.setId(1L);
        aUserBackingForm2.setUsername("Joe");
        aUserBackingForm2.setEmail("joe@bar.com");
        aUserBackingForm2.setPassword("Bar");
        aUserBackingForm2.setIsEnabled(true);
        aUserBackingForm2.setForcePasswordChange(true);
        aUserBackingForm2.setRoles(roles);
        assertThat(aUserBackingForm, hasSameStateAsUserBackingForm(aUserBackingForm2));
    }

    @Test
    public void testGetterMethodsForAllProperties_shouldAllBeValid() {
        List<Role> roles = new ArrayList<Role>();
        Role aRole = aRole().name("role_1").deleted(false).build();
        roles.add(aRole);
        UserBackingForm aUserBackingForm = aUserBackingForm().id(1L).username("Joe").email("joe@bar.com").forcePasswordChange(true).password("Bar").isEnabled(true).roles(roles).build();
        assertThat(aUserBackingForm.getId(), is(1L));
        assertThat(aUserBackingForm.getUsername(), is("Joe"));
        assertThat(aUserBackingForm.getEmail(), is("joe@bar.com"));
        assertThat(aUserBackingForm.getPassword(), is("Bar"));
        assertThat(aUserBackingForm.getForcePasswordChange(), is(true));
        assertThat(aUserBackingForm.getIsEnabled(), is(true));
        assertThat(aUserBackingForm.getRoles(), is(roles));
    }

    @Test
    public void testIsEqualsForUserBackingFormsWithDifferentUserName_shouldNotBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        Role aRole = aRole().name("role_1").deleted(false).build();
        roles.add(aRole);
        UserBackingForm aUserBackingForm = aUserBackingForm().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).roles(roles).build();
        UserBackingForm aUserBackingForm2 = aUserBackingForm().id(1L).username("DifferentJoe").email("joe@bar.com").password("Bar").isEnabled(true).roles(roles).build();
        assertThat(aUserBackingForm, not(hasSameStateAsUserBackingForm(aUserBackingForm2)));
    }

    @Test
    public void testIsEqualsForUserBackingFormsWithDifferentEmail_shouldNotBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        Role aRole = aRole().name("role_1").deleted(false).build();
        roles.add(aRole);
        UserBackingForm aUserBackingForm = aUserBackingForm().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).roles(roles).build();
        UserBackingForm aUserBackingForm2 = aUserBackingForm().id(1L).username("Joe").email("DifferentJoe@bar.com").password("Bar").isEnabled(true).roles(roles).build();
        assertThat(aUserBackingForm, not(hasSameStateAsUserBackingForm(aUserBackingForm2)));
    }

    @Test
    public void testIsEqualsForUserBackingFormsWithDifferentPassword_shouldNotBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        Role aRole = aRole().name("role_1").deleted(false).build();
        roles.add(aRole);
        UserBackingForm aUserBackingForm = aUserBackingForm().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).roles(roles).build();
        UserBackingForm aUserBackingForm2 = aUserBackingForm().id(1L).username("Joe").email("joe@bar.com").password("DifferentBar").isEnabled(true).roles(roles).build();
        assertThat(aUserBackingForm, not(hasSameStateAsUserBackingForm(aUserBackingForm2)));
    }

    @Test
    public void testIsEqualsForUserBackingFormsWithDifferentEnabled_shouldNotBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        Role aRole = aRole().name("role_1").deleted(false).build();
        roles.add(aRole);
        UserBackingForm aUserBackingForm = aUserBackingForm().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).roles(roles).build();
        UserBackingForm aUserBackingForm2 = aUserBackingForm().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(false).roles(roles).build();
        assertThat(aUserBackingForm, not(hasSameStateAsUserBackingForm(aUserBackingForm2)));
    }

    @Test
    public void testIsEqualsForUserBackingFormsWithDifferentForcePasswordChange_shouldNotBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        Role aRole = aRole().name("role_1").deleted(false).build();
        roles.add(aRole);
        UserBackingForm aUserBackingForm = aUserBackingForm().id(1L).username("Joe").email("joe@bar.com").forcePasswordChange(true).password("Bar").isEnabled(true).roles(roles).build();
        UserBackingForm aUserBackingForm2 = aUserBackingForm().id(1L).username("Joe").email("joe@bar.com").forcePasswordChange(false).password("Bar").isEnabled(true).roles(roles).build();
        assertThat(aUserBackingForm, not(hasSameStateAsUserBackingForm(aUserBackingForm2)));
    }

    @Test
    public void testIsEqualsForUserBackingFormsWithDifferentRoles_shouldNotBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        Role aRole = aRole().name("role_1").deleted(false).build();
        roles.add(aRole);

        List<Role> roles2 = new ArrayList<Role>();
        Role aRole2 = aRole().name("role_2").deleted(false).build();
        roles2.add(aRole2);
        UserBackingForm aUserBackingForm = aUserBackingForm().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).roles(roles).build();
        UserBackingForm aUserBackingForm2 = aUserBackingForm().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).roles(roles2).build();
        assertThat(aUserBackingForm, not(hasSameStateAsUserBackingForm(aUserBackingForm2)));
    }

    @Test
    public void testEqualsWithNullId_shouldNotBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        Role aRole = aRole().name("role_1").deleted(false).build();
        roles.add(aRole);
        UserBackingForm aUserBackingForm = aUserBackingForm().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).roles(roles).build();
        UserBackingForm aUserBackingForm2 = aUserBackingForm().id(null).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).roles(roles).build();
        assertThat(aUserBackingForm, not(hasSameStateAsUserBackingForm(aUserBackingForm2)));
    }

    @Test
    public void testHashCodeForSameReference_shouldBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        Role aRole = aRole().name("role_1").deleted(false).build();
        roles.add(aRole);
        UserBackingForm aUserBackingForm = aUserBackingForm().id(1L).username("Joe").email("joe@bar.com").forcePasswordChange(true).password("Bar").isEnabled(true).roles(roles).build();
        UserBackingForm aUserBackingForm2 = aUserBackingForm;
        assertThat(aUserBackingForm, hasSameStateAsUserBackingForm(aUserBackingForm2));
        int hashCode1 = aUserBackingForm.hashCode();
        int hashCode2 = aUserBackingForm2.hashCode();

        assertThat(hashCode1, is (hashCode2));
    }

    @Test
    public void testHashCodeNewObjectsEquals_shouldBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        Role aRole = aRole().name("role_1").deleted(false).build();
        roles.add(aRole);
        UserBackingForm aUserBackingForm = aUserBackingForm().id(1L).username("Joe").forcePasswordChange(true).email("joe@bar.com").password("Bar").isEnabled(true).roles(roles).build();
        UserBackingForm aUserBackingForm2 = aUserBackingForm().id(1L).username("Joe").forcePasswordChange(true).email("joe@bar.com").password("Bar").isEnabled(true).roles(roles).build();
        assertThat(aUserBackingForm, hasSameStateAsUserBackingForm(aUserBackingForm2));

        int hashCode1 = aUserBackingForm.hashCode();
        int hashCode2 = aUserBackingForm2.hashCode();

        assertThat(hashCode1, is (hashCode2));
    }

    @Test
    public void testHashCodeNewObjectsWithDifferentValues_shouldNotBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        Role aRole = aRole().name("role_1").deleted(false).build();
        roles.add(aRole);
        UserBackingForm aUserBackingForm = aUserBackingForm().id(1L).username("Joe").forcePasswordChange(true).email("joe@bar.com").password("Bar").isEnabled(true).roles(roles).build();
        UserBackingForm aUserBackingForm2 = aUserBackingForm().id(1L).username("Joe2").forcePasswordChange(true).email("2@bar.com").password("Bar2").isEnabled(true).roles(roles).build();

        int hashCode1 = aUserBackingForm.hashCode();
        int hashCode2 = aUserBackingForm2.hashCode();
        assertThat(hashCode1, not (hashCode2));
    }

    @Test
    public void testToString_shouldContainAllValues() {
        List<Role> roles = new ArrayList<Role>();
        Role aRole = aRole().name("role_1").deleted(false).build();
        roles.add(aRole);
        UserBackingForm aUserBackingForm = aUserBackingForm().id(999L).username("Joe").forcePasswordChange(true).email("joe@bar.com").password("Bar").isEnabled(true).roles(roles).build();
        String str = aUserBackingForm.toString();
        assertThat(str, containsString("id=999"));
        assertThat(str, containsString("username=Joe"));
        assertThat(str, containsString("email=joe@bar.com"));
        assertThat(str, containsString("password=Bar"));
        assertThat(str, containsString("isEnabled=true"));
        assertThat(str, containsString("forcePasswordChange=true"));
    }
}
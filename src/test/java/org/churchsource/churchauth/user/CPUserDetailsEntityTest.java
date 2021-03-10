package org.churchsource.churchauth.user;

import org.churchsource.churchauth.user.role.Privilege;
import org.churchsource.churchauth.user.role.Role;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

import static org.churchsource.churchauth.user.CPUserDetails.aCPUserDetails;
import static org.churchsource.churchauth.user.role.Role.aRole;
import static org.churchsource.churchauth.user.role.Privilege.aPrivilege;
import static org.churchsource.churchauth.user.helpers.CPUserDetailsMatcher.hasSameStateAsCPUserDetails;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@SpringBootTest
public class CPUserDetailsEntityTest {
    @Test
    public void testTwoCPUserDetailsWithSameIds_shouldBeEqual() {
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails().id(1L).build();
        assertThat(aCPUserDetails, hasSameStateAsCPUserDetails(aCPUserDetails2));
    }

    @Test
    public void testTwoCPUserDetailsWithDifferentIds_shouldNotBeEqual() {
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails().id(2L).build();
        assertThat(aCPUserDetails, not(aCPUserDetails2));
    }

    @Test
    public void testTwoCPUserDetailsWithSameIdsButUsingNoArgsConstructor_shouldBeEqual() {
        CPUserDetails aCPUserDetails = new CPUserDetails();
        aCPUserDetails.setId(1L);
        aCPUserDetails.setDeleted(false);
        CPUserDetails aCPUserDetails2 = aCPUserDetails().id(1L).deleted(false).build();
        assertThat(aCPUserDetails, hasSameStateAsCPUserDetails(aCPUserDetails2));
    }

    @Test
    public void testIsEqualsForCPUserDetailsWithAllPropertiesSetTheSame_shouldBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        roles.add(aRole().name("role_1").build());
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).email("joe@bar.com").username("Joe").password("Bar").isEnabled(true).forcePasswordChange(true).deleted(false).roles(roles).build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails().id(1L).email("joe@bar.com").username("Joe").password("Bar").isEnabled(true).forcePasswordChange(true).deleted(false).roles(roles).build();
        assertThat(aCPUserDetails.equals(aCPUserDetails2), is (true));
    }

    @Test
    public void testIsEqualsForCPUserDetailsWithAllPropertiesSetTheSameUsingSetterMethods_shouldBeEqual() {
        CPUserDetails aCPUserDetails = aCPUserDetails()
                .id(1L)
                .email("joe@bar.com")
                .username("Joe")
                .password("Bar")
                .isEnabled(true)
                .forcePasswordChange(true)
                .deleted(false)
                .build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails().build();
        aCPUserDetails2.setId(1L);
        aCPUserDetails2.setUsername("Joe");
        aCPUserDetails2.setEmail("joe@bar.com");
        aCPUserDetails2.setPassword("Bar");
        aCPUserDetails2.setEnabled(true);
        aCPUserDetails2.setForcePasswordChange(true);
        aCPUserDetails2.setDeleted(false);
        assertThat(aCPUserDetails, hasSameStateAsCPUserDetails(aCPUserDetails2));
    }

    @Test
    public void testGetterMethodsForAllProperties_shouldAllBeValid() {
        CPUserDetails aCPUserDetails = aCPUserDetails()
                .id(1L)
                .username("Joe")
                .email("joe@bar.com")
                .password("Bar")
                .isEnabled(true)
                .forcePasswordChange(true)
                .deleted(false)
                .build();
        assertThat(aCPUserDetails.getId(), is(1L));
        assertThat(aCPUserDetails.getUsername(), is("Joe"));
        assertThat(aCPUserDetails.getEmail(), is("joe@bar.com"));
        assertThat(aCPUserDetails.getPassword(), is("Bar"));
        assertThat(aCPUserDetails.isEnabled(), is(true));
        assertThat(aCPUserDetails.isForcePasswordChange(), is(true));
        assertThat(aCPUserDetails.getDeleted(), is(false));
    }

    @Test
    public void testIsEqualsForCPUserDetailsWithDifferentUserName_shouldNotBeEqual() {
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails().id(1L).username("JoeSoap").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).build();
        assertThat(aCPUserDetails, not(hasSameStateAsCPUserDetails(aCPUserDetails2)));
    }

    @Test
    public void testIsEqualsForCPUserDetailsWithDifferentEmail_shouldNotBeEqual() {
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails().id(1L).username("Joe").email("Differentjoe@bar.com").password("Bar").isEnabled(true).deleted(false).build();
        assertThat(aCPUserDetails, not(hasSameStateAsCPUserDetails(aCPUserDetails2)));
    }

    @Test
    public void testIsEqualsForCPUserDetailsWithDifferentPassword_shouldNotBeEqual() {
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Barber").isEnabled(true).deleted(false).build();
        assertThat(aCPUserDetails, not(hasSameStateAsCPUserDetails(aCPUserDetails2)));
    }

    @Test
    public void testIsEqualsForCPUserDetailsWithDifferentEnabled_shouldNotBeEqual() {
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(false).deleted(false).build();
        assertThat(aCPUserDetails, not(hasSameStateAsCPUserDetails(aCPUserDetails2)));
    }

    @Test
    public void testIsEqualsForCPUserDetailsWithDifferentDeleted_shouldNotBeEqual() {
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(true).build();
        assertThat(aCPUserDetails, not(hasSameStateAsCPUserDetails(aCPUserDetails2)));
    }

    @Test
    public void testIsEqualsForCPUserDetailsWithDifferentForcePasswordChange_shouldNotBeEqual() {
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).forcePasswordChange(true).deleted(false).build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).forcePasswordChange(false).deleted(false).build();
        assertThat(aCPUserDetails, not(hasSameStateAsCPUserDetails(aCPUserDetails2)));
    }

    @Test
    public void testIsEqualsForCPUserDetailsWithDifferentRoles_shouldNotBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        roles.add(aRole().name("role_1").build());
        List<Role> roles2 = new ArrayList<Role>();
        roles.add(aRole().name("role_2").build());
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles).build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles2).build();
        assertThat(aCPUserDetails, not(hasSameStateAsCPUserDetails(aCPUserDetails2)));
    }

    @Test
    public void testEqualsWithNullId_shouldNotBeEqual() {
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails().id(null).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).build();
        assertThat(aCPUserDetails, not(hasSameStateAsCPUserDetails(aCPUserDetails2)));
    }

    @Test
    public void testEquals_shouldBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        roles.add(aRole().name("role_1").build());
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles).build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles).build();
        assertThat(aCPUserDetails.equals(aCPUserDetails2), is(true));
    }

    @Test
    public void testEquals_shouldNotBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        roles.add(aRole().name("role_1").build());
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles).build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails().id(1L).username("JoeSoap").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles).build();
        assertThat(aCPUserDetails.equals(aCPUserDetails2), is(false));
    }

    @Test
    public void testEqualsWithNull_shouldNotBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        roles.add(aRole().name("role_1").build());
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles).build();
        CPUserDetails aCPUserDetails2 = null;
        assertThat(aCPUserDetails.equals(aCPUserDetails2), is(false));
    }

    @Test
    public void testEqualsWithDifferentObjectType_shouldNotBeEqual() {
        List<Role> roles = new ArrayList<Role>();
        roles.add(aRole().name("role_1").build());
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles).build();
        Object testObject = new Object();
        assertThat(aCPUserDetails.equals(testObject), is(false));
    }

    @Test
    public void testHashCodeForSameReference_shouldBeEqual() {
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails;
        assertThat(aCPUserDetails, hasSameStateAsCPUserDetails(aCPUserDetails2));
        int hashCode1 = aCPUserDetails.hashCode();
        int hashCode2 = aCPUserDetails2.hashCode();

        assertThat(hashCode1, is (hashCode2));
    }

    @Test
    public void testHashCodeNewObjectsEquals_shouldBeEqual() {
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").forcePasswordChange(true).password("Bar").isEnabled(true).deleted(false).build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").forcePasswordChange(true).password("Bar").isEnabled(true).deleted(false).build();
        assertThat(aCPUserDetails, hasSameStateAsCPUserDetails(aCPUserDetails2));

        int hashCode1 = aCPUserDetails.hashCode();
        int hashCode2 = aCPUserDetails2.hashCode();

        assertThat(hashCode1, is (hashCode2));
    }

    @Test
    public void testHashCodeNewObjectsWithDifferentValues_shouldNotBeEqual() {
        CPUserDetails aCPUserDetails = aCPUserDetails().id(1L).username("Joe").email("joe@bar.com").password("Bar").forcePasswordChange(true).isEnabled(true).deleted(false).build();
        CPUserDetails aCPUserDetails2 = aCPUserDetails().id(2L).username("JoeSoap").email("joe@bar.com").password("Bar").forcePasswordChange(true).isEnabled(true).deleted(true).build();

        int hashCode1 = aCPUserDetails.hashCode();
        int hashCode2 = aCPUserDetails2.hashCode();
        assertThat(hashCode1, not (hashCode2));
    }

    @Test
    public void testToString_shouldContainAllValues() {

        CPUserDetails aCPUserDetails = aCPUserDetails().id(999L).username("Joe").email("joe@bar.com").password("Bar").forcePasswordChange(true).isEnabled(true).deleted(false).build();
        String str = aCPUserDetails.toString();
        assertThat(str, containsString("id=999"));
        assertThat(str, containsString("username=Joe"));
        assertThat(str, containsString("email=joe@bar.com"));
        assertThat(str, containsString("password=Bar"));
        assertThat(str, containsString("enabled=true"));
        assertThat(str, containsString("forcePasswordChange=true"));
        assertThat(str, containsString("deleted=false"));
    }

    @Test
    public void testGetAuthoritiesWithNullRoles_shouldReturnNull() {
        CPUserDetails aCPUserDetails = aCPUserDetails().id(999L).username("Joe").password("Bar").isEnabled(true).deleted(false).roles(null).build();
        Collection<? extends GrantedAuthority> authorities = aCPUserDetails.getAuthorities();
        assertThat(authorities, (is((Collection)null)));
    }

    @Test
    public void testGetAuthoritiesWithEmptyRoles_shouldReturnEmptyCollection() {
        List<Role> roles = new ArrayList<Role>();
        CPUserDetails aCPUserDetails = aCPUserDetails().id(999L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles).build();
        Collection<? extends GrantedAuthority> authorities = aCPUserDetails.getAuthorities();
        assertThat(authorities, (is(not((Collection)null))));
        assertThat(authorities.size(), is(0));
    }

    @Test
    public void testGetAuthoritiesWithRolesButNoPrivileges_shouldReturnEmptyCollection() {
        List<Role> roles = new ArrayList<Role>();
        roles.add(aRole().name("role_1").build());
        CPUserDetails aCPUserDetails = aCPUserDetails().id(999L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles).build();
        Collection<? extends GrantedAuthority> authorities = aCPUserDetails.getAuthorities();
        assertThat(authorities, (is(not((Collection)null))));
        assertThat(authorities.size(), is(0));
    }

    @Test
    public void testGetAuthoritiesWithRolesTwoPrivileges_shouldReturnCollectionWithTwoGrantedAuthorities() {
        List<Role> roles = new ArrayList<Role>();
        List<Privilege> privileges = new ArrayList<Privilege>();
        privileges.add(aPrivilege().name("priv1").build());
        privileges.add(aPrivilege().name("priv2").build());
        roles.add(aRole().name("role_1").privileges(privileges).build());

        CPUserDetails aCPUserDetails = aCPUserDetails().id(999L).username("Joe").email("joe@bar.com").password("Bar").isEnabled(true).deleted(false).roles(roles).build();


        Collection<? extends GrantedAuthority> authorities = aCPUserDetails.getAuthorities();
        assertThat(authorities, (is(not((Collection)null))));
        assertThat(authorities.size(), is(2));
        assertThat(authorities, containsInAnyOrder(new SimpleGrantedAuthority("priv1"), new SimpleGrantedAuthority("priv2")));
    }
}
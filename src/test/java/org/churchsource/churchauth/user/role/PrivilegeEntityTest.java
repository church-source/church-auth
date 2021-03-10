package org.churchsource.churchauth.user.role;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.churchsource.churchauth.user.role.Privilege.aPrivilege;
import static org.churchsource.churchauth.user.role.helpers.PrivilegeMatcher.hasSameStateAsPrivilege;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@SpringBootTest
public class PrivilegeEntityTest {
    @Test
    public void testTwoPrivilegeWithSameIds_shouldBeEqual() {
        Privilege aPrivilege = aPrivilege().id(1L).build();
        Privilege aPrivilege2 = aPrivilege().id(1L).build();
        assertThat(aPrivilege, hasSameStateAsPrivilege(aPrivilege2));
    }

    @Test
    public void testTwoPrivilegeWithDifferentIds_shouldNotBeEqual() {
        Privilege aPrivilege = aPrivilege().id(1L).build();
        Privilege aPrivilege2 = aPrivilege().id(2L).build();
        assertThat(aPrivilege, not(aPrivilege2));
    }

    @Test
    public void testTwoPrivilegeWithSameIdsButUsingNoArgsConstructor_shouldBeEqual() {
        Privilege aPrivilege = new Privilege();
        aPrivilege.setId(1L);
        aPrivilege.setDeleted(false);
        Privilege aPrivilege2 = aPrivilege().id(1L).deleted(false).build();
        assertThat(aPrivilege, hasSameStateAsPrivilege(aPrivilege2));
    }

    @Test
    public void testIsEqualsForPrivilegeWithAllPropertiesSetTheSame_shouldBeEqual() {
        Privilege aPrivilege = aPrivilege().id(1L).name("Privilege").build();
        Privilege aPrivilege2 = aPrivilege().id(1L).name("Privilege").build();
        assertThat(aPrivilege, hasSameStateAsPrivilege(aPrivilege2));
    }

    @Test
    public void testIsEqualsForPrivilegeWithAllPropertiesSetTheSameUsingSetterMethods_shouldBeEqual() {
        Privilege aPrivilege = aPrivilege()
                .id(1L)
                .name("Privilege")
                .deleted(false)
                .build();
        Privilege aPrivilege2 = aPrivilege().build();
        aPrivilege2.setId(1L);
        aPrivilege2.setName("Privilege");
        aPrivilege2.setDeleted(false);
        assertThat(aPrivilege, hasSameStateAsPrivilege(aPrivilege2));
    }

    @Test
    public void testGetterMethodsForAllProperties_shouldAllBeValid() {
        List<Privilege> privileges = new ArrayList<Privilege>();
        privileges.add(aPrivilege().name("priv1").build());
        privileges.add(aPrivilege().name("priv2").build());
        Privilege aPrivilege = aPrivilege()
                .id(1L)
                .name("Privilege")
                .deleted(false)
                .build();
        assertThat(aPrivilege.getId(), is(1L));
        assertThat(aPrivilege.getName(), is("Privilege"));
        assertThat(aPrivilege.getDeleted(), is(false));
    }

    @Test
    public void testIsEqualsForPrivilegeWithDifferentId_shouldNotBeEqual() {

        Privilege aPrivilege = aPrivilege()
                .id(1L)
                .name("Privilege")
                .deleted(false)
                .build();

        Privilege aPrivilege2 = aPrivilege()
                .id(2L)
                .name("Privilege")
                .deleted(false)
                .build();
        assertThat(aPrivilege, not(hasSameStateAsPrivilege(aPrivilege2)));
    }

    @Test
    public void testIsEqualsForPrivilegeWithDifferentName_shouldNotBeEqual() {
        Privilege aPrivilege = aPrivilege()
                .id(1L)
                .name("Privilege")
                .deleted(false)
                .build();

        Privilege aPrivilege2 = aPrivilege()
                .id(1L)
                .name("aDifferentPrivilegeName")
                .deleted(false)
                .build();
        assertThat(aPrivilege, not(hasSameStateAsPrivilege(aPrivilege2)));
    }

    @Test
    public void testIsEqualsForPrivilegeWithDifferentDeleted_shouldNotBeEqual() {
        Privilege aPrivilege = aPrivilege()
                .id(1L)
                .name("Privilege")
                .deleted(false)
                .build();

        Privilege aPrivilege2 = aPrivilege()
                .id(1L)
                .name("Privilege")
                .deleted(true)
                .build();
        assertThat(aPrivilege, not(hasSameStateAsPrivilege(aPrivilege2)));
    }

    @Test
    public void testEqualsWithNullId_shouldNotBeEqual() {
        Privilege aPrivilege = aPrivilege().id(1L).name("Privilege").deleted(false).build();
        Privilege aPrivilege2 = aPrivilege().id(null).name("Privilege").deleted(false).build();
        assertThat(aPrivilege, not(hasSameStateAsPrivilege(aPrivilege2)));
    }

    @Test
    public void testEquals_shouldBeEqual() {
        List<Privilege> privileges = new ArrayList<Privilege>();
        privileges.add(aPrivilege().name("priv1").build());
        privileges.add(aPrivilege().name("priv2").build());

        Privilege aPrivilege = aPrivilege().id(1L).name("Privilege").deleted(false).build();
        Privilege aPrivilege2 = aPrivilege().id(1L).name("Privilege").deleted(false).build();
        assertThat(aPrivilege.equals(aPrivilege2), is(true));
    }

    @Test
    public void testEquals_shouldNotBeEqual() {
        Privilege aPrivilege = aPrivilege().id(1L).name("Privilege").deleted(false).build();
        Privilege aPrivilege2 = aPrivilege().id(1L).name("Privilege2").deleted(true).build();
        assertThat(aPrivilege.equals(aPrivilege2), is(false));
    }

    @Test
    public void testEqualsWithNull_shouldNotBeEqual() {
        Privilege aPrivilege = aPrivilege().id(1L).name("Privilege").deleted(false).build();
        Privilege aPrivilege2 = null;
        assertThat(aPrivilege.equals(aPrivilege2), is(false));
    }

    @Test
    public void testEqualsWithDifferentObjectType_shouldNotBeEqual() {
        Privilege aPrivilege = aPrivilege().id(1L).name("Privilege").deleted(false).build();
        Object testObject = new Object();
        assertThat(aPrivilege.equals(testObject), is(false));
    }

    @Test
    public void testHashCodeForSameReference_shouldBeEqual() {
        Privilege aPrivilege = aPrivilege().id(1L).name("Privilege").deleted(false).build();
        Privilege aPrivilege2 = aPrivilege;
        assertThat(aPrivilege, hasSameStateAsPrivilege(aPrivilege2));
        int hashCode1 = aPrivilege.hashCode();
        int hashCode2 = aPrivilege2.hashCode();

        assertThat(hashCode1, is (hashCode2));
    }

    @Test
    public void testHashCodeNewObjectsEquals_shouldBeEqual() {
        Privilege aPrivilege = aPrivilege().id(1L).name("Privilege").deleted(false).build();
        Privilege aPrivilege2 = aPrivilege().id(1L).name("Privilege").deleted(false).build();
        assertThat(aPrivilege, hasSameStateAsPrivilege(aPrivilege2));

        int hashCode1 = aPrivilege.hashCode();
        int hashCode2 = aPrivilege2.hashCode();

        assertThat(hashCode1, is (hashCode2));
    }

    @Test
    public void testHashCodeNewObjectsWithDifferentValues_shouldNotBeEqual() {
        Privilege aPrivilege = aPrivilege().id(1L).name("Privilege").deleted(false).build();
        Privilege aPrivilege2 = aPrivilege().id(2L).name("Privilege2").deleted(false).build();

        int hashCode1 = aPrivilege.hashCode();
        int hashCode2 = aPrivilege2.hashCode();
        assertThat(hashCode1, not (hashCode2));
    }

    @Test
    public void testToString_shouldContainAllValues() {
        Privilege aPrivilege = aPrivilege().id(999L).name("Privilege").deleted(false).build();
        String str = aPrivilege.toString();
        assertThat(str, containsString("id=999"));
        assertThat(str, containsString("name=Privilege"));
        assertThat(str, containsString("deleted=false"));
    }
}
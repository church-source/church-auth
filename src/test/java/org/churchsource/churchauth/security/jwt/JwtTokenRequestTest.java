package org.churchsource.churchauth.security.jwt;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.churchsource.churchauth.security.jwt.JwtTokenRequest.aJwtTokenRequest;
import static org.churchsource.churchauth.security.jwt.helpers.JwtTokenRequestMatcher.hasSameStateAsJwtTokenRequest;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@SpringBootTest
public class JwtTokenRequestTest {
    @Test
    public void testJwtTokenRequestWithAllFieldsTheSame_shouldBeEqual() {
        JwtTokenRequest jwtTokenRequest = aJwtTokenRequest().username("name").password("password").build();
        JwtTokenRequest jwtTokenRequest2 = aJwtTokenRequest().username("name").password("password").build();
        assertThat(jwtTokenRequest, hasSameStateAsJwtTokenRequest(jwtTokenRequest2));
    }

    @Test
    public void testIsEqualsForJwtTokenRequestsWithAllPropertiesSetTheSameUsingSetterMethods_shouldBeEqual() {
        JwtTokenRequest jwtTokenRequest = aJwtTokenRequest()
                .username("name")
                .password("password")
                .build();
        JwtTokenRequest jwtTokenRequest2 = aJwtTokenRequest().build();
        jwtTokenRequest2.setUsername("name");
        jwtTokenRequest2.setPassword("password");
        assertThat(jwtTokenRequest, hasSameStateAsJwtTokenRequest(jwtTokenRequest2));
    }

    @Test
    public void testGetterMethodsForAllProperties_shouldAllBeValid() {
        JwtTokenRequest jwtTokenRequest = aJwtTokenRequest()
                .username("name")
                .password("password")
                .build();
        assertThat(jwtTokenRequest.getUsername(), is("name"));
        assertThat(jwtTokenRequest.getPassword(), is("password"));
    }

    @Test
    public void testIsEqualsForJwtTokenRequestsWithDifferentName_shouldNotBeEqual() {
        JwtTokenRequest jwtTokenRequest = aJwtTokenRequest().username("name").password("password").build();
        JwtTokenRequest jwtTokenRequest2 = aJwtTokenRequest().username("DifferentName").password("password").build();
        assertThat(jwtTokenRequest, not(hasSameStateAsJwtTokenRequest(jwtTokenRequest2)));
    }

    @Test
    public void testIsEqualsForJwtTokenRequestsWithDifferentPassword_shouldNotBeEqual() {
        JwtTokenRequest jwtTokenRequest = aJwtTokenRequest().username("name").password("password").build();
        JwtTokenRequest jwtTokenRequest2 = aJwtTokenRequest().username("name").password("DifferentPassword").build();
        assertThat(jwtTokenRequest, not(hasSameStateAsJwtTokenRequest(jwtTokenRequest2)));
    }

    @Test
    public void testHashCodeForSameReference_shouldBeEqual() {
        JwtTokenRequest jwtTokenRequest = aJwtTokenRequest().username("name").password("password").build();
        JwtTokenRequest jwtTokenRequest2 = jwtTokenRequest;
        assertThat(jwtTokenRequest, hasSameStateAsJwtTokenRequest(jwtTokenRequest2));
        int hashCode1 = jwtTokenRequest.hashCode();
        int hashCode2 = jwtTokenRequest2.hashCode();

        assertThat(hashCode1, is (hashCode2));
    }

    @Test
    public void testHashCodeNewObjectsEquals_shouldBeEqual() {
        JwtTokenRequest jwtTokenRequest = aJwtTokenRequest().username("name").password("password").build();
        JwtTokenRequest jwtTokenRequest2 = aJwtTokenRequest().username("name").password("password").build();
        assertThat(jwtTokenRequest, hasSameStateAsJwtTokenRequest(jwtTokenRequest2));

        int hashCode1 = jwtTokenRequest.hashCode();
        int hashCode2 = jwtTokenRequest2.hashCode();

        assertThat(hashCode1, is (hashCode2));
    }

    @Test
    public void testHashCodeNewObjectsWithDifferentValues_shouldNotBeEqual() {
        JwtTokenRequest jwtTokenRequest = aJwtTokenRequest().username("name").password("password").build();
        JwtTokenRequest jwtTokenRequest2 = aJwtTokenRequest().username("Dname").password("Dpassword").build();

        int hashCode1 = jwtTokenRequest.hashCode();
        int hashCode2 = jwtTokenRequest2.hashCode();
        assertThat(hashCode1, not (hashCode2));
    }

    @Test
    public void testToString_shouldContainAllValues() {
        JwtTokenRequest jwtTokenRequest = aJwtTokenRequest().username("name").password("password").build();

        String str = jwtTokenRequest.toString();
        assertThat(str, containsString("username=name"));
        assertThat(str, containsString("password=password"));
    }
}
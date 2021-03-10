package org.churchsource.churchauth.security.jwt;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.churchsource.churchauth.security.jwt.JwtTokenResponse.aJwtTokenResponse;
import static org.churchsource.churchauth.security.jwt.helpers.JwtTokenResponseMatcher.hasSameStateAsJwtTokenResponse;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@SpringBootTest
public class JwtTokenResponseTest {
    @Test
    public void testJwtTokenResponseWithAllFieldsTheSame_shouldBeEqual() {
        JwtTokenResponse jwtTokenResponse = aJwtTokenResponse().token("token").build();
        JwtTokenResponse jwtTokenResponse2 = aJwtTokenResponse().token("token").build();
        assertThat(jwtTokenResponse, hasSameStateAsJwtTokenResponse(jwtTokenResponse2));
    }

    @Test
    public void testIsEqualsForJwtTokenResponsesWithAllPropertiesSetTheSameUsingSetterMethods_shouldBeEqual() {
        JwtTokenResponse jwtTokenResponse = aJwtTokenResponse()
                .token("token")
                .build();
        JwtTokenResponse jwtTokenResponse2 = aJwtTokenResponse().build();
        jwtTokenResponse2.setToken("token");
        assertThat(jwtTokenResponse, hasSameStateAsJwtTokenResponse(jwtTokenResponse2));
    }

    @Test
    public void testGetterMethodsForAllProperties_shouldAllBeValid() {
        JwtTokenResponse jwtTokenResponse = aJwtTokenResponse()
                .token("token")
                .build();
        assertThat(jwtTokenResponse.getToken(), is("token"));
    }

    @Test
    public void testIsEqualsForJwtTokenResponsesWithDifferentToken_shouldNotBeEqual() {
        JwtTokenResponse jwtTokenResponse = aJwtTokenResponse().token("token").build();
        JwtTokenResponse jwtTokenResponse2 = aJwtTokenResponse().token("DifferentToken").build();
        assertThat(jwtTokenResponse, not(hasSameStateAsJwtTokenResponse(jwtTokenResponse2)));
    }

    @Test
    public void testHashCodeForSameReference_shouldBeEqual() {
        JwtTokenResponse jwtTokenResponse = aJwtTokenResponse().token("token").build();
        JwtTokenResponse jwtTokenResponse2 = jwtTokenResponse;
        assertThat(jwtTokenResponse, hasSameStateAsJwtTokenResponse(jwtTokenResponse2));
        int hashCode1 = jwtTokenResponse.hashCode();
        int hashCode2 = jwtTokenResponse2.hashCode();

        assertThat(hashCode1, is (hashCode2));
    }

    @Test
    public void testHashCodeNewObjectsEquals_shouldBeEqual() {
        JwtTokenResponse jwtTokenResponse = aJwtTokenResponse().token("token").build();
        JwtTokenResponse jwtTokenResponse2 = aJwtTokenResponse().token("token").build();
        assertThat(jwtTokenResponse, hasSameStateAsJwtTokenResponse(jwtTokenResponse2));

        int hashCode1 = jwtTokenResponse.hashCode();
        int hashCode2 = jwtTokenResponse2.hashCode();

        assertThat(hashCode1, is (hashCode2));
    }

    @Test
    public void testHashCodeNewObjectsWithDifferentValues_shouldNotBeEqual() {
        JwtTokenResponse jwtTokenResponse = aJwtTokenResponse().token("token").build();
        JwtTokenResponse jwtTokenResponse2 = aJwtTokenResponse().token("Differenttoken").build();

        int hashCode1 = jwtTokenResponse.hashCode();
        int hashCode2 = jwtTokenResponse2.hashCode();
        assertThat(hashCode1, not (hashCode2));
    }

    @Test
    public void testToString_shouldContainAllValues() {
        JwtTokenResponse jwtTokenResponse = aJwtTokenResponse().token("token").build();

        String str = jwtTokenResponse.toString();
        assertThat(str, containsString("token=token"));
    }
}
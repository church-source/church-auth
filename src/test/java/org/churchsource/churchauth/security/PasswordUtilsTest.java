package org.churchsource.churchauth.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordUtilsTest {
    @InjectMocks
    private PasswordUtils passwordUtils;

    @Test
    public void testGetEncodedPassword_shouldReturnEncodedPassword() {
        BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
        BCryptPasswordEncoder bpeSpied = Mockito.spy(bpe);
        PasswordUtils pu = Mockito.spy(passwordUtils);
        when(pu.getBCryptPasswordEncoder()).thenReturn(bpeSpied);
        when(bpeSpied.encode("password")).thenReturn("encoded");

        String encodedPassword = pu.getEncodedPassword("password");
        assertThat(encodedPassword, is("encoded"));
    }

}

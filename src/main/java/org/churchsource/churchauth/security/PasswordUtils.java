package org.churchsource.churchauth.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class PasswordUtils {
    public String getEncodedPassword(String password) {
        return getBCryptPasswordEncoder().encode(password);
    }

    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
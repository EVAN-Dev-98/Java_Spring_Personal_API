package com.personal_api.personal_api.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Password {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String HashPassword(String password) {
        return encoder.encode(password);
    }

    public static boolean CheckPassword(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}

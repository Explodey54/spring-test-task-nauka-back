package com.testtask.nauka.common.utils;

import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderProvider {
    @Getter
    private final PasswordEncoder encoder = new BCryptPasswordEncoder(11);
}

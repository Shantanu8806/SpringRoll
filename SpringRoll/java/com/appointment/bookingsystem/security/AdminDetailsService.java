package com.appointment.bookingsystem.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AdminDetailsService implements UserDetailsService {

    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "admin123"; // Plain text as per your request

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (ADMIN_USERNAME.equals(username)) {
            return User.builder()
                    .username(ADMIN_USERNAME)
                    .password(ADMIN_PASSWORD)
                    .roles("ADMIN")
                    .build();
        } else {
            throw new UsernameNotFoundException("Admin not found with username: " + username);
        }
    }
}

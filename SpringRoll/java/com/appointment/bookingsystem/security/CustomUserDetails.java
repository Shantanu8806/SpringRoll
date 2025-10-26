package com.appointment.bookingsystem.security;

import com.appointment.bookingsystem.entity.Doctor;
import com.appointment.bookingsystem.entity.Staff;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private String role;

    public CustomUserDetails(Doctor doctor) {
        this.username = doctor.getEmail();
        this.password = doctor.getPassword();
        this.role = "DOCTOR";
    }

    public CustomUserDetails(Staff staff) {
        this.username = staff.getEmail();
        this.password = staff.getPassword();
        this.role = "STAFF";
    }

    public CustomUserDetails(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role; // e.g., "ADMIN"
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

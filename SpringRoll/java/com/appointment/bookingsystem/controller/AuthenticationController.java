package com.appointment.bookingsystem.controller;

import com.appointment.bookingsystem.security.JwtTokenUtil;
import com.appointment.bookingsystem.security.AdminDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminDetailsService adminDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Load user role
            String role = adminDetailsService.loadUserByUsername(request.getUsername()).getAuthorities()
                            .iterator().next().getAuthority();

            String token = jwtTokenUtil.generateToken(request.getUsername(), role);

            return ResponseEntity.ok(new LoginResponse(token, role));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(null);
        }
    }
}

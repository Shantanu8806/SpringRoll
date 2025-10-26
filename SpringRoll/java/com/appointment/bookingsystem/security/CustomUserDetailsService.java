package com.appointment.bookingsystem.security;

import com.appointment.bookingsystem.entity.Doctor;
import com.appointment.bookingsystem.entity.Staff;
import com.appointment.bookingsystem.repository.DoctorRepository;
import com.appointment.bookingsystem.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check Doctor
        Doctor doctor = doctorRepository.findByEmail(username);
        if (doctor != null) {
            return new CustomUserDetails(doctor);
        }

        // Check Staff
        Staff staff = staffRepository.findByEmail(username);
        if (staff != null) {
            return new CustomUserDetails(staff);
        }

        // Hardcoded Admin
        if ("admin@hospital.com".equals(username)) {
            return new CustomUserDetails("admin@hospital.com", "Admin@123", "ADMIN");
        }

        throw new UsernameNotFoundException("User not found with email: " + username);
    }
}

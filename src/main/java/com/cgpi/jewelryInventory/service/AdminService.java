package com.cgpi.jewelryInventory.service;

import com.cgpi.jewelryInventory.dto.RegisterRequest;
import com.cgpi.jewelryInventory.model.Admin;
import com.cgpi.jewelryInventory.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin register(RegisterRequest request) {

        if (adminRepository.existsByUsername(request.getUsername()))
            throw new IllegalArgumentException("Username already taken");

        if (adminRepository.existsByEmail(request.getEmail()))
            throw new IllegalArgumentException("Email already in use");

        String role = "ROLE_" + request.getRole().toUpperCase();

        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRoles(role);

        return adminRepository.save(admin);
    }
}

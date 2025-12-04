package com.cgpi.jewelryInventory.controller;

import com.cgpi.jewelryInventory.dto.*;
import com.cgpi.jewelryInventory.model.Admin;
import com.cgpi.jewelryInventory.security.JwtUtil;
import com.cgpi.jewelryInventory.service.AdminService;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final AdminService adminService;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(AdminService adminService, AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.adminService = adminService;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        Admin saved = adminService.register(request);
        return ResponseEntity.ok("User registered with role: " + saved.getRoles());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = (User) auth.getPrincipal();

        String roles = String.join(",", user.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .toList());

        String token = jwtUtil.generateToken(user.getUsername(), roles);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}

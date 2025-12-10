package com.example.rbac.user_management_rbac.services;


import com.example.rbac.user_management_rbac.DTO.JwtResponse;
import com.example.rbac.user_management_rbac.DTO.LoginRequest;
import com.example.rbac.user_management_rbac.DTO.RegisterRequest;
import com.example.rbac.user_management_rbac.entity.Role;
import com.example.rbac.user_management_rbac.entity.User;
import com.example.rbac.user_management_rbac.repository.RoleRepository;
import com.example.rbac.user_management_rbac.repository.UserRepository;
import com.example.rbac.user_management_rbac.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final EventPublisherService eventPublisher;

    public AuthService(UserRepository userRepo,
                       RoleRepository roleRepo,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil, EventPublisherService eventPublisher) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void register(RegisterRequest request) {
        // validate unique email
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // default role: ROLE_USER
        Role roleUser = roleRepo.findByName("ROLE_USER")
                .orElseGet(() -> roleRepo.save(new Role("ROLE_USER")));

        user.getRoles().add(roleUser);

        User saved=userRepo.save(user);
        eventPublisher.publishUserRegistered(saved);
    }

    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = (User) authentication.getPrincipal();
        eventPublisher.publishUserLogin(user);

        String token = jwtUtil.generateToken(user);

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String usernameForResponse = user.getUsername(); // returns email in your current User class
        String email = user.getEmail();

        return new JwtResponse(token, user.getId(), usernameForResponse, email, roles);
    }
}

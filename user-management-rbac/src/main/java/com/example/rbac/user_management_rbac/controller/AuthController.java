package com.example.rbac.user_management_rbac.controller;

import com.example.rbac.user_management_rbac.DTO.JwtResponse;
import com.example.rbac.user_management_rbac.DTO.LoginRequest;
import com.example.rbac.user_management_rbac.DTO.RegisterRequest;
import com.example.rbac.user_management_rbac.DTO.UserProfileResponse;
import com.example.rbac.user_management_rbac.entity.Role;
import com.example.rbac.user_management_rbac.entity.User;
import com.example.rbac.user_management_rbac.repository.RoleRepository;
import com.example.rbac.user_management_rbac.repository.UserRepository;
import com.example.rbac.user_management_rbac.security.JwtUtil;
import com.example.rbac.user_management_rbac.services.AuthService;
import com.example.rbac.user_management_rbac.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    /*private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepo,
                          RoleRepository roleRepo,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authManager,
                          JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }*/

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService,
                          UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    /*@PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // default role USER
        Role roleUser = roleRepo.findByName("ROLE_USER")
                .orElseGet(() -> roleRepo.save(new Role("ROLE_USER")));
        user.getRoles().add(roleUser);

        userRepo.save(user);

        return ResponseEntity.ok("User registered successfully");
    }*/

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    /*@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) auth.getPrincipal();
        String token = jwtUtil.generateToken(user);

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        JwtResponse resp = new JwtResponse(token, user.getId(), user.getUsername(), user.getEmail(), roles);
        return ResponseEntity.ok(resp);
    }*/

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }


    /*@GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMe(@AuthenticationPrincipal User user) {
        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        UserProfileResponse profile =
                new UserProfileResponse(user.getId(), user.getUsername(), user.getEmail(), roles);

        return ResponseEntity.ok(profile);
    }*/

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMe() {
        UserProfileResponse profile = userService.getCurrentUserProfile();
        return ResponseEntity.ok(profile);
    }
}

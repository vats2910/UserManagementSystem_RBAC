package com.example.rbac.user_management_rbac.controller;

import com.example.rbac.user_management_rbac.DTO.AdminStatsResponse;
import com.example.rbac.user_management_rbac.DTO.AssignRolesRequest;
import com.example.rbac.user_management_rbac.entity.Role;
import com.example.rbac.user_management_rbac.entity.User;
import com.example.rbac.user_management_rbac.repository.RoleRepository;
import com.example.rbac.user_management_rbac.repository.UserRepository;
import com.example.rbac.user_management_rbac.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController {

    /*private final RoleRepository roleRepo;
    private final UserRepository userRepo;

    public AdminController(RoleRepository roleRepo, UserRepository userRepo) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
    }*/

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // create role: POST /api/roles  body: { "name": "ADMIN" } or "ROLE_ADMIN"
    /*@PostMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createRole(@RequestBody Role roleReq) {
        String name = roleReq.getName();
        if (!name.startsWith("ROLE_")) {
            name = "ROLE_" + name;
        }

        if (roleRepo.findByName(name).isPresent()) {
            return ResponseEntity.badRequest().body("Role already exists");
        }

        Role role = new Role(name);
        roleRepo.save(role);

        return ResponseEntity.ok("Role created: " + name);
    }*/

    @PostMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createRole(@RequestBody Role roleReq) {
        String message = adminService.createRole(roleReq.getName());
        return ResponseEntity.ok(message);
    }

    // assign roles: POST /api/users/{id}/roles  body: { "roles": ["ADMIN", "USER"] }
    /*@PostMapping("/users/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignRoles(@PathVariable Long userId,
                                         @RequestBody AssignRolesRequest request) {

        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        for (String r : request.getRoles()) {
            String full = r.startsWith("ROLE_") ? r : "ROLE_" + r;
            Role role = roleRepo.findByName(full)
                    .orElseGet(() -> roleRepo.save(new Role(full)));
            user.getRoles().add(role);
        }

        userRepo.save(user);
        return ResponseEntity.ok("Roles assigned to user");
    }*/

    @PostMapping("/users/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignRoles(@PathVariable Long userId,
                                         @Valid @RequestBody AssignRolesRequest request) {
        adminService.assignRolesToUser(userId, request);
        return ResponseEntity.ok("Roles assigned to user");
    }


    /*@GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminStatsResponse> getAdminStats() {

        long totalUsers = userRepo.count();

        // mock last login times (because we don't store lastLogin in the entity)
        List<String> lastLogins = Arrays.asList(
                "admin@example.com -> 2025-12-09T08:30:00",
                "john@example.com -> 2025-12-09T08:10:00"
        );

        AdminStatsResponse response = new AdminStatsResponse(totalUsers, lastLogins);

        return ResponseEntity.ok(response);
    }*/

    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminStatsResponse> getStats() {
        AdminStatsResponse stats = adminService.getAdminStats();
        return ResponseEntity.ok(stats);
    }

}
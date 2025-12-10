package com.example.rbac.user_management_rbac.services;


import com.example.rbac.user_management_rbac.DTO.AdminStatsResponse;
import com.example.rbac.user_management_rbac.DTO.AssignRolesRequest;
import com.example.rbac.user_management_rbac.entity.Role;
import com.example.rbac.user_management_rbac.entity.User;
import com.example.rbac.user_management_rbac.repository.RoleRepository;
import com.example.rbac.user_management_rbac.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AdminService {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final UserService userService;

    public AdminService(RoleRepository roleRepo,
                        UserRepository userRepo,
                        UserService userService) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @Transactional
    public String createRole(String rawName) {
        String name = rawName;
        if (!name.startsWith("ROLE_")) {
            name = "ROLE_" + name;
        }

        if (roleRepo.findByName(name).isPresent()) {
            throw new RuntimeException("Role already exists: " + name);
        }

        Role role = new Role(name);
        roleRepo.save(role);

        return "Role created: " + name;
    }

    @Transactional
    public void assignRolesToUser(Long userId, AssignRolesRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        for (String rawRole : request.getRoles()) {
            String name = rawRole.startsWith("ROLE_") ? rawRole : "ROLE_" + rawRole;

            Role role = roleRepo.findByName(name)
                    .orElseGet(() -> roleRepo.save(new Role(name)));

            user.getRoles().add(role);
        }

        userRepo.save(user);
    }

    public AdminStatsResponse getAdminStats() {
        long totalUsers = userService.getTotalUsers();

        // mock last login data – you can replace this with real fields later
        List<String> lastLogins = Arrays.asList(
                "admin@gmail.com -> 2025-12-09T08:30:00",
                "adarsh@gmail.com -> 2025-12-09T08:10:00"
        );

        return new AdminStatsResponse(totalUsers, lastLogins);
    }
}

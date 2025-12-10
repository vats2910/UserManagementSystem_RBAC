package com.example.rbac.user_management_rbac.services;

import com.example.rbac.user_management_rbac.DTO.UserProfileResponse;
import com.example.rbac.user_management_rbac.entity.Role;
import com.example.rbac.user_management_rbac.entity.User;
import com.example.rbac.user_management_rbac.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Get profile of currently logged-in user using SecurityContext.
     */
    public UserProfileResponse getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("User is not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return new UserProfileResponse(
                user.getId(),
                user.getUsername(), // or user.getEmail(), or a custom getDisplayName()
                user.getEmail(),
                roles
        );
    }

    /**
     * Helper to get a user by id (for admin or other features).
     */
    public User getUserById(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    public long getTotalUsers() {
        return userRepo.count();
    }
}

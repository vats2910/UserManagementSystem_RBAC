package com.example.rbac.user_management_rbac.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserProfileResponse {

    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    public UserProfileResponse(Long id, String username, String email, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

}

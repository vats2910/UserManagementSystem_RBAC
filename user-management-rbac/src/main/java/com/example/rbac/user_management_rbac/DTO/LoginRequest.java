package com.example.rbac.user_management_rbac.DTO;


import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;

}

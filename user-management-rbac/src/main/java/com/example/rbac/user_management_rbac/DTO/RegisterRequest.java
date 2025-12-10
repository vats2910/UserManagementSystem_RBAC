package com.example.rbac.user_management_rbac.DTO;

import jdk.jfr.DataAmount;
import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    private String email;
    private String password;

}

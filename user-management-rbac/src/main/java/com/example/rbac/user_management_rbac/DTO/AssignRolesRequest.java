package com.example.rbac.user_management_rbac.DTO;

import java.util.List;

public class AssignRolesRequest {
    private List<String> roles;

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}

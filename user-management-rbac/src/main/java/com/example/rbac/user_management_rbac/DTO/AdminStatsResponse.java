package com.example.rbac.user_management_rbac.DTO;


import java.util.List;

public class AdminStatsResponse {
    private long totalUsers;
    private List<String> lastLogins; // simple mock data

    public AdminStatsResponse() {
    }

    public AdminStatsResponse(long totalUsers, List<String> lastLogins) {
        this.totalUsers = totalUsers;
        this.lastLogins = lastLogins;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public List<String> getLastLogins() {
        return lastLogins;
    }

    public void setLastLogins(List<String> lastLogins) {
        this.lastLogins = lastLogins;
    }

}

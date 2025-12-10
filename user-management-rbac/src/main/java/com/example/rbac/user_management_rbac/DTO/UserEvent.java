package com.example.rbac.user_management_rbac.DTO;

public class UserEvent {

    private String type;      // "USER_REGISTERED" / "USER_LOGIN"
    private Long userId;
    private String email;
    private String username;
    private long timestamp;   // epoch millis

    public UserEvent() {
    }

    public UserEvent(String type, Long userId, String email, String username, long timestamp) {
        this.type = type;
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}


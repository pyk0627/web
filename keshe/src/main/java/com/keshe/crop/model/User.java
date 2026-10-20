package com.keshe.crop.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 系统用户 JavaBean。
 */
public class User {

    private Long id;
    private String username;
    private String passwordHash;
    private String displayName;
    private String email;
    private String phone;
    private Role role = Role.USER;
    private boolean enabled = true;
    private LocalDateTime createdAt;

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public String getRoleLabel() {
        return role == null ? "" : role.getLabel();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCreatedDate() {
        if (createdAt == null) {
            return "—";
        }
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

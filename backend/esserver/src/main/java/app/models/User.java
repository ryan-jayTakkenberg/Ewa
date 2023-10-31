package app.models;

import app.authentication.PermissionLevel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class User {

    public static List<User> list = new ArrayList<>();

    private UUID id;
    private PermissionLevel permissionLevel;
    private String name;
    private String email;
    private LocalDate lastLogin;
    private String password;

    public User(String id, String permissionLevel, String name, String email, String lastLogin, String password) {
        this.id = id != null ? UUID.fromString(id) : null;
        this.permissionLevel = permissionLevel != null ? PermissionLevel.valueOf(permissionLevel.toUpperCase()) : null;
        this.name = name;
        this.email = email;
        this.lastLogin = lastLogin != null ? LocalDate.parse(lastLogin) : null;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(PermissionLevel permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

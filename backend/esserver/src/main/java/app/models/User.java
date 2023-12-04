package app.models;

import app.Util;
import app.enums.PermissionLevel;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue
    private long id;

    private PermissionLevel permissionLevel;

    private String name;
    private String email;
    private LocalDate lastLogin;
    private String password;
    private String resetToken;
    private LocalDateTime resetTokenExpiry;

    @OneToMany(mappedBy = "app_user")
    private Set<Report> reports;

    public User(PermissionLevel permissionLevel, String name, String email, LocalDate lastLogin, String password) {
        this.permissionLevel = permissionLevel;
        this.name = name;
        this.email = email;
        this.lastLogin = lastLogin;
        this.password = Util.hash(password);
    }

    public User() {

    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getResetTokenExpiry() {
        return resetTokenExpiry;
    }

    public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
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

    public long getId() {
        return id;
    }
}

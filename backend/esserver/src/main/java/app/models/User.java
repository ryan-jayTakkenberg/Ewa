package app.models;

import app.util.HashUtil;
import app.enums.PermissionLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
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
    @Enumerated(EnumType.STRING)
    private PermissionLevel permissionLevel;
    private String name;
    private String email;
    private LocalDate lastLogin;
    private String password;
    private String resetToken;
    private LocalDateTime resetTokenExpiry;
    @OneToOne(cascade = CascadeType.MERGE)
    @JsonIgnoreProperties({"user", "warehouse"})
    private Team team;

    @OneToMany(mappedBy = "app_user")
    @JsonIgnore
    private Set<Report> reports;  //todo implement reports user

    public User(PermissionLevel permissionLevel, String name, String email, LocalDate lastLogin, String password, Team team) {
        this.permissionLevel = permissionLevel;
        this.name = name;
        this.email = email;
        this.lastLogin = lastLogin;
        this.password = HashUtil.hash(password);
        this.team = team;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public void setPasswordAndHash(String password) {
        setPassword(HashUtil.hash(password));
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
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
}

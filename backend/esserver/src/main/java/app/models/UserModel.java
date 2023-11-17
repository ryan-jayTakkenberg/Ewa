package app.models;

import app.Util;
import app.enums.PermissionLevel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class UserModel {

    @Id
    @GeneratedValue
    private long id;

    private PermissionLevel permissionLevel;

    private String name;
    private String email;
    private LocalDate lastLogin;
    private String password;

    public UserModel(PermissionLevel permissionLevel, String name, String email, LocalDate lastLogin, String password) {
        this.permissionLevel = permissionLevel;
        this.name = name;
        this.email = email;
        this.lastLogin = lastLogin;
        this.password = Util.hash(password);
    }

    public UserModel() {

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

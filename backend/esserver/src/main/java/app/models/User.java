package app.models;

import app.authentication.PermissionLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    public static List<User> list = new ArrayList<>();

    private UUID id;
    private PermissionLevel permissionLevel;
    public final List<Long> warehouseIds;

    public User(String id) {
        this(id, PermissionLevel.ADMIN, new ArrayList<>());
    }

    public User(String id, PermissionLevel permissionLevel, List<Long> warehouseIds) {
        this.id = UUID.fromString(id);
        this.permissionLevel = permissionLevel;
        this.warehouseIds = warehouseIds;
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
}

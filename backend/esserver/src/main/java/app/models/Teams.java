package app.models;

import java.util.ArrayList;
import java.util.List;

public class Teams {

    private int id;
    private String name;
    private List<UserModel> users;
    private String warehouse;
    private String project;

    public Teams(int id, String name, String warehouse, String project, List<UserModel> users) {
        this.id = id;
        this.name = name;
        this.warehouse = warehouse;
        this.project = project;
        this.users = users != null ? new ArrayList<>(users) : new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}

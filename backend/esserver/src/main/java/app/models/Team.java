package app.models;

import app.enums.PermissionLevel;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = "TEAM_ID_COUNT", query = "SELECT COUNT(p) FROM Team t LEFT JOIN t.projects p WHERE t.id = :teamId"),
})
@Entity
public class Team {

    @Id
    @GeneratedValue
    private int id;
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_id")
    @JsonIncludeProperties({"id", "name"})
    private Warehouse warehouse;

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL)
    private User user;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"team"})
    private final Set<Project> projects = new HashSet<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private final Set<Order> orders = new HashSet<>();

    // TODO why does team have permission level? User already has this?!
    private PermissionLevel permissionLevel;



    public Team(PermissionLevel permissionLevel, int id, String name, Warehouse warehouse) {
        this.permissionLevel = permissionLevel;
        this.id = id;
        this.name = name;
        this.warehouse = warehouse;
    }

    public Team(String name, Warehouse warehouse) {
        this.id = 0;
        this.name = name;
        this.warehouse = warehouse;
    }

    public Team(){

    }
    public User getUser() {
        return user;
    }
    public enum Name {
        TeamWest("TeamWest"),
        TeamNoord("TeamNoord"),
        TeamOost("TeamOost"),
        TeamZuid("TeamZuid");
        private final String name;
        Name(String name) {
            this.name = name;
        }
    }

    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(PermissionLevel permissionLevel) {
        this.permissionLevel = permissionLevel;
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

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        if (this.warehouse != null) {
            this.warehouse.removeTeam(this);
        }
        this.warehouse = warehouse;
        if (this.warehouse != null) {
            this.warehouse.addTeam(this);
        }
    }



    public void addProject(Project project) {
        this.projects.add(project);
        project.setTeam(this);
    }

    public void removeProject(Project project) {
        this.projects.remove(project);
        project.setTeam(null);
    }


    public void setUser(User user) {
        this.user = user;
    }

    public Set<Project> getProjects() {
        return projects;
    }


}

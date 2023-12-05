package app.models;

import app.enums.PermissionLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;

import java.util.List;
@NamedQueries({
        @NamedQuery(name = "TEAM_ID_COUNT", query = "SELECT COUNT(p) FROM Team t LEFT JOIN t.projects p WHERE t.id = :teamId"),
})
@Entity
public class Team {

    @Id
    @GeneratedValue
    private int id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    @JsonIncludeProperties({"id", "name"})
    private Warehouse warehouse;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties("team")
    private User user;

    @OneToMany(mappedBy = "team")
    @JsonIgnoreProperties({"team"})
    private List<Project>  projects;
    private PermissionLevel permissionLevel;

    public Team(PermissionLevel permissionLevel,int id, String name, Warehouse warehouse) {
        this.permissionLevel = permissionLevel;
        this.id = id;
        this.name = name;
        this.warehouse = warehouse;
        this.user = user;
    }
    public Team(){

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

    public List<Project> getProjects() {
        return projects;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}

package app.models;

import app.enums.PermissionLevel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Teams {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String warehouse;

    @OneToMany(mappedBy = "team")
    @JsonIgnoreProperties({"team"})
    private List<Project>  projects;
    private PermissionLevel permissionLevel;

    public Teams(PermissionLevel permissionLevel,int id, String name, String warehouse) {
        this.permissionLevel = permissionLevel;
        this.id = id;
        this.name = name;
        this.warehouse = warehouse;
    }
    public Teams(){

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

    public static Teams createSampleTeam(int pId) {
        int id = pId;
        String name = String.valueOf(Teams.Name.values()[(int) (Math.random() * Teams.Name.values().length)]);
        String warehouse = ("warehouse " + id);

        return new Teams(PermissionLevel.ADMIN,id, name, warehouse);

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

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public List<Project> getProjects() {
        return projects;
    }
}

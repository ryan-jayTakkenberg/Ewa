package app.models;

import app.enums.PermissionLevel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
@Entity
public class Teams {

@Id
@GeneratedValue
    private int id;
    private String name;
    private String warehouse;
    private String project;
    private PermissionLevel permissionLevel;

    public Teams(PermissionLevel permissionLevel,int id, String name, String warehouse, String project) {
        this.permissionLevel = permissionLevel;
        this.id = id;
        this.name = name;
        this.warehouse = warehouse;
        this.project = project;
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

    public enum Project {
        Hva("Hva"),
        Mediamarkt("Mediamarkt"),
        Overheid("Overheid"),
        Politie("Politie");
        private final String project;
        Project(String name) {
            this.project = name;
        }
    }

    public static Teams createSampleTeam(int pId) {
        int id = pId;
        String name = String.valueOf(Teams.Name.values()[(int) (Math.random() * Teams.Name.values().length)]);
        String warehouse = ("warehouse " + id);
        String project = String.valueOf(Teams.Project.values()[(int) (Math.random() * Teams.Project.values().length)]);



        return new Teams(PermissionLevel.ADMIN,id, name, warehouse, project);

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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}

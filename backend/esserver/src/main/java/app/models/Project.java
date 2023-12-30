package app.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Project {

    /*
     * BACKEND: MODEL
     * Every model should have an @Entity annotation before the class.
     * Every model should also have an @Id annotation, every id must be unique!
     *
     * You can see all the tables in: 'localhost:8085/api/h2-console'
     * Remember to set the 'JDBC URL' to 'jdbc:h2:mem:testdb'
     */

    @Id
    @GeneratedValue
    private int projectId;
    private String projectName;
    private String clientName;
    private LocalDate installDate;
    private String notes;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIncludeProperties({"id", "name"})
    private Team team;

    public Project(int projectId, String projectName, String clientName, LocalDate installDate, String notes, Team team) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.clientName = clientName;
        this.installDate = installDate;
        this.notes = notes;
        this.team = team;
    }

    public Project() {

    }

    public int getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getClientName() {
        return clientName;
    }

    public LocalDate getInstallDate() {
        return installDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }



    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        if (this.team != null) {
            this.team.removeProject(this);
        }
        this.team = team;
        if (this.team != null) {
            this.team.addProject(this);
        }
    }
}

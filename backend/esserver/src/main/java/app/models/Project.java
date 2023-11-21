package app.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.Date;

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

    //TODO pass actual Teams object and Warehouse object into this class instead of hardcoded String

    @Id
    @GeneratedValue
    private int projectId;
    private String projectName;
    private String clientName;
    private LocalDate installDate;
    private String installTeamId;
    private String usedWarehouseId;
    private String notes;

    public Project(int projectId, String projectName, String clientName, LocalDate installDate, String installTeamId, String usedWarehouseId, String notes) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.clientName = clientName;
        this.installDate = installDate;
        this.installTeamId = installTeamId;
        this.usedWarehouseId = usedWarehouseId;
        this.notes = notes;
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

    public String getInstallTeamId() {
        return installTeamId;
    }

    public String getUsedWarehouseId() {
        return usedWarehouseId;
    }

    public String getNotes() {
        return notes;
    }
}

package app.models;

import java.util.Date;

public class Project {

    private int projectId;
    private String projectName;
    private String clientName;
    private Date installDate;
    private Teams installTeamId;
    private Warehouse usedWarehouseId;
    private String notes;

    public Project(int projectId, String projectName, String clientName, Date installDate, Teams installTeamId, Warehouse usedWarehouseId, String notes) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.clientName = clientName;
        this.installDate = installDate;
        this.installTeamId = installTeamId;
        this.usedWarehouseId = usedWarehouseId;
        this.notes = notes;
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

    public Date getInstallDate() {
        return installDate;
    }

    public Teams getInstallTeamId() {
        return installTeamId;
    }

    public Warehouse getUsedWarehouseId() {
        return usedWarehouseId;
    }

    public String getNotes() {
        return notes;
    }
}

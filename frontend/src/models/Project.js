export class Project{
    projectName;
    clientName;
    installDate;
    installTeam;
    usedWarehouse;
    notes;

    constructor(projectName, clientName, installDate, installTeam, usedWarehouse, notes) {
        this.projectName = projectName;
        this.clientName = clientName;
        this.installDate = installDate;
        this.installTeam =installTeam
        this.usedWarehouse = usedWarehouse;
        this.notes = notes;
    }

    static createSampleProject(){
        const projectName = "Project EWA";
        const clientName = "HVA";
        const installDate = "20-10-2023";
        const installTeam = "Team 1";
        const usedWarehouse = "Warehouse 1";
        const notes = "This is a project";

        return new Project(projectName, clientName, installDate, installTeam, usedWarehouse, notes);
    }
}
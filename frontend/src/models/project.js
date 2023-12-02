import {getAPI} from "@/backend";

export default class Project {
    projectId;
    projectName;
    clientName;
    installDate;
    notes;
    team;

    constructor(projectId, projectName, clientName, installDate, notes, team) {
        this.projectId = projectId
        this.projectName = projectName;
        this.clientName = clientName;
        this.installDate = installDate;
        this.notes = notes;
        this.team = team;
    }

    clone() {
        return new Project(this.projectId, this.projectName, this.clientName, this.installDate, this.notes, this.team);
    }

    equals(other) {
        if (!other) {
            return false;
        }
        for (let attr of Object.keys(this)) {
            if (typeof this[attr] !== typeof other[attr] || `${this[attr]}` !== `${other[attr]}`) {
                return false;
            }
        }
        return true;
    }

    /**
     * put this Project into the database
     * will add a new Project to the database if no Project exists
     * will override the existing Project with the new Project if the Project already exists
     */
    async putDatabase() {
        try {
            const isNewProject = this.id < 0;
            // TODO make a post request to the backend
            //  if the current Project id is -1, receive the new Project id
            if (isNewProject) {
                this.id = Project.projects.length;// receive the new Project id
                Project.projects.push(this);
            } else {
                Project.projects[Project.projects.findIndex(o => o.id === this.id)] = this;
            }
            return true;
        } catch (e) {
            return false;
        }
    }

    /**
     * delete this Project from the database
     */
    async delDatabase() {
        try {
            const isNewProject = this.id < 0;
            if (isNewProject) {
                return false;
            }
            // TODO make a delete request to the backend
            Project.projects = Project.projects.filter(o => o.id !== this.id);
            return true;
        } catch (e) {
            return false;
        }
    }

    /**
     * get all the Projects from the database
     */
    static async getDatabase() {
        try {
            this.fetching = true;
            const response = await getAPI("/api/projects");
            return response.data;
        } catch (e) {
            return [];
        } finally {
            this.fetching = false;
        }
    }

    static fetching = true;
    static projects = [];

    static createNewProject(projectName, clientName, installDate, installTeamId, usedWarehouseId, notes) {
        return new Project(-1, projectName, clientName, installDate, installTeamId, usedWarehouseId, notes);
    }
}
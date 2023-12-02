import Project from "@/models/project";
import {deleteAPI, getAPI, postAPI, putAPI, responseOk} from "@/backend";
import {classToObject} from "@/models/helper";

export class ProjectAdaptor {
    async asyncFindAll(){
        const response = await getAPI("api/projects");

        if (!responseOk(response)){
            console.error(response.data);
            return[];
        }

        if(!Array.isArray(response.data)){
            console.error('Geen geldige JSON-array ontvangen' + response.data);
            return [];
        }

        return response.data.map(data => new Project(data.projectId, data.projectName, data.clientName, data.installDate, data.notes, data.team))
    }

    async asyncFindById(id){
        const response = await getAPI(`/api/projects/${id}`);

        if (!responseOk(response)){
            console.error(response.data)
            return[];
        }

        return response.data;
    }

    async asyncSaveProject(project){
        const response = await postAPI("/api/projects", classToObject(project));

        if (!responseOk(response)){
            console.error(response.data);
            return[];
        }

        return response.data;
    }

    async asyncUpdateProject(project) {
        const response = await putAPI(`/api/projects/${project.projectId}`, classToObject(project))

        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        const data = response.data
        if (!data.projectId || !data.projectName || !data.clientName || !data.installDate || !data.notes || !data.team){
            console.error("Failed to update project. Empty response: " + data)
            return[];
        }

        return new Project(data.projectId, data.projectName, data.clientName, data.installDate, data.notes, data.team);
    }

    async asyncDeleteById(id){
        const response = await deleteAPI(`/api/projects/${id}`);

        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        return response.data;
    }
}
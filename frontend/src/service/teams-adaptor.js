import Team from "@/models/team";
import {deleteAPI, getAPI, postAPI, putAPI, responseOk} from "@/backend";
import {classToObject} from "@/models/helper";

export class TeamsAdaptor {

    async asyncFindAll() {
        const response = await getAPI("/api/teams");
        if (!responseOk(response)) {
            console.error(response.data);
            return [];
        }

        if (!Array.isArray(response.data)) {
            console.error('Geen geldige JSON-array ontvangen: ' + response.data);
            return [];
        }

        return response.data.map(data => new Team(data.id, data.name, data.warehouse, data.project));
    }

    async asyncFindById(id) {
        const response = await getAPI(`/api/teams/${id}`);
        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        return response.data;
    }

    async asyncSaveTeam(team) {
        const response = await postAPI("/api/teams", classToObject(team));
        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        if (response.data.id) {
            team.id = response.data.id;
        }

        return response.data;
    }


    async asyncUpdateTeam(team) {
        const response = await putAPI(`/api/teams/${team.id}`, classToObject(team));
        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        const data = response.data;

        // Checking if the response contains the updated team information
        if (!data.id || !data.name || !data.warehouse || !data.project) {
            console.error("Failed to update team. Empty response: " + data);
            return {};
        }

        return new Team(data.id, data.name, data.warehouse, data.project);
    }




    async asyncDeleteById(id) {
        const response = await deleteAPI(`/api/teams/${id}`);
        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        return response.data;
    }
}
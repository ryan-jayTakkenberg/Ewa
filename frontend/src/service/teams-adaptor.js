import Team from "@/models/team";
import {deleteAPI, getAPI, postAPI, putAPI, responseOk} from "@/backend";
import {classToObject} from "@/models/helper";

export class TeamsAdaptor {

    // Fetches all teams from the API
    async asyncFindAll() {
        const response = await getAPI("/api/teams");

        // Check if the response is successful
        if (!responseOk(response)) {
            console.error(response.data);
            return [];
        }

        // Check if the response data is a valid JSON array
        if (!Array.isArray(response.data)) {
            console.error('Geen geldige JSON-array ontvangen: ' + response.data);
            return [];
        }

        // Map the response data to Team objects and return the array
        return response.data.map(data => new Team(data.id, data.name, data.warehouse, data.project));
    }

    // Fetches a team by its ID from the API
    async asyncFindById(id) {
        const response = await getAPI(`/api/teams/${id}`);

        // Check if the response is successful
        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        // Return the response data
        return response.data;
    }

    // Saves a new team to the API
    async asyncSaveTeam(team) {
        const response = await postAPI("/api/teams", classToObject(team));

        // Check if the response is successful
        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        // If the response contains an ID, update the team object with it
        if (response.data.id) {
            team.id = response.data.id;
        }

        // Return the response data
        return response.data;
    }

    // Updates an existing team on the API
    async asyncUpdateTeam(team) {
        const response = await putAPI(`/api/teams/${team.id}`, classToObject(team));

        // Check if the response is successful
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

        // Return a new Team object with the updated information
        return new Team(data.id, data.name, data.warehouse, data.project);
    }

    // Deletes a team by its ID from the API
    async asyncDeleteById(id) {
        const response = await deleteAPI(`/api/teams/${id}`);

        // Check if the response is successful
        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        // Return the response data
        return response.data;
    }
}

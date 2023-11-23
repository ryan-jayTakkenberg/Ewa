import Warehouse from "@/models/warehouse"
import {deleteAPI, getAPI, postAPI, putAPI, responseOk} from "@/backend";
import {classToObject} from "@/models/helper";

export class WarehouseAdaptor {
    resourcesUrl;

    // Fetches all teams from the API
    async asyncFindAll() {
        const response = await getAPI("/api/warehouses");

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
        return response.data.map(data => new Warehouse(data.id, data.name, data.address, data.postalCode, data.city));
    }

    // Fetches a team by its ID from the API
    async asyncFindById(id) {
        const response = await getAPI(`/api/warehouses/${id}`);

        // Check if the response is successful
        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        // Return the response data
        return response.data;
    }

    // Saves a new team to the API
    async asyncSaveTeam(warehouse) {
        const response = await postAPI("/api/warehouse", classToObject(warehouse));

        // Check if the response is successful
        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        // If the response contains an ID, update the team object with it
        if (response.data.id) {
            warehouse.id = response.data.id;
        }

        // Return the response data
        return response.data;
    }

    // Updates an existing team on the API
    async asyncUpdateTeam(warehouse) {
        const response = await putAPI(`/api/warehouse/${warehouse.id}`, classToObject(warehouse));

        // Check if the response is successful
        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        const data = response.data;

        // Checking if the response contains the updated team information
        if (!data.id || !data.name || !data.address || !data.postalCode || !data.city) {
            console.error("Failed to update team. Empty response: " + data);
            return {};
        }

        // Return a new Team object with the updated information
        return new Warehouse(data.id, data.name, data.address, data.postalCode, data.city);
    }

    // Deletes a team by its ID from the API
    async asyncDeleteById(id) {
        const response = await deleteAPI(`/api/warehouse/${id}`);

        // Check if the response is successful
        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        // Return the response data
        return response.data;
    }
}
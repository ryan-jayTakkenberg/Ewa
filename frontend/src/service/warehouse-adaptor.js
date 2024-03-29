import Warehouse from "@/models/warehouse"
import {deleteAPI, getAPI, postAPI, putAPI, responseOk} from "@/backend";
import {classToObject} from "@/models/helper";

export class WarehouseAdaptor {
    resourcesUrl;

    async asyncFindAll() {
        const response = await getAPI("/api/warehouses");

        if (!responseOk(response)) {
            console.error(response.data);
            return [];
        }

        if (!Array.isArray(response.data)) {
            console.error('Geen geldige JSON-array ontvangen: ' + response.data);
            return [];
        }

        return response.data.map(data => new Warehouse(data.id, data.name, data.address, data.postalCode, data.city, data.teams, data.products,
            data.maxStorage, data.minStorage, data.currentStorage));
    }

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

    async asyncSaveWarehouse(warehouse) {
        const response = await postAPI("/api/warehouses", classToObject(warehouse));

        // Check if the response is successful
        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        // Return the response data
        return response.data;
    }

    // Updates an existing warehouse on the API
    async asyncUpdateWarehouse(warehouse) {
        const response = await putAPI(`/api/warehouses/${warehouse.id}`, classToObject(warehouse));

        // Check if the response is successful
        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        const data = response.data;

        // Checking if the response contains the updated team information
        if (!data.id || !data.name || !data.address || !data.postalCode || !data.city ||!data.maxStorage ||!data.minStorage
            ||!data.currentStorage) {
            console.error("Failed to update warehouse. Empty response: " + data);
            return {};
        }

        return new Warehouse(data.id, data.name, data.address, data.postalCode, data.city, data.maxStorage, data.minStorage,
            data.currentStorage);
    }

    async asyncDeleteById(id) {
        const response = await deleteAPI(`/api/warehouses/${id}`);

        // Check if the response is successful
        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        // Return the response data
        return response.data;
    }

    async asyncUpdateProductQuantities(warehouseId, products) {
        const response = await postAPI(`/api/warehouses/updateProducts`, {
            warehouseId: warehouseId,
            products: products
        });

        if (!responseOk(response)) {
            console.error(response.data);
            return {};
        }

        return response.data;
    }
}
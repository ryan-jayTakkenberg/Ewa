import Warehouse from "@/models/warehouse"

export class WarehouseAdaptor {
    resourcesUrl;

    constructor(resourcesURL) {
        this.resourcesUrl = resourcesURL;
    }

    async fetchJson(url, options = null) {
        let response = await fetch(url, options)
        if (response.ok) {
            return await response.json();
        } else {
            console.log(response, !response.bodyUsed ? await response.text() : "");
            return null;
        }
    }

    async asyncFindAll() {
        const warehouseData = await this.fetchJson(this.resourcesUrl);
        if (Array.isArray(warehouseData)) {// checkt op array voorkomt daarom errors
            return warehouseData.map(Warehouse.copyConstructor);
        } else {
            console.error('Geen geldige JSON-array ontvangen.');
            return [];
        }
    }

    async asyncFindById(id) {
        return this.fetchJson(this.resourcesUrl + "/" + id);
    }

    async asyncSave(warehouse) {
        let url = `${this.resourcesUrl}`;
        if (warehouse.id !== 0) {
            url += `/${warehouse.id}`;
        }else {
            url += `/warehouses`;
        }
        const method = warehouse.id === 0 ? 'POST' : 'PUT';
        const options = {
            method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(warehouse)
        };
        const response = await this.fetchJson(url, options);
        if (method === 'POST' && response.id) {
            warehouse.id = response.id;
        }
        return response;
    }

    async asyncDeleteById(id) {
        return this.fetchJson(this.resourcesUrl + "/" + id,
            {
                method: 'DELETE'
            })
    }
}
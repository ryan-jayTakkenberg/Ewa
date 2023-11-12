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

    async asyncAddNew(warehouse) {
        const url = `${this.resourcesUrl}`;
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(warehouse)
        };
        const response = await this.fetchJson(url, options);

        if (response.id) {
            warehouse.id = response.id;
        }

        return response;
    }

    async asyncUpdate(warehouse) {
        const url = `${this.resourcesUrl}/${warehouse.id}`;
        const options = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(warehouse)
        };
        const response = await this.fetchJson(url, options);

        return response;
    }


    async asyncDeleteById(id) {
        return this.fetchJson(this.resourcesUrl + "/" + id,
            {
                method: 'DELETE'
            })
    }
}
import {getAPI, responseOk} from "@/backend";

export default class Warehouse {
    id;
    name;
    address;
    postalCode;
    city;
    teams;
    products;

    constructor(id, name, address, postalCode, city, teams, products) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.teams = teams;
        this.products = products;
    }

    static copyConstructor(warehouse) {
       try {
           if (warehouse == null) return null;
           return Object.assign(new Warehouse(), warehouse)
       } catch (error){
            console.error("Error with copying of warehouse", error)
           return null;
       }
    }

    injectAttributes(from) {
        if (!(from instanceof Object)) {
            return false;
        }
        for (let attr of Object.keys(this)) {
            if (from[attr] !== null && from[attr] !== undefined) {
                this[attr] = from[attr];
            }
        }
        return true;
    }

    clone() {
        let warehouse = new Warehouse();
        warehouse.injectAttributes(this);
        return warehouse;
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
     * put this Warehouse into the database
     * will add a new Warehouse to the database if no Warehouse exists
     * will override the existing Warehouse with the new Warehouse if the Warehouse already exists
     */
    async putDatabase() {
        try {
            const isNewWarehouse = this.id < 0;
            // TODO make a post request to the backend
            //  if the current Warehouse id is -1, receive the new Warehouse id
            if (isNewWarehouse) {
                this.id = Warehouse.warehouses.length;// receive the new Warehouse id
                Warehouse.warehouses.push(this);
            } else {
                Warehouse.warehouses[Warehouse.warehouses.findIndex(o => o.id === this.id)] = this;
            }
            return true;
        } catch (e) {
            return false;
        }
    }

    /**
     * delete this Warehouse from the database
     */
    async delDatabase() {
        try {
            const isNewWarehouse = this.id < 0;
            if (isNewWarehouse) {
                return false;
            }
            // TODO make a delete request to the backend
            Warehouse.warehouses = Warehouse.warehouses.filter(o => o.id !== this.id);
            return true;
        } catch (e) {
            return false;
        }
    }

    /**
     * get all the Warehouses from the database
     */
    static async getDatabase() {
        try {
            this.fetching = true;
            let warehouses = [];
            // make a get request to the backend
            // update "warehouses" with the response
            let response = await getAPI("/api/warehouses");
            if (!responseOk(response)) {
                return warehouses;
            }

            for (let obj of response.data) {
                let warehouse = new Warehouse();
                if (warehouse.injectAttributes(obj)) {
                    warehouses.push(warehouse);
                }
            }
            return warehouses;
        } catch (e) {
            return [];
        } finally {
            this.fetching = false;
        }
    }

    static fetching = true;
    static warehouses = [];
}
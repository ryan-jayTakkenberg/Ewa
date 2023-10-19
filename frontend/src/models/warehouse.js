export default class Warehouse {
    id;
    name;
    address;
    postalCode;
    city;

    static City = [
        "Amsterdam",
        "Haarlem",
        "Zaandam",
        "Den Helder"
    ];

    static PostalCode = [
        "1234 AB",
        "5678 CD",
        "3456 JF",
        "1284 AJ"
    ];

    constructor(id, name, address, postalCode, city) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
    }

    clone() {
        return new Warehouse(this.id, this.name, this.address, this.postalCode, this.city);
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
            // TODO make a get request to the backend
            //  update "Warehouses" with the response
            return [new Warehouse(0, "Warehouse 0", "Address 10", Warehouse.PostalCode[Math.floor(Math.random() * Warehouse.PostalCode.length)], Warehouse.City[Math.floor(Math.random() * Warehouse.City.length)]),
                new Warehouse(1, "Warehouse 1", "Address 124", Warehouse.PostalCode[Math.floor(Math.random() * Warehouse.PostalCode.length)], Warehouse.City[Math.floor(Math.random() * Warehouse.City.length)]),
                new Warehouse(2, "Warehouse 2", "Address 23", Warehouse.PostalCode[Math.floor(Math.random() * Warehouse.PostalCode.length)], Warehouse.City[Math.floor(Math.random() * Warehouse.City.length)])];
        } catch (e) {
            return [];
        } finally {
            this.fetching = false;
        }
    }

    static fetching = true;
    static warehouses = [];

    static createNewWarehouse(name, address, postalCode, city) {
        return new Warehouse(-1, name, address, postalCode, city);
    }
}
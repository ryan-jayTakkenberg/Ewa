export class Warehouse{
    name;
    address;
    postalCode;
    city;

    static City = {
        AMSTERDAM: "Amsterdam",
        HAARLEM: "Haarlem",
        ZAANDAM: "Zaandam",
        DENHELDER: "Den Helder"
    }

    static PostalCode = {
        POSTAL1: "1234 AB",
        POSTAL2: "5678 CD",
        POSTAL3: "3456 JF",
        POSTAL4: "1284 AJ"
    }

    constructor(name, address, postalCode, city) {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
    }

    static createSampleWarehouse(number){
        const name = "Warehouse " + number;
        const address = "Address 10"

        const postalCodeValues = Object.values(Warehouse.PostalCode);
        const postalCode = postalCodeValues[Math.floor(Math.random() * postalCodeValues.length)];

        const cityValues = Object.values(Warehouse.City);
        const city = cityValues[Math.floor(Math.random() * cityValues.length)]

        return new Warehouse(name, address, postalCode, city);
    }
}
export class Product{
    productId;
    name;
    weight;
    description;

    constructor(productId, name, weight, description) {
        this.productId = productId;
        this.name = name;
        this.weight = weight;
        this.description = description;
    }

    static createSampleProduct(){
        const productId = "A12345";
        const name = "Solar Panel";
        const weight = "5,125";
        const description = "A solar panel to generate energy";

        return new Product(productId, name, weight, description)
    }
}
export default class Product {
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

    clone() {
        return new Product(this.id, this.name, this.weight, this.description);
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

    static createNewProduct(name, weight, description) {
        return new Product(-1, name, weight, description);
    }

    /**
     * put this product into the database
     * will add a new product to the database if no product exists
     * will override the existing product with the new product if the product already exists
     */
    async putDatabase() {
        try {
            const isNewProduct = this.id < 0;
            // TODO make a post request to the backend
            //  if the current product id is -1, receive the new product id
            if (isNewProduct) {
                this.id = 0;// receive the new product id
                Product.products.push(this);
            } else {
                Product.products[Product.products.findIndex(o => o.id === this.id)] = this;
            }
            return true;
        } catch (e) {
            return false;
        }
    }

    /**
     * delete this product from the database
     */
    async delDatabase() {
        try {
            const isNewProduct = this.id < 0;
            if (isNewProduct) {
                return false;
            }
            // TODO make a delete request to the backend
            Product.products = Product.products.filter(o => o.id !== this.id);
            return true;
        } catch (e) {
            return false;
        }
    }

    /**
     * get all the products from the database
     */
    static async getDatabase() {
        try {
            this.fetching = true;
            // TODO make a get request to the backend
            //  update "products" with the response
            return [Product.createNewProduct('Solar Panel', '5,125', 'A solar panel to generate energy'), Product.createNewProduct('Solar Panel', '4,125', 'A solar panel to generate energy'), Product.createNewProduct('Solar Panel', '51,125', 'A solar panel to generate a lot of energy')];
        } catch (e) {
            return [];
        } finally {
            this.fetching = false;
        }
    }

    static fetching = true;
    static products = [];
}
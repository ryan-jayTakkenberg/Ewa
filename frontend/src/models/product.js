import { getKey } from "../data";
import axios from "../axios-config";
import {classToObject} from "@/models/helper";

export default class Product {
    id;
    name;
    price;
    description;

    constructor(id, name, price, description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    clone() {
        return new Product(this.id, this.name, this.price, this.description);
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

    static createNewProduct(name, price, description) {
        return new Product(-1, name, price, description);
    }

    /**
     * put this product into the database
     * will add a new product to the database if no product exists
     * will override the existing product with the new product if the product already exists
     */
    async putDatabase() {
        try {
            const isNewProduct = this.id < 0;

            let response = await axios.post("/api/product", classToObject(this), {
                headers: {
                    "Authorization": getKey(),
                    'Content-Type': 'application/json'
                }
            });

            // make a post request to the backend
            // if the current product id is -1, receive the new product id
            if (isNewProduct) {
                this.id = response.data.id;// receive the new product id
                Product.products.push(this);
            } else {
                Product.products[Product.products.findIndex(o => o.id === this.id)] = this;
            }
            this.injectAttributes(response.data);
            return this;
        } catch (e) {
            return null;
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

            // make a delete request to the backend
            await axios.delete(`/api/product/${this.id}`, {
                headers: {
                    "Authorization": getKey()
                }
            });

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
            // make a get request to the backend
            // update "products" with the response
            let response = await axios.get("/api/product", {
                headers: {
                    "Authorization": getKey()
                }
            });

            let products = [];
            for (let obj of response.data) {
                let product = new Product();
                if (product.injectAttributes(obj)) {
                    products.push(product);
                }
            }
            return products;
        } catch (e) {
            return [];
        } finally {
            this.fetching = false;
        }
    }

    static fetching = true;
    static products = [];
}
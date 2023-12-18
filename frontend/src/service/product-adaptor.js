import {deleteAPI, getAPI, postAPI, putAPI, responseOk} from "@/backend";
import Product from "@/models/product";
import Warehouse from "@/models/warehouse";
import Orders from "@/models/order";
import Order from "@/models/order";

export class ProductAdaptor {

    static ENDPOINT = "/api/product";

    async fetchAll() {
        try {
            let products = [];
            // make a get request to the backend
            // update "products" with the response
            let response = await getAPI(ProductAdaptor.ENDPOINT);
            if (!responseOk(response)) {
                return products;
            }

            for (let obj of response.data) {
                let product = new Product();
                if (product.injectAttributes(obj)) {
                    products.push(product);
                }
            }
            return products;
        } catch (e) {
            return [];
        }
    }

    async fetchById(id) {
        try {
            const response = await getAPI(`${ProductAdaptor.ENDPOINT}/${id}`);
            if (!responseOk(response)) {
                return response;
            }

            return response.data;
        } catch (error) {
            return {};
        }
    }

    async editOrUpdate(product) {
        try {
            const response = await putAPI(ProductAdaptor.ENDPOINT, product);
            if (!responseOk(response)) {
                return response;
            }

            // Update the clientside product list
            this.updateRelations().catch(r => console.error(r));
            product.injectAttributes(response.data);
            Product.products[Product.products.findIndex(o => o.id === product.id)] = product;

            return response.data;
        } catch (error) {
            return {};
        }
    }

    async addOrCreate(product) {
        try {
            console.log(product);
            const response = await postAPI(ProductAdaptor.ENDPOINT, product);
            if (!responseOk(response)) {
                return response;
            }

            // Update the clientside product list
            product.injectAttributes(response.data);
            Product.products.push(product);

            return response.data;
        } catch (error) {
            console.log(error);
            return {};
        }
    }

    async deleteOrRemove(id) {
        try {
            const response = await deleteAPI(`${ProductAdaptor.ENDPOINT}/${id}`);
            if (!responseOk(response)) {
                return response;
            }

            // Update the clientside product list
            this.updateRelations().catch(r => console.error(r));
            Product.products = Product.products.filter(o => o.id !== id);

            return response.data;
        } catch (error) {
            return {};
        }
    }

    async updateRelations() {
        const [warehouses, orders] = await Promise.all([
            Warehouse.getDatabase(),
            Orders.getDatabase(),
        ]);
        Warehouse.warehouses = warehouses;
        Order.orders = orders;
    }

}
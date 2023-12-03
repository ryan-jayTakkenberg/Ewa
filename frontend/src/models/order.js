import {getJWT} from "@/data";
import axios from "../axios-config";
import {classToObject} from "@/models/helper";

export default class Order {
    id;
    orderNumber;
    orderedFrom;
    orderDate;
    estimatedDeliveryDate;
    teamId;
    productId;
    quantity;
    status;

    static Status = {
        PENDING: "PENDING",
        DELIVERED: "DELIVERED",
        CANCELED: "CANCELED",
    }

    constructor(id, orderNumber, orderedFrom, orderDate, estimatedDeliveryDate, teamId, productId, quantity, status) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.orderedFrom = orderedFrom;
        this.orderDate = orderDate;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.teamId = teamId;
        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
    }

    clone() {
        return new Order(this.id, this.orderNumber, this.orderDate, this.teamId, this.productId, this.quantity, this.status);
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

    static createNewOrder(orderNumber, orderDate, estimatedDeliveryDate, teamId, productId, quantity, status) {
        return new Order(-1, orderNumber, orderDate, estimatedDeliveryDate, teamId, productId, quantity, status);
    }

    /**
     * put this order into the database
     * will add a new order to the database if no order exists
     * will override the existing order with the new order if the order already exists
     */
    async putDatabase() {
        try {
            const isNewOrder = this.id < 0;

            let response = await axios.post(
                "/api/orders", classToObject(this),
                {headers: {"Authorization": getJWT()}}
            );

            // make a post request to the backend
            // if the current order id is -1, receive the new order id
            if (isNewOrder) {
                this.id = response.data.id;// receive the new order id
                Order.orders.push(this);
            } else {
                Order.orders[Order.orders.findIndex(o => o.id === this.id)] = this;
            }
            this.injectAttributes(response.data);
            return this;
        } catch (e) {
            return null;
        }
    }

    /**
     * delete this order from the database
     */
    async delDatabase() {
        try {
            const isNewProduct = this.id < 0;
            if (isNewProduct) {
                return false;
            }

            // make a delete request to the backend
            await axios.delete(
                `/api/orders/${this.id}`,
                {headers: {"Authorization": getJWT()}}
            );
            Order.orders = Order.orders.filter(o => o.id !== this.id);
            return true;
        } catch (e) {
            return false;
        }
    }

    /**
     * get all the orders from the database
     */
    static async getDatabase() {
        try {
            this.fetching = true;
            // make a get request to the backend
            // update "orders" with the response
            let response = await axios.get(
                "/api/orders",
                {headers: {"Authorization": getJWT()}}
            );

            let orders = [];
            for (let obj of response.data) {
                let order = new Order();
                if (order.injectAttributes(obj)) {
                    orders.push(order);
                }
            }
            return orders;
        } catch (e) {
            return [];
        } finally {
            this.fetching = false;
        }
    }

    static fetching = true;
    static orders = [];
}

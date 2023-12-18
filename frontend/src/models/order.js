import {getJWT} from "@/data";
import axios from "../axios-config";
import {classToObject} from "@/models/helper";
import {postAPI, putAPI, responseOk} from "@/backend";

export default class Order {
    id;
    name;
    orderedFrom;
    orderDate;
    estimatedDeliveryDate;
    team;
    products;
    status;
    totalPrice;

    static Status = {
        PENDING: "PENDING",
        DELIVERED: "DELIVERED",
        CANCELED: "CANCELED",
    }

    constructor(id, name, orderedFrom, orderDate, estimatedDeliveryDate, team, products, status, totalPrice) {
        this.id = id;
        this.name = name;
        this.orderedFrom = orderedFrom;
        this.orderDate = orderDate;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.team = team;
        this.products = products;
        this.status = status;
        this.totalPrice = totalPrice
    }

    clone() {
        return new Order(this.id, this.name, this.orderedFrom, this.orderDate, this.estimatedDeliveryDate, this.team, this.products, this.status, this.totalPrice);
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

    // create a new order with a temporary id indicating a new order
    static createNewOrder(name, orderDate, estimatedDeliveryDate, team, products, status, totalPrice) {
        return new Order(-1, name, orderDate, estimatedDeliveryDate, team, products, status, totalPrice);
    }

    /**
     * Edit this order in the database
     */
    async putDatabase() {
        try {
            // Check if is not a new user
            const isNewOrder = this.id < 0;
            if (isNewOrder) return false;
            // make a put request to the backend
            let response = await putAPI(`/api/orders/${this.id}`, classToObject(this));
            if (!responseOk(response)) return false;

            Order.orders[Order.orders.findIndex(o => o.id === this.id)] = this;
            this.injectAttributes(response.data);
            return this;
        } catch (e) {
            return null;
        }
    }

    /**
     * Create this new order in the database
     * Will create a new order in the database
     */
    async postDatabase() {
        try {
            // Check if order is indeed a new order
            const isNewOrder = this.id < 0;
            if (!isNewOrder) return false;
            // Make a post request to the backend
            let response = await postAPI('/api/orders', classToObject(this));
            if (!responseOk(response)) return false;

            // Receive the new order id
            this.id = response.data.id;

            // Update the local orders array with the new order
            Order.orders.push(this);

            this.injectAttributes(response.data);
            return this;
        } catch (e) {
            return null;
        }
    }

    /** confirm this order in the database
     */
    async confirmDatabase() {
        try {
            const isNewOrder = this.id < 0;
            if (isNewOrder) return false;
            // make a post request to the backend to confirm order
            await axios.post(
                `/api/orders/${this.id}/confirm`,
                {headers: {"Authorization": getJWT()}});
            return true;
        } catch (e) {
            return false;
        }
    }

    /** cancel this order in the database
     */
    async cancelDatabase() {
        try {
            const isNewOrder = this.id < 0;
            if (isNewOrder) return false;
            // make a post request to the backend to confirm order
            await axios.post(
                `/api/orders/${this.id}/cancel`,
                {headers: {"Authorization": getJWT()}});
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
            // Make a get request to the backend
            // Update "orders" with the response
            let response = await axios.get("/api/orders",
                {headers: {"Authorization": getJWT()}});

            let orders = [];
            for (let obj of response.data) {
                let order = new Order();
                if (order.injectAttributes(obj)) orders.push(order);
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

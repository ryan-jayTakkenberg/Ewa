import Order from "@/models/order";
import {deleteAPI, getAPI, postAPI, putAPI, responseOk} from "@/backend";
import {classToObject} from "@/models/helper";
import User from "@/models/user";

export class OrderAdaptor {
    static ENDPOINT = "/api/orders";

    async fetchAll() {
        try {
            const response = await getAPI(OrderAdaptor.ENDPOINT);
            if (!responseOk(response)) {
                console.warn('Response not OK:', response.data);
                return [];
            }
            return response.data.map(data => new Order(data.id,  data.name, data.orderedFrom, data.orderDate,
                data.estimatedDeliveryDate, data.team, data.orderedProducts, data.status, data.totalPrice));

        } catch (error) {
            return [];
        }
    }
    async asyncCreate(order) {
        try {
            const response = await postAPI(OrderAdaptor.ENDPOINT, classToObject(order));

            if (!responseOk(response)) {
                console.error(response.data);
                return {};
            }

            // Update the clientside order list
            order.injectAttributes(response.data);
            Order.orders.push(order);

            return response.data;
        } catch (error) {
            console.log(error);
            return {};
        }
    }
    async asyncUpdate(order) {
        console.log(order);
        try {
            const response = await putAPI(OrderAdaptor.ENDPOINT, order);
            if (!responseOk(response)) {
                console.error(response.data);
                return {};
            }
            const data = response.data;
            // Update the clientside order list
            order.injectAttributes(data);
            Order.orders[Order.orders.findIndex(o => o.id === order.id)] = order;

            return new Order(data.id,  data.name, data.orderedFrom, data.orderDate, data.estimatedDeliveryDate,
                data.team, data.orderedProducts, data.status, data.totalPrice);

        } catch (error) {
            return {};
        }
    }

    async asyncDeleteById(id) {
        try {
            const response = await deleteAPI(`${OrderAdaptor.ENDPOINT}/${id}/cancel`);
            if (!responseOk(response)) return response;

            // Update the clientside order list
            Order.orders = Order.orders.filter(o => o.id !== id);

            return response.data;
        } catch (error) {
            return {};
        }
    }
    async asyncConfirmById(id) {
        try {
            const response = await putAPI(`${OrderAdaptor.ENDPOINT}/${id}/confirm`);
            const data = response.data;
            if (!responseOk(response)) {
                console.error(data);
                return {};
            }
            // Update the clientside order list
            let index = Order.orders.findIndex(o => o.id === id);
            let orderClass = Order.orders[index];
            if (orderClass) {
                orderClass.injectAttributes(data);
                Order.orders[index] = orderClass;
                return orderClass;
            }

            return data;
        } catch (error) {
            return {};
        }
    }

    async asyncCancelById(id) {
        try {
            const response = await putAPI(`${OrderAdaptor.ENDPOINT}/${id}/cancel`);
            const data = response.data;
            if (!responseOk(response)) {
                console.error(data);
                return {};
            }

            // Update the clientside order list
            let index = Order.orders.findIndex(o => o.id === id);
            let orderClass = Order.orders[index];
            if (orderClass) {
                orderClass.injectAttributes(data);
                Order.orders[index] = orderClass;
                return orderClass;
            }

            return data;
        } catch (error) {
            return {};
        }
    }

}

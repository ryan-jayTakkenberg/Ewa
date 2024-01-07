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
    async asyncCreate(orderJson) {
        try {
            const response = await postAPI(OrderAdaptor.ENDPOINT, orderJson);

            if (!responseOk(response)) {
                console.error(response.data);
                return {};
            }

            // Update the clientside order list
            let orderClass = new Order();
            orderClass.injectAttributes(response.data);
            Order.orders.push(orderClass);

            return response.data;
        } catch (error) {
            console.log(error);
            return {};
        }
    }
    async asyncUpdate(orderJson) {
        try {
            const response = await putAPI(OrderAdaptor.ENDPOINT, orderJson);
            if (!responseOk(response)) {
                console.error(response.data);
                return {};
            }

            let index = Order.orders.findIndex(o => o.id === orderJson.orderId);
            let order = Order.orders[index];

            // Update the clientside order list
            order.injectAttributes(response.data);

            Order.orders[index] = order;

            return response.data;
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

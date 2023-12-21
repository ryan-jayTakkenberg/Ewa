import {deleteAPI, getAPI, postAPI, putAPI, responseOk} from "@/backend";
import {classToObject} from "@/models/helper";
import Order from "@/models/order";

export class OrderAdaptor {

    static ENDPOINT = "/api/orders";

    async fetchAll() {
        try {
            const response = await getAPI(OrderAdaptor.ENDPOINT);
            if (!responseOk(response)) {
                console.error(response.data);
                return [];
            }
            return response.data
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

            // Update the clientside order list
            order.injectAttributes(response.data);
            Order.orders[Order.orders.findIndex(o => o.id === order.id)] = order;

            return response.data;
        } catch (error) {
            return {};
        }
    }

    // async asyncDeleteById(id) {
    //     try {
    //         const response = await deleteAPI(`${OrderAdaptor.ENDPOINT}/${id}`);
    //         if (!responseOk(response)) return response;
    //
    //         // Update the clientside order list
    //         Order.orders = Order.orders.filter(o => o.id !== id);
    //
    //         return response.data;
    //     } catch (error) {
    //         return {};
    //     }
    // }

    async asyncConfirmOrder(order) {
        try {
            const response = await deleteAPI(`${OrderAdaptor.ENDPOINT}/confirm`);

            if (!responseOk(response)) {
                console.error(response.data);
                return {};
            }

            // Update the clientside order list

            return response.data;
        } catch (error) {
            return {};
        }
    }

    async asyncCancelOrder(id) {
        try {
            const response = await postAPI(`${OrderAdaptor.ENDPOINT}/${id}/delete`);
            if (!responseOk(response)) return response;


            // Update the clientside order list

            return response.data;
        } catch (error) {
            return {};
        }
    }

}

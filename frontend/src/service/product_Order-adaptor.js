import {deleteAPI, getAPI, postAPI, putAPI, responseOk} from "@/backend";
import Order from "@/models/order";
import User from "@/models/user";
export class Product_OrderAdaptor {

    static ENDPOINT = "/api/product_order";
    async asyncCreate(product_order) {
        try {
            console.log(product_order);
            const response = await postAPI(Product_OrderAdaptor.ENDPOINT, product_order);
            if (!responseOk(response)) return response;

            // Update the clientside order list
            const orderId = response.data.order.id;
            const updatedOrder = await this.fetchOrder(orderId);

            let index = Order.orders.findIndex(o => o.id === orderId);
            let orderClass = Order.orders[index];
            if (orderClass) {
                orderClass.injectAttributes(updatedOrder);
                Order.orders[index] = orderClass;
                return orderClass;
            }

            return response.data;
        } catch (error) {
            console.log(error);
            return {};
        }
    }

    async fetchOrder(orderId) {
        try {
            const response = await getAPI(`/api/orders/${orderId}`);
            if (responseOk(response)) {
                return response.data;
            }
        } catch (error) {
            console.error(error);
            return null;
        }
    }
}

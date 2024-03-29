export default class Order {
    static orders = [];

    id;
    name;
    orderedFrom;
    orderDate;
    estimatedDeliveryDate;
    team;
    orderedProducts;
    status;
    totalPrice;

    static Status = {
        PENDING: "PENDING",
        DELIVERED: "DELIVERED",
        CANCELED: "CANCELED",
    }

    constructor(id, name, orderedFrom, orderDate, estimatedDeliveryDate, team, orderedProducts, status, totalPrice) {
        this.id = id;
        this.name = name;
        this.orderedFrom = orderedFrom;
        this.orderDate = orderDate;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.team = team;
        this.orderedProducts = orderedProducts;
        this.status = status;
        this.totalPrice = totalPrice
    }

    clone() {
        return new Order(this.id, this.name, this.orderedFrom, this.orderDate, this.estimatedDeliveryDate, this.team, this.orderedProducts, this.status, this.totalPrice);
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
    static createNewOrder(name, orderedFrom, orderDate, estimatedDeliveryDate, team, orderedProducts, status) {
        return new Order(null, name, orderedFrom, orderDate, estimatedDeliveryDate, team, orderedProducts, status);
    }
}

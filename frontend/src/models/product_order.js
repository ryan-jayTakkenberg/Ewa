export default class Product_Order {
    amount;
    productId;
    orderId;

    constructor(amount, productId, orderId) {
        this.amount = amount;
        this.productId = productId;
        this.orderId = orderId;
    }

    clone() {
        return new Product_Order(this.amount, this.productId, this.orderId);
    }

    injectAttributes(from) {
        if (!(from instanceof Object)) return false;

        for (let attr of Object.keys(this)) {
            if (from[attr] !== null && from[attr] !== undefined) this[attr] = from[attr];
        }
        return true;
    }

    equals(other) {
        if (!other) {
            return false;
        }
        for (let attr of Object.keys(this)) {
            if (typeof this[attr] !== typeof other[attr] || `${this[attr]}` !== `${other[attr]}`) return false;
        }
        return true;
    }
}

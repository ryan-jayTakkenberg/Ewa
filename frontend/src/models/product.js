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
            if (from[attr]) {
                this[attr] = from[attr];
            }
        }
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
            // TODO make a post request to the backend
            //  if the current product id is -1, receive the new product id
            if (isNewProduct) {
                this.id = 0;// receive the new product id
                Product.products.push(this);
            } else {
                Product.products[Product.products.findIndex(o => o.id === this.id)] = this;
            }
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
            // TODO make a delete request to the backend
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
            // TODO make a get request to the backend
            //  update "products" with the response
            return [new Product(1, "Solar panel", 412, "De beste Zonnepanelen"), new Product(2, "Rail voor zonnepanelen", 17.95, "Deze rail kunt u gebruiken voor het monteren van uw zonnepanelen op uw dak. De rail is 120 centimeter lang en kan daardoor gebruikt worden voor de montage van meerdere zonnepanelen tegelijk. Verder is de rail gemaakt van aluminium, wat een lange levensduur en bestendigheid tegen slecht weer en vocht garandeert. De rail werkt perfect samen met het andere aansluitmateriaal dat we aanbieden, zoals eindklemmen, aardingsklemmen en midden klemmen dankzij de uitsparingen in de rail. Deze is 25mm breed aan beide kanten. ")];
        } catch (e) {
            return [];
        } finally {
            this.fetching = false;
        }
    }

    static fetching = true;
    static products = [];
}
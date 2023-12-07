package app.models.relations;

import app.models.Order;
import app.models.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class Product_Order {

    @Id
    @GeneratedValue
    private long id;

    private long amount;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JsonIgnoreProperties({"product_orders"})
    private Product product;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JsonIgnoreProperties({"products"})
    private Order order;

    public Product_Order(long amount, Product product, Order order) {
        this.amount = amount;
        this.product = product;
        this.order = order;
    }

    public Product_Order() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

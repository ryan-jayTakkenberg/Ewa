package app.models.relations;

import app.models.Product;
import app.models.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class Product_Warehouse {

    @Id
    @GeneratedValue
    private long id;

    private long amount;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JsonIgnoreProperties({"products_warehouse"})
    private Product product;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JsonIgnoreProperties({"products"})
    private Warehouse warehouse;

    public Product_Warehouse(long amount, Product product, Warehouse warehouse) {
        this.amount = amount;
        this.product = product;
        this.warehouse = warehouse;
    }

    public Product_Warehouse() {

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

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}

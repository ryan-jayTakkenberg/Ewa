package app.models;

import app.models.relations.Product_Order;
import app.models.relations.Product_Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Product {

    /*
     * BACKEND: MODEL
     * Every model should have an @Entity annotation before the class.
     * Every model should also have an @Id annotation, every id must be unique!
     *
     * You can see all the tables in: 'localhost:8085/api/h2-console'
     * Remember to set the 'JDBC URL' to 'jdbc:h2:mem:testdb'
     */

    @Id
    @GeneratedValue
    private long id;

    private String name;
    private double price;
    private String description;

    @OneToMany(mappedBy = "product", cascade = { CascadeType.ALL })
    @JsonIgnoreProperties("product")
    private Set<Product_Warehouse> products_warehouse;

    @OneToMany(mappedBy = "product", cascade = { CascadeType.ALL })
    @JsonIgnoreProperties("product")
    private Set<Product_Order> product_orders;

    public Product(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Product() {

    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public Set<Product_Warehouse> getProducts_warehouse() {
        return products_warehouse;
    }

    @JsonIgnore
    public Set<Product_Order> getProduct_orders() {
        return product_orders;
    }
}

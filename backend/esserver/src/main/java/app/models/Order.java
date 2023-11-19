package app.models;

import jakarta.persistence.*;

@Entity
public class Order {

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
    private String body;
    private String date;

    // TODO add relation and mapping
    /*
    @ManyToMany
    @JoinTable(
       name = "order_product",
       joinColumns = @JoinColumn(name = "order_id"),
       inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;
    */

    private String productName;
    private int quantity;

    public Order(long id, String body, String date, String productName, int quantity) {
        this.id = id;
        this.body = body;
        this.date = date;
        this.productName = productName;
        this.quantity = quantity;
    }

    // TODO why do I need to add a second constructor when I add @Entity above the model?
    public Order() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
package app.models;

import app.models.relations.Product_Order;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "order_table")
public class Order {
    public enum OrderStatus {PENDING, DELIVERED, CANCELED}

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
    private String orderName;
    private String orderedFrom;
    private LocalDate orderDate;
    private LocalDate estimatedDeliveryDate;
    @ManyToOne()
    @JoinColumn(name = "team_id")
    private Team team;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"order"})
    private Set<Product_Order> products = new HashSet<>();

    public Order(long id, String orderName, String orderedFrom, LocalDate orderDate, LocalDate estimatedDeliveryDate, Team team, Set<Product_Order> products, OrderStatus status) {
        this.id = id;
        this.orderName = orderName;
        this.orderedFrom = orderedFrom;
        this.orderDate = orderDate;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.team = team;
        this.products = products;
        this.status = status;
    }

    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getOrderedFrom() {
        return orderedFrom;
    }

    public void setOrderedFrom(String orderedFrom) {
        this.orderedFrom = orderedFrom;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(LocalDate estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Set<Product_Order> getProducts() {
        return products;
    }

    public void addProduct(Product_Order product) {
        this.products.add(product);
    }
}

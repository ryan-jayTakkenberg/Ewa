package app.models;

import jakarta.persistence.*;

import java.time.LocalDate;


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
    private String orderedFrom;
    private LocalDate orderDate;
    private LocalDate estimatedDeliveryDate;
    private int teamId;
    private int productId;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // TODO add relation and mapping
    /*
    @ManyToMany
    @JoinTable(
       name = "order_product",
       joinColumns = @JoinColumn(name = "order_id"),
       inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;
    */
    public Order(long id, String orderedFrom, LocalDate orderDate, LocalDate estimatedDeliveryDate, int teamId, int productId, int quantity, OrderStatus status) {
        this.id = id;
        this.orderedFrom = orderedFrom;
        this.orderDate = orderDate;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.teamId = teamId;
        this.productId = productId;
        this.quantity = quantity;
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

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

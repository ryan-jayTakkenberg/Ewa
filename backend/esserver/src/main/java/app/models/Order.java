package app.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;


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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    protected Team team;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    public Order(long id, String orderedFrom, LocalDate orderDate, LocalDate estimatedDeliveryDate, Team team, List<Product> products, OrderStatus status) {
        this.id = id;
        this.orderedFrom = orderedFrom;
        this.orderDate = orderDate;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.team = team;
        this.products = products;
        this.status = status;
    }
    public Order() {}
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

}

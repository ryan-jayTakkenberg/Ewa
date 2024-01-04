package app.models;

import app.models.relations.Product_Order;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "order_table")
public class Order {
    public enum OrderStatus {PENDING, DELIVERED, CANCELED}

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String orderedFrom;
    private LocalDate orderDate;
    private LocalDate estimatedDeliveryDate;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Team team;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"order"})
    private Set<Product_Order> orderedProducts = new HashSet<>();
    private double totalPrice;

    public Order(String name, String orderedFrom, LocalDate orderDate, LocalDate estimatedDeliveryDate, Team team, Set<Product_Order> orderedProducts, OrderStatus status) {
        this.name = name;
        this.orderedFrom = orderedFrom;
        this.orderDate = orderDate;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.team = team;
        this.orderedProducts = orderedProducts;
        this.status = status;
    }

    public Order() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getOrderedFrom() {
        return orderedFrom;
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

    public void setOrderedFrom(String orderedFrom) {
        this.orderedFrom = orderedFrom;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Set<Product_Order> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(Set<Product_Order> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    public void addOrderedProduct(int amount, Product product) {
        Product_Order newProductOrder = new Product_Order(amount, product, this);
        this.orderedProducts.add(newProductOrder);
        updateTotalPrice();
    }

    public void removeOrderedProduct(Product_Order productOrder) {
        this.orderedProducts.remove(productOrder);
        productOrder.setOrder(null);
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double totalPrice = this.orderedProducts.stream()
                .mapToDouble(productOrder -> productOrder.getProduct().getPrice() * productOrder.getAmount())
                .sum();

        // Round the total price to two decimal places
        BigDecimal roundedTotalPrice = BigDecimal.valueOf(totalPrice).setScale(2, RoundingMode.HALF_UP);

        this.setTotalPrice(roundedTotalPrice.doubleValue());
    }
}

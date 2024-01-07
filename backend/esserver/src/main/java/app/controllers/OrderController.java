package app.controllers;

import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.exceptions.NotFoundException;
import app.jwt.JWToken;
import app.models.*;
import app.models.relations.Product_Order;
import app.models.relations.Product_Warehouse;
import app.repositories.*;
import app.util.JsonBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderJPARepository orderRepo;

    @Autowired
    private ProductJPARepository productRepo;

    @Autowired
    private TeamJPARepository teamRepo;

    @Autowired
    private UserJPARepository userRepo;

    @Autowired
    private Product_OrderJPARepository product_orderJPARepository;

    /**
     * Getting all the orders, if viewer will only return for assigned team warehouse
     *
     * @return list of orders
     */
    @GetMapping
    private List<Order> getAllOrders(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        if (jwtInfo.isAdmin()) return orderRepo.findAll();
        if (jwtInfo.isViewer()) {
            final long userId = jwtInfo.getId();
            User user = userRepo.findById(userId);
            if (user == null) throw new ForbiddenException("Viewer user not found");
            final long teamId = user.getTeam().getId();
            return orderRepo.findAllByWarehouseId(teamId);
        }
        throw new ForbiddenException("Invalid user permission level");
    }

    @GetMapping("/{id}")
    private Order getOrderById(@PathVariable long id) {
        return orderRepo.findById(id);
    }

    /**
     * Update an existing order in the database
     *
     * @param jwtInfo the json web token
     * @param json    the order to add
     * @return the order if it was updated successfully
     * @apiNote requires admin permission
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    private Order putOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody JsonNode json) {
        // Check if the user is admin
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to edit an order");
        }

        JsonBuilder jsonBuilder = JsonBuilder.parse(json);

        String name = jsonBuilder.getStringFromField("name");
        String orderedFrom = jsonBuilder.getStringFromField("orderedFrom");
        Order.OrderStatus status = Order.OrderStatus.valueOf(jsonBuilder.getStringFromField("status").toUpperCase());
        LocalDate orderDate = jsonBuilder.getDateFromField("orderDate");
        LocalDate estimatedDeliveryDate = jsonBuilder.getDateFromField("estimatedDeliveryDate");
        long orderId = jsonBuilder.getLongFromField("orderId");
        long teamId = jsonBuilder.getLongFromField("teamId");
        List<JsonBuilder> productJsonBuilders = jsonBuilder.getArrayFromField("products");

        Order order = orderRepo.findById(orderId);
        if (order == null) {
            System.err.println("Order not found for id: " + orderId);
            throw new NotFoundException("Order not found for id: " + orderId);
        }

        Team team = teamRepo.findById(teamId);
        if (team == null) {
            System.err.println("Team not found for id: " + teamId);
            throw new NotFoundException("Team not found for id: " + teamId);
        }

        Set<Product_Order> ordered_products = new HashSet<>();
        for (JsonBuilder productJsonBuilder : productJsonBuilders) {

            long amount = productJsonBuilder.getLongFromField("amount");
            long productId = productJsonBuilder.getLongFromField("productId");

            Product product = productRepo.findById(productId);
            if (product == null) {
                System.err.println("Product not found for id: " + productId);
                throw new NotFoundException("Product not found for id: " + productId);
            }

            Product_Order product_order = new Product_Order(amount, product, order);
            ordered_products.add(product_order);
        }

        order.setName(name);
        order.setOrderedFrom(orderedFrom);
        order.setStatus(status);
        order.setOrderDate(orderDate);
        order.setEstimatedDeliveryDate(estimatedDeliveryDate);
        order.setTeam(team);

        new HashSet<>(order.getOrderedProducts()).forEach(product_order -> {
            order.removeOrderedProduct(product_order);
            product_orderJPARepository.delete(product_order);
        });

        ordered_products.forEach(order::addOrderedProduct);

        return orderRepo.save(order);
    }


    /**
     * create a new order in the database with products
     *
     * @param jwtInfo the json web token
     * @param json    the new order to add
     * @return the new order if it was added successfully
     * @apiNote requires admin permission
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Order postOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo,
                            @RequestBody JsonNode json) {
        // Check if the jwt is provided
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        // Check if the user is admin
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to create an order");

        JsonBuilder jsonBuilder = JsonBuilder.parse(json);
        String name = jsonBuilder.getStringFromField("name");
        String orderedFrom = jsonBuilder.getStringFromField("orderedFrom");
        Order.OrderStatus status = Order.OrderStatus.valueOf(jsonBuilder.getStringFromField("status").toUpperCase());
        LocalDate orderDate = jsonBuilder.getDateFromField("orderDate");
        LocalDate estimatedDeliveryDate = jsonBuilder.getDateFromField("estimatedDeliveryDate");
        long teamId = jsonBuilder.getLongFromField("teamId");
        List<JsonBuilder> productJsonBuilders = jsonBuilder.getArrayFromField("products");

        Team team = teamRepo.findById(teamId);
        if (team == null) throw new NotFoundException("Team not found for id: " + teamId);

        // Extract ordered products information from the JSON
        Set<Product_Order> ordered_products = new HashSet<>();
        for (JsonBuilder productJsonBuilder : productJsonBuilders) {
            long amount = productJsonBuilder.getLongFromField("amount");
            long productId = productJsonBuilder.getLongFromField("productId");

            Product product = productRepo.findById(productId);
            if (product == null) {
                System.err.println("Product not found for id: " + productId);
                throw new NotFoundException("Product not found for id: " + productId);
            }

            Product_Order product_order = new Product_Order(amount, product, null);
            ordered_products.add(product_order);
        }

        // Create the new order with associated products
        Order order = new Order();
        order.setName(name);
        order.setOrderedFrom(orderedFrom);
        order.setOrderDate(orderDate);
        order.setEstimatedDeliveryDate(estimatedDeliveryDate);
        order.setTeam(team);
        order.setStatus(status);

        // Save the new order to obtain an ID
        Order newOrder = orderRepo.save(order);

        // Associate the ordered products with the new order
        ordered_products.forEach(productOrder -> productOrder.setOrder(newOrder));
        ordered_products.forEach(newOrder::addOrderedProduct);

        return orderRepo.save(newOrder);

    }

    /**
     * Confirms an order in the database
     *
     * @param jwtInfo the json web token
     * @param id      the order id
     * @return the confirmed order
     * @apiNote no permission requirements
     */
    @PutMapping("/{id}/confirm")
    @ResponseStatus(HttpStatus.OK)
    private Order confirmOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable Long id) {
        // Check if the jwt is provided
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        // Check if order is found
        Order order = orderRepo.findById(id);
        if (order == null) throw new BadRequestException("No order found with id: " + id);
        // Check if the order status is suitable for confirmation
        if (!order.getStatus().equals(Order.OrderStatus.PENDING))
            throw new ForbiddenException("Order cannot be confirmed. Invalid status.");

        // Update the order status to "DELIVERED"
        order.setStatus(Order.OrderStatus.DELIVERED);

        // Save the order to persist the status change
        order = orderRepo.save(order);

//        // Add products to Product_Warehouse
        // todo
//        Set<Product_Order> orderedProducts = order.getOrderedProducts();
//        for (Product_Order productOrder : orderedProducts) {
//            Product product = productOrder.getProduct();
//            Warehouse warehouse = order.getTeam().getWarehouse();
//
//            Product_Warehouse product_warehouse = new Product_Warehouse(productOrder.getAmount(), product, warehouse);
//            warehouse.addProduct(product_warehouse);
//
//            // Check if a product already exists for this product and warehouse
//            Product_Warehouse existingRecord = productWarehouseRepo.findByProductAndWarehouse(product, warehouse);
//            if (existingRecord != null) {
//                // Update the existing product with the new quantity
//                existingRecord.setAmount(existingRecord.getAmount() + productOrder.getAmount());
//                productWarehouseRepo.save(existingRecord);
//            } else {
//                // Create a new product_warehouse if it doesn't exist
//                Product_Warehouse newRecord = new Product_Warehouse(productOrder.getAmount(), product, warehouse);
//                productWarehouseRepo.save(newRecord);
//            }
//        }

        return order;
    }

    /**
     * Cancel an order in the database
     *
     * @param jwtInfo the json web token
     * @param id      the order id
     * @return the confirmed order
     * @apiNote no permission requirements
     */
    @PutMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    private Order cancelOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo,
                              @PathVariable Long id) {
        // Check if the jwt is provided
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        // Check if order is found
        Order order = orderRepo.findById(id);
        if (order == null) throw new BadRequestException("No order found with id: " + id);
        // Check if the order status is suitable for cancellation
        if (!order.getStatus().equals(Order.OrderStatus.PENDING))
            throw new ForbiddenException("Order cannot be confirmed. Invalid status.");
        // Update the order status to "CANCELED"
        order.setStatus(Order.OrderStatus.CANCELED);

        return orderRepo.save(order);
    }

    /**
     * Deletes an order from the database
     *
     * @param jwtInfo the json web token
     * @param id      the order id
     * @return the deleted order
     * @apiNote requires admin permission
     */
    @DeleteMapping("/{id}")
    private Order deleteOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo,
                              @PathVariable Long id) {
        // Check if the jwt is provided
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        // Check if the user is admin
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to remove an order");
        // Check if id is not null
        if (id == null) throw new BadRequestException("No valid ID provided for order");
        // Find order by id
        Order order = orderRepo.findById(id);
        // Check if order exists
        if (order == null) throw new BadRequestException("No order found with id: " + id); // Check if order is found
        return orderRepo.delete(order);
    }
}

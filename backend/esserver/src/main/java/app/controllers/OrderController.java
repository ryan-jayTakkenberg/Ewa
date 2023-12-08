package app.controllers;

import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.jwt.JWToken;
import app.models.Order;
import app.repositories.OrderJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders") // TODO update correct path
public class OrderController {

    /*
     * PRODUCT CONTROLLER
     * This controller class manages the CRUD (Create, Read, Update, Delete) operations for the 'Order' entity.
     *
     * Endpoints:
     * - GET /orders: Retrieves a list of all orders that belong to the logged-in users team, or all orders for admin
     * - POST /orders: Creates a new order. Can only be done by admin
     * - PUT /orders/{id}: Updates the order by given ID, can be done by admin for modifying orders or viewer for
     *                     updating status of order to 'DELIVERED'.
     * - DELETE /orders/{id}: Removes an order by ID (when the order is completed). Can only be done by admin
     *
     * Authorization:
     * - All endpoints require a valid JWT token, and a ForbiddenException is thrown if no token is provided.
     * - Admin-level token is required for creating and deleting products; otherwise, a ForbiddenException is thrown.
     *
     * Error Handling:
     * - Throws ForbiddenException for authentication and authorization issues.
     * - Throws BadRequestException for invalid or missing parameters.
     *
     * Dependencies:
     * - Autowired 'ProductJPARepository' for database interaction.
     * - Utilizes 'JWToken' for extracting JWT information from the request attributes.
     *
     * Note:
     * - The controller assumes a REST structure and adheres to HTTP status codes.
     * - Ensure that the 'ProductJPARepository' is properly configured for database operations.
     * - POST can also be used to UPDATE (edit) the entity
     */

    @Autowired
    private OrderJPARepository orderRepo;

    @GetMapping
    private List<Order> getOrders(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {
        if (jwtInfo == null) throw new ForbiddenException("No token provided");

        if(jwtInfo.isAdmin()){
            return orderRepo.findAll();
        } else



        //Todo if user is viewer
//            Long teamId = jwtInfo.getTeamId();
//
//            if (teamId != null){
//                return orderRepo.findByTeamId(teamId);
//            } else {
//                throw new ForbiddenException("Team information not found in token")
//;              }
//        }
            throw new ForbiddenException("Team information not found in token");

    }

    @GetMapping("/{id}")
    private Order getOrderById(@PathVariable long id) {
        return orderRepo.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Order postOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody Order order) {
        // Check if the jwt is provided
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        // Check if the user is admin
        if (!jwtInfo.isAdmin())
            throw new ForbiddenException("Admin role is required to create a product"); // Check if the user is admin
        return orderRepo.save(order);
    }

    @DeleteMapping("/{id}")
    private Order deleteOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable Long id) {
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

    @PutMapping("/{id}/confirm")
    private Order confirmOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable Long id) {
        // Check if the jwt is provided
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        // Check if the user is viewer
        if (!jwtInfo.isViewer()) throw new ForbiddenException("Viewer role is required to confirm an order");
        Order order = orderRepo.findById(id);     // Find order by id
        // Check if order is found
        if (order == null) throw new BadRequestException("No order found with id: " + id);
        // Check if the order status is suitable for confirmation
        if (!order.getStatus().equals(Order.OrderStatus.PENDING)) {
            throw new ForbiddenException("Order cannot be confirmed. Invalid status.");
        }
        // Update the order status to "DELIVERED"
        order.setStatus(Order.OrderStatus.DELIVERED);
        return orderRepo.save(order);
    }

}

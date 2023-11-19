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
@RequestMapping("/order") // TODO update correct path
public class OrderController {

    /*
     * PRODUCT CONTROLLER
     * This controller class manages the CRUD (Create, Read, Update, Delete) operations for the 'Order' entity.
     *
     * Endpoints:
     * - GET /orders: Retrieves a list of all orders that belong to the logged-in user. Can only be done by admin
     * - POST /orders: Creates a new order. Can only be done by viewer
     * - DELETE /orders/{id}: Removes a order by ID (when the order is completed). Can only be done by admin
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

        if (jwtInfo == null) {
            throw new ForbiddenException("No token provided");
        }

        return orderRepo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Order postOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody Order order) {

        // TODO post order for viewer only

//        if (jwtInfo == null) {
//            throw new ForbiddenException("No token provided");
//        }
//        if (!jwtInfo.isAdmin()) {
//            throw new ForbiddenException("Admin role is required to create a product");
//        }

        return orderRepo.save(order);
    }

    @DeleteMapping("/{id}")
    private Order deleteOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable Long id) {

        if (jwtInfo == null) {
            throw new ForbiddenException("No token provided");
        }
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to remove a order");
        }

        if (id == null) {
            throw new BadRequestException("No valid ID provided for order");
        }

        Order order = orderRepo.findById(id);
        if (order == null) {
            throw new BadRequestException("No order found with that id");
        }

        return orderRepo.delete(order);
    }

}
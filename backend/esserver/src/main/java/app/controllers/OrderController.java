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

    @Autowired
    private OrderJPARepository orderRepo;

    /**
     * Getting all the orders, if viewer will only return for assigned team warehouse
     *
     * @return list of orders
     */
    @GetMapping
    private List<Order> getAllOrders(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {
        if (jwtInfo == null) throw new ForbiddenException("No token provided");

        if (jwtInfo.isAdmin()) {
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

    /**
     * Update an existing order in the database
     *
     * @param jwtInfo the json web token
     * @param order   the order to add
     * @return the order if it was updated successfully
     * @apiNote requires admin permission
     */
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Order putOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo,
                           @RequestBody Order order) {
        // Check if the jwt is provided
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        // Check if the user is admin
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to create an order");
        // Check if order is found
        Order existingOrder = orderRepo.findById(order.getId());
        if (existingOrder == null) throw new BadRequestException("No order found for ID" + order.getId());

        return orderRepo.save(order);
    }


    /**
     * create a new order in the database
     *
     * @param jwtInfo the json web token
     * @param order   the new order to add
     * @return the new order if it was added successfully
     * @apiNote requires admin permission
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Order postOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo,
                            @RequestBody Order order) {
        // Check if the jwt is provided
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        // Check if the user is admin
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to create an order");

        // Check if order is a new order
        if (orderRepo.findById(order.getId()) != null)
            throw new BadRequestException("Order already exists for id: " + order.getId());

        return orderRepo.save(order);
    }

// todo order confirmation

    /**
     * Confirms an order in the database
     *
     * @param jwtInfo the json web token
     * @param id      the order id
     * @return the confirmed order
     * @apiNote no permission requirements
     */
    @PutMapping("/{id}/confirm")
    private Order confirmOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo,
                               @PathVariable Long id) {
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

        return orderRepo.save(order);
    }

    // todo order cancellation

    /**
     * Cancel an order in the database
     *
     * @param jwtInfo the json web token
     * @param id      the order id
     * @return the confirmed order
     * @apiNote no permission requirements
     */
    @PutMapping("/{id}/cancel")
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

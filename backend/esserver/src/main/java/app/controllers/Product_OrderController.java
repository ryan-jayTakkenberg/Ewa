package app.controllers;

import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.exceptions.NotFoundException;
import app.jwt.JWToken;
import app.models.Order;
import app.models.Product;
import app.models.relations.Product_Order;
import app.repositories.OrderJPARepository;
import app.repositories.ProductJPARepository;
import app.repositories.Product_OrderJPARepository;
import app.util.JsonBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product_order")
public class Product_OrderController {
    private final Product_OrderJPARepository productOrderRepo;
    private final ProductJPARepository productRepo;
    private final OrderJPARepository orderRepo;

    public Product_OrderController(Product_OrderJPARepository productRepo, ProductJPARepository productRepo1, OrderJPARepository orderRepo) {
        this.productOrderRepo = productRepo;
        this.productRepo = productRepo1;
        this.orderRepo = orderRepo;
    }

    /**
     * Getting all the product_orders relations
     *
     * @return list of product_orders
     */
    @GetMapping
    private List<Product_Order> getProductOrders() {
        return productOrderRepo.findAll();
    }

    /**
     * Get a specific product_order
     *
     * @return The product_order for the specified id
     */
    @GetMapping("/{id}")
    private Product_Order getProductOrder(@PathVariable Long id) {
        if (id == null) throw new BadRequestException("Missing field: 'id'");
        Product_Order productOrder = productOrderRepo.findById(id);
        if (productOrder == null) throw new NotFoundException("Product_Order not found for id: " + id);

        return productOrder;
    }

    /**
     * Add a product_order relation to the database
     *
     * @param jwtInfo the json web token
     * @param json    the productOrder to add
     * @return the product_order if it was added successfully
     * @apiNote requires admin permission
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Product_Order postProductOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody JsonNode json) {
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to create a product_order");

        JsonBuilder jsonBuilder = JsonBuilder.parse(json);
        long productId = jsonBuilder.getLongFromField("productId");
        long orderId = jsonBuilder.getLongFromField("orderId");
        long amount = jsonBuilder.getLongFromField("amount");

        Product product = productRepo.findById(productId);
        if (product == null) throw new NotFoundException("No product found for id: " + productId);

        Order order = orderRepo.findById(orderId);
        if (order == null) throw new NotFoundException("No order found for id: " + orderId);
        Product_Order product_order = new Product_Order(amount, product, order);

        // Update order with the new ordered product
        order.addOrderedProduct(product_order);
        orderRepo.save(order);

        return productOrderRepo.save(product_order);
    }

    /**
     * Edit a product_order from the database wil create new one if not exists
     *
     * @param jwtInfo      the json web token
     * @param productOrder the product_order to add or edit
     * @return the product_order if it was edited successfully
     * @apiNote requires admin permission
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    private Product_Order putProductOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody Product_Order productOrder) {
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to edit or create a product order");

        Product product = productRepo.findById(productOrder.getProduct().getId());
        if (product == null) throw new NotFoundException("No product found for id: " + productOrder.getProduct().getId());

        return productOrderRepo.save(productOrder);
    }

    /**
     * Deletes a product_order relation from the database
     *
     * @param jwtInfo the json web token
     * @param id      the product_order id
     * @return the deleted product_order
     * @apiNote requires admin permission
     */
    @DeleteMapping("/{id}")
    private Product_Order deleteProductOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable Long id) {
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to remove a ordered product");
        if (id == null) throw new BadRequestException("No valid ID provided for oredered product");

        Product_Order productInfo = productOrderRepo.findById(id);
        if (productInfo == null) throw new BadRequestException("No product found for such ID");

        return productOrderRepo.delete(productInfo);
    }

}

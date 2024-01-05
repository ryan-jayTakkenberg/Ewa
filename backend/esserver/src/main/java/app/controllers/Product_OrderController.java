package app.controllers;

import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.exceptions.NotFoundException;
import app.jwt.JWToken;
import app.models.relations.Product_Order;
import app.repositories.Product_OrderJPARepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product_order")
public class Product_OrderController {
    private final Product_OrderJPARepository productOrderRepo;
    public Product_OrderController(Product_OrderJPARepository productRepo) {
        this.productOrderRepo = productRepo;
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
     * @param jwtInfo      the json web token
     * @param productOrder the productOrder to add
     * @return the product_order if it was added successfully
     * @apiNote requires admin permission
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Product_Order postProductOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody Product_Order productOrder) {
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to create a product_order");

        if (productOrderRepo.findById(productOrder.getId()) != null)
            throw new BadRequestException("Product already exists for id: " + productOrder.getId());

        return productOrderRepo.save(productOrder);
    }

    /**
     * Edit a product_order from the database
     *
     * @param jwtInfo      the json web token
     * @param productOrder the product_order to add or edit
     * @return the product_order if it was edited successfully
     * @apiNote requires admin permission
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    private Product_Order putProductOrder(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody Product_Order productOrder) {
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to edit a product");
        if (productOrderRepo.findById(productOrder.getId()) == null)
            throw new NotFoundException("No ordered product found for id: " + productOrder.getId());

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
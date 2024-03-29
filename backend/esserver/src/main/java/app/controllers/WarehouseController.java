package app.controllers;

import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.exceptions.NotFoundException;
import app.jwt.JWToken;
import app.models.relations.Product_Warehouse;
import app.models.Product;
import app.models.Warehouse;
import app.repositories.ProductJPARepository;
import app.repositories.WarehouseJPARepository;
import app.util.JsonBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

    private final WarehouseJPARepository warehouseRepository;
    private final ProductJPARepository productRepository;

    public WarehouseController(WarehouseJPARepository warehouseRepository, ProductJPARepository productRepository) {
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
    }
    @GetMapping("/{id}")
    public Warehouse getWarehouseById(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable long id) {

        Warehouse warehouse = warehouseRepository.findById(id);


        if (warehouse != null) {
            return warehouse;
        } else {
            throw new NotFoundException("Team not found with ID: " + id);
        }
    }

    @GetMapping
    private List<Warehouse> getWarehouses(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo){

        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        return warehouseRepository.findAll();
    }

    @PostMapping
    public Warehouse addNewWarehouse(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody Warehouse warehouse) {
        if (!jwtInfo.isAdmin()){
            throw new ForbiddenException("Admin role is required to create a warehouse");
        }

        return warehouseRepository.save(warehouse);
    }

    @PutMapping("/{id}")
    public Warehouse updateWarehouse(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo,@PathVariable long id, @RequestBody JsonNode json) {
        Warehouse existingWarehouse = warehouseRepository.findById(id);
        if (!jwtInfo.isAdmin()){
            throw new ForbiddenException("Admin role is required to alter a warehouse");
        }

        JsonBuilder jsonBuilder = JsonBuilder.parse(json);
        int warehouseId = jsonBuilder.getIntFromField("id");
        String name = jsonBuilder.getStringFromField("name");
        String address = jsonBuilder.getStringFromField("address");
        String city = jsonBuilder.getStringFromField("city");
        String postalCode = jsonBuilder.getStringFromField("postalCode");
        int maxStorage = jsonBuilder.getIntFromField("maxStorage");
        int minStorage = jsonBuilder.getIntFromField("minStorage");
        int currentStorage = jsonBuilder.getIntFromField("currentStorage");

        if (existingWarehouse == null) {
            throw new NotFoundException("Warehouse not found with ID: " + id);
        }

        if (id != warehouseId) {
            throw new BadRequestException("ID in path does not match ID in request.");
        }

        Warehouse updatedWarehouse = new Warehouse(warehouseId, name, address, city, postalCode, maxStorage, minStorage, currentStorage);
        return warehouseRepository.save(updatedWarehouse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Warehouse> deleteWarehouse(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo,@PathVariable long id){
        Warehouse warehouseToDelete = warehouseRepository.findById(id);
        if (!jwtInfo.isAdmin()){
            throw new ForbiddenException("Admin role is required to delete a warehouse.");
        }
        if (warehouseToDelete != null){
            warehouseRepository.delete(warehouseToDelete);
            return ResponseEntity.ok(warehouseToDelete);
        }

        return null;
    }

    /*
    From here on: Products in warehouse
     */

    @PostMapping("/product")
    public Warehouse addProductToWarehouse(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody JsonNode json) {
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to add a product to a warehouse");
        }

        JsonBuilder jsonBuilder = JsonBuilder.parse(json);

        long amount = jsonBuilder.getLongFromField("amount");
        long productId = jsonBuilder.getLongFromField("productId");
        long warehouseId = jsonBuilder.getLongFromField("warehouseId");

        Warehouse warehouse = warehouseRepository.findById(warehouseId);
        if (warehouse == null) {
            throw new NotFoundException("No valid warehouse found for such warehouseId");
        }

        Product productInfo = productRepository.findById(productId);
        if (productInfo == null) {
            throw new NotFoundException("No valid product found for such productInfo");
        }

        Product_Warehouse product = new Product_Warehouse(amount, productInfo, warehouse);
        warehouse.addProduct(product);

        return warehouseRepository.save(warehouse);
    }

    @PostMapping("/updateProducts")
    public ResponseEntity<?> updateProductQuantities(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody JsonNode json) {
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to update product quantities in a warehouse");
        }

        JsonBuilder jsonBuilder = JsonBuilder.parse(json);
        long warehouseId = jsonBuilder.getLongFromField("warehouseId");
        JsonNode productsNode = json.get("products"); // An array of products and their used amounts

        Warehouse warehouse = warehouseRepository.findById(warehouseId);
        if (warehouse == null) {
            throw new NotFoundException("No valid warehouse found for warehouseId: " + warehouseId);
        }

        if (productsNode.isArray()) {
            for (JsonNode productNode : productsNode) {
                long productId = productNode.get("productId").asLong();
                long amountUsed = productNode.get("amountUsed").asLong();

                // Find the product in the warehouse
                Product_Warehouse productInWarehouse = warehouse.getProducts().stream()
                        .filter(p -> p.getProduct().getId() == productId)
                        .findFirst()
                        .orElseThrow(() -> new NotFoundException("Product not found in warehouse: " + productId));

                // Check if enough stock is available and reduce the stock
                if (productInWarehouse.getAmount() < amountUsed) {
                    throw new BadRequestException("Not enough stock for product: " + productId);
                }
                productInWarehouse.setAmount(productInWarehouse.getAmount() - amountUsed);
            }
        }

        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);
        return ResponseEntity.ok(updatedWarehouse);
    }

}

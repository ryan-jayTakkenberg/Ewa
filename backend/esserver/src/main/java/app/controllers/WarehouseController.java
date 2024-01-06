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

    @GetMapping
    private List<Warehouse> getWarehouses(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo){

        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        return warehouseRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Warehouse> addNewWarehouse(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo,@RequestBody Warehouse warehouse){
        if (!jwtInfo.isAdmin()){
            throw new ForbiddenException("Admin role is required to create a warehouse");
        }

        if (warehouse == null){
            return ResponseEntity.badRequest().build();
        }

        Warehouse addedWarehouse = warehouseRepository.save(warehouse);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedWarehouse.getId())
                .toUri();

        return ResponseEntity.created(location).body(addedWarehouse);
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
}

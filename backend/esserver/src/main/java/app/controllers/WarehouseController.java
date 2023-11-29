package app.controllers;

import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.exceptions.NotFoundException;
import app.jwt.JWToken;
import app.models.Warehouse;
import app.repositories.WarehouseJPARepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

    private final WarehouseJPARepository warehouseRepository;

    public WarehouseController(WarehouseJPARepository warehouseRepository){
        this.warehouseRepository = warehouseRepository;
    }

    @GetMapping
    private List<Warehouse> getWarehouses(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo){
        if (jwtInfo.isAdmin() || jwtInfo.isViewer()){
            return warehouseRepository.findAll();
        }

        return List.of(warehouseRepository.findById(jwtInfo.getId()));
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
    public ResponseEntity<Warehouse> updateWarehouse(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo,@PathVariable long id, @RequestBody Warehouse updatedWarehouse){
        Warehouse existingWarehouse = warehouseRepository.findById(id);
        if (!jwtInfo.isAdmin()){
            throw new ForbiddenException("Admin role is required to alter a warehouse");
        }

        if (existingWarehouse != null){
            if (id != updatedWarehouse.getId()){
                throw new BadRequestException("ID in path does not match ID in request.");
            }

            updatedWarehouse.setId((int) id);
            Warehouse savedWarehouse = warehouseRepository.save(updatedWarehouse);
            return ResponseEntity.ok(savedWarehouse);
        } else {
            throw new NotFoundException("Warehouse not found witd ID: " + id);
        }
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
}

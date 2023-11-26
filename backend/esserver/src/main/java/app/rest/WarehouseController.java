//package app.rest;
//
//import app.exceptions.BadRequestException;
//import app.exceptions.NotFoundException;
//import app.models.Warehouse;
//import app.repository.WarehouseRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.net.URI;
//import java.util.List;
//
//@RestController
//@RequestMapping("/warehouses")
//public class WarehouseController {
//
//    private final WarehouseRepository warehouseRepository;
//
//    @GetMapping("/test")
//    public List<Warehouse> getTestWarehouses(){
//        return List.of(
//            new Warehouse("test-warehouse-A"),
//            new Warehouse("test-warehouse-B")
//        );
//    }
//
//    @Autowired
//    public WarehouseController(WarehouseRepository warehouseRepository){
//        this.warehouseRepository = warehouseRepository;
//    }
//
//    @GetMapping("")
//    public List<Warehouse> getAllWarehouses(){
//        return warehouseRepository.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Warehouse> getById(@PathVariable long id){
//        Warehouse warehouse = warehouseRepository.findById(id);
//        if (warehouse != null) {
//            return ResponseEntity.ok(warehouse);
//        } else {
//            throw new NotFoundException("Warehouse not found with ID: " + id);
//        }
//    }
//
//    @PostMapping("")
//    public ResponseEntity<Warehouse> addNewWarehouse(@RequestBody Warehouse warehouse ){
//        if (warehouse == null) {
//            return ResponseEntity.badRequest().build();
//        }
//        Warehouse addedWarehouse = warehouseRepository.save(warehouse);
//
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(addedWarehouse.getId())
//                .toUri();
//
//        return ResponseEntity.created(location).body(addedWarehouse);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Warehouse> updateWarehouse(@PathVariable long id, @RequestBody Warehouse updatedWarehouse) {
//        Warehouse existingWarehouse = warehouseRepository.findById(id);
//        if (existingWarehouse != null) {
//            if (id != updatedWarehouse.getId()) {
//                throw new BadRequestException("ID in path does not match ID in request.");
//            }
//
//            updatedWarehouse.setId((int) id);
//            Warehouse savedWarehouse = warehouseRepository.save(updatedWarehouse);
//            return ResponseEntity.ok(savedWarehouse);
//        } else {
//            throw new NotFoundException("Warehouse not found with ID: " + id);
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Warehouse> deleteWarehouse(@PathVariable long id) {
//        Warehouse warehouseToDelete = warehouseRepository.findById(id);
//        if (warehouseToDelete != null) {
//            warehouseRepository.delete(id);
//            return ResponseEntity.ok(warehouseToDelete);
//        } else {
//            throw new NotFoundException("Warehouse not found with ID: " + id);
//        }
//    }
//}

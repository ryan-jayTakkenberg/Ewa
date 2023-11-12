package app.Rest;

import app.exceptions.NotFoundException;
import app.models.Warehouse;
import app.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

    private final WarehouseRepository warehouseRepository;

    @GetMapping("/test")
    public List<Warehouse> getTestWarehouses(){
        return List.of(
            new Warehouse("test-warehouse-A"),
            new Warehouse("test-warehouse-B")
        );
    }

    @Autowired
    public WarehouseController(WarehouseRepository warehouseRepository){
        this.warehouseRepository = warehouseRepository;
    }

    @GetMapping("")
    public List<Warehouse> getAllWarehouses(){
        return warehouseRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getById(@PathVariable int id){
        Warehouse warehouse = warehouseRepository.findById((long) id);
        if (warehouse != null) {
            return ResponseEntity.ok(warehouse);
        } else {
            throw new NotFoundException("Offer not found with ID: " + id);
        }
    }

}

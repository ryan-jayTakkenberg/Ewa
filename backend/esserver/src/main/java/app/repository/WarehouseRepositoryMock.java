package app.repository;

import app.models.Teams;
import app.models.Warehouse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WarehouseRepositoryMock implements WarehouseRepository{

    private final List<Warehouse> warehouses = new ArrayList<>();

    public static int warehouseIDCount = 8;

    public WarehouseRepositoryMock(){
        for (int i = 0; i < 7; i++){
            warehouses.add(Warehouse.createSampleOffer(i));
        }
    }

    @Override
    public List<Warehouse> findAll() {
        return warehouses;
    }

    @Override
    public Warehouse findById(Long id) {
        for (Warehouse warehouse: warehouses){
            if (warehouse.getId() == id){
                return warehouse;
            }
        }
        return null;
    }

    @Override
    public Warehouse save(Warehouse warehouse) {
        if (warehouse.getId() == 0) {
            warehouse.setId(warehouseIDCount++);
            warehouses.add(warehouse);
        } else {
            for (int i = 0; i < warehouses.size(); i++) {
                if (warehouses.get(i).getId() == warehouse.getId()) {
                    warehouses.set(i, warehouse); // Replace the existing offer with the updated one
                    break;
                }
            }
        }
        return warehouse;
    }

    @Override
    public Warehouse delete(Long id) {
        for (Warehouse warehouse: warehouses){
            if(warehouse.getId() == id){
                warehouses.remove(warehouse);
                break;
            }

        }
        return null;
    }
}

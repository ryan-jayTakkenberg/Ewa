package app.repository;

import app.models.Teams;
import app.models.Warehouse;
import java.util.List;

public interface WarehouseRepository {

    List<Warehouse> findAll();
    Warehouse findById(Long id);
    Warehouse save(Warehouse warehouse);
    Warehouse delete(Long id);
}

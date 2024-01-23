package app.repositories;

import app.models.Warehouse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WarehouseJPARepositoryTest {
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private WarehouseJPARepository warehouseRepository;

    @Test
    void findAllShouldReturnListOfWarehouses() {
        //arrange
        TypedQuery<Warehouse> query = mock(TypedQuery.class);
        List<Warehouse> expectedWarehouses = Arrays.asList(new Warehouse(1, "Test", "Amsterdam", "Bijlmer", "9012 AF", 100, 20, 0), new Warehouse(2, "Test", "Amsterdam", "Bijlmer", "9012 AF", 100, 20, 0));

        when(entityManager.createQuery("select w from Warehouse w", Warehouse.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedWarehouses);

        //act
        List<Warehouse> actualWarehouses = warehouseRepository.findAll();

        //assert
        assertEquals(expectedWarehouses, actualWarehouses);
        verify(query).getResultList();
    }

    @Test
    void findByIdShouldReturnWarehouseWithID() {
        //arrange
        long warehouseId = 1;
        Warehouse expectedWarehouse = new Warehouse(1, "Test", "Amsterdam", "Bijlmer", "9012 AF", 100, 20, 0);

        when(entityManager.find(Warehouse.class, warehouseId)).thenReturn(expectedWarehouse);

        //act
        Warehouse actualWarehouse = warehouseRepository.findById(warehouseId);

        //assert
        assertEquals(expectedWarehouse, actualWarehouse);
        verify(entityManager).find(Warehouse.class, warehouseId);
    }
}

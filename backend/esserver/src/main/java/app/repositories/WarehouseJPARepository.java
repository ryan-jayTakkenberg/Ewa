package app.repositories;

import app.models.Warehouse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class WarehouseJPARepository implements EntityRepositoryJPA<Warehouse>{

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<Warehouse> findAll() {
        TypedQuery<Warehouse> query = this.em.createQuery("select w from Warehouse w", Warehouse.class);
        return query.getResultList();
    }

    @Override
    public Warehouse findById(long id) {
        return this.em.find(Warehouse.class, id);
    }

    @Override
    public Warehouse save(Warehouse entity) {
        return this.em.merge(entity);
    }

    @Override
    public Warehouse delete(Warehouse entity) {
        this.em.remove(entity);
        return entity;
    }
}

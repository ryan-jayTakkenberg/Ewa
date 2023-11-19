package app.repositories;

import app.models.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Transactional
public class OrderJPARepository implements EntityRepositoryJPA<Order> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Order> findAll() {
        // TODO
        return null;
    }

    @Override
    public Order findById(long id) {
        // TODO
        return null;
    }

    @Override
    public Order save(Order entity) {
        // TODO
        return null;
    }

    @Override
    public Order delete(Order entity) {
        // TODO
        return null;
    }
}
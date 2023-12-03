package app.repositories;

import app.models.Order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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
        TypedQuery<Order> query = this.em.createQuery("select a from Order a", Order.class);
        return query.getResultList();
    }

    @Override
    public Order findById(long id) {
        return em.find(Order.class, id);
    }

    @Override
    public Order save(Order entity) {
        return this.em.merge(entity);
    }

    @Override
    public Order delete(Order entity) {
        em.remove(entity);
        return entity;
    }
}

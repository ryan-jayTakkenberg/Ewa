package app.repositories;

import app.models.relations.Product_Order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class Product_OrderJPARepository implements EntityRepositoryJPA<Product_Order> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Product_Order> findAll() {
        TypedQuery<Product_Order> query = this.em.createQuery("SELECT po FROM Product_Order po", Product_Order.class);
        return query.getResultList();
    }

    @Override
    public Product_Order findById(long id) {
        return em.find(Product_Order.class, id);
    }

    @Override
    public Product_Order save(Product_Order entity) {
        return em.merge(entity);
    }

    @Override
    public Product_Order delete(Product_Order entity) {
        em.remove(entity);
        return entity;
    }

}

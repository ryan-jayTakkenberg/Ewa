package app.repositories;

import app.models.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class ProductJPARepository implements EntityRepositoryJPA<Product> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Product> findAll() {
        TypedQuery<Product> query = this.em.createQuery("select a from Product a", Product.class);
        return query.getResultList();
    }

    @Override
    public Product findById(int id) {
        return em.find(Product.class, id);
    }

    @Override
    public Product save(Product entity) {
        return this.em.merge(entity);
    }

    @Override
    public Product deleteById(int id) {
        Product product = this.findById(id);
        em.remove(product);
        return product;
    }
}

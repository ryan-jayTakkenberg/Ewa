package app.repositories;

import app.models.ProductInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class ProductJPARepository implements EntityRepositoryJPA<ProductInfo> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ProductInfo> findAll() {
        TypedQuery<ProductInfo> query = this.em.createQuery("select a from ProductInfo a", ProductInfo.class);
        return query.getResultList();
    }

    @Override
    public ProductInfo findById(long id) {
        return em.find(ProductInfo.class, id);
    }

    @Override
    public ProductInfo save(ProductInfo entity) {
        return this.em.merge(entity);
    }

    @Override
    public ProductInfo delete(ProductInfo entity) {
        em.remove(entity);
        return entity;
    }
}
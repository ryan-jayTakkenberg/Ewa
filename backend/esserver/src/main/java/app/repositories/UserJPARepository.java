package app.repositories;

import app.models.UserModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class UserJPARepository implements EntityRepositoryJPA<UserModel> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<UserModel> findAll() {
        TypedQuery<UserModel> query = this.em.createQuery("select a from UserModel a", UserModel.class);
        return query.getResultList();
    }

    @Override
    public UserModel findById(int id) {
        return em.find(UserModel.class, id);
    }

    @Override
    public UserModel save(UserModel entity) {
        return this.em.merge(entity);
    }

    @Override
    public UserModel delete(UserModel entity) {
        em.remove(entity);
        return entity;
    }
}

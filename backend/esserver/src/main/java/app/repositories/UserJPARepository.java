package app.repositories;

import app.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class UserJPARepository implements EntityRepositoryJPA<User> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> findAll() {
        TypedQuery<User> query = this.em.createQuery("select a from User a", User.class);
        return query.getResultList();
    }
    public User findByEmail(String email) {
        TypedQuery<User> query = this.em.createQuery("select u from User u where u.email = :email", User.class);
        query.setParameter("email", email);
        List<User> resultList = query.getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }
    public User findByResetToken(String resetToken) {
        TypedQuery<User> query = this.em.createQuery("select u from User u where u.resetToken = :resetToken", User.class);
        query.setParameter("resetToken", resetToken);
        List<User> resultList = query.getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    @Override
    public User findById(long id) {
        return em.find(User.class, id);
    }

    @Override
    public User save(User entity) {
        return this.em.merge(entity);
    }

    @Override
    public User delete(User entity) {
        em.remove(entity);
        return entity;
    }
}

package app.repositories;


import app.models.Teams;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class TeamJPARepository implements EntityRepositoryJPA<Teams> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Teams> findAll() {
        TypedQuery<Teams> query = this.em.createQuery("select t from Teams t", Teams.class);
        return query.getResultList();
    }

    @Override
    public Teams findById(long id) {
        return em.find(Teams.class, id);
    }

    @Override
    public Teams save(Teams entity) {
        return this.em.merge(entity);
    }

    @Override
    public Teams delete(Teams entity) {
        em.remove(entity);
        return entity;
    }
}
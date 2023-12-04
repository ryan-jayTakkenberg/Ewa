package app.repositories;


import app.models.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class TeamJPARepository implements EntityRepositoryJPA<Team> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Team> findAll() {
        TypedQuery<Team> query = this.em.createQuery("select t from Team t", Team.class);
        return query.getResultList();
    }

    @Override
    public Team findById(long id) {
        return em.find(Team.class, id);
    }

    @Override
    public Team save(Team entity) {
        return this.em.merge(entity);
    }

    @Override
    public Team delete(Team entity) {
        em.remove(entity);
        return entity;
    }

    public List<Team> findByQuery(String jpqlName, Object... params) {
        TypedQuery<Team> query = em.createNamedQuery(jpqlName, Team.class);

        if (jpqlName.equals("TEAM_ID_COUNT") && params.length > 0) {
            query.setParameter("teamId",Long.valueOf((Long) params[0]));
        }

        return query.getResultList();
    }

}

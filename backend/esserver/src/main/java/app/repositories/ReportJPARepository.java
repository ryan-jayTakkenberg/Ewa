package app.repositories;

import app.models.Report;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Transactional
public class ReportJPARepository implements EntityRepositoryJPA<Report> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Report> findAll() {

        TypedQuery<Report> query = this.em.createQuery("SELECT r FROM Report r", Report.class);
        return query.getResultList();
    }

    @Override
    public Report findById(long id) {

        return em.find(Report.class, id);
    }

    @Override
    public Report save(Report entity) {

        return em.merge(entity);
    }

    @Override
    public Report delete(Report entity) {

        em.remove(entity);
        return entity;
    }
}
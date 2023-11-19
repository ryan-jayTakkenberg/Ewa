package app.repositories;

import app.models.Report;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class ReportJPARepository implements EntityRepositoryJPA<Report> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Report> findAll() {
        // TODO
        return null;
    }

    @Override
    public Report findById(long id) {
        // TODO
        return null;
    }

    @Override
    public Report save(Report entity) {
        // TODO
        return null;
    }

    @Override
    public Report delete(Report entity) {
        // TODO
        return null;
    }
}
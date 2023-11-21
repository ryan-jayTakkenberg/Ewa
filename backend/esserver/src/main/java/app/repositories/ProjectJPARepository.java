package app.repositories;

import app.models.Project;
import app.repositories.EntityRepositoryJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Transactional
public class ProjectJPARepository implements EntityRepositoryJPA<Project> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Project> findAll() {

        TypedQuery<Project> query = this.em.createQuery("SELECT r FROM Project r", Project.class);
        return query.getResultList();
    }

    @Override
    public Project findById(long id) {
        // TODO
        return null;
    }

    @Override
    public Project save(Project entity) {

        return em.merge(entity);
    }

    @Override
    public Project delete(Project entity) {
        // TODO
        return null;
    }
}
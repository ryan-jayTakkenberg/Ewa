package app.repositories;

import java.util.List;


public interface EntityRepositoryJPA <E> {
    List<E> findAll();
    E findById(long id);
    E save(E entity);
    E delete(E entity);
}

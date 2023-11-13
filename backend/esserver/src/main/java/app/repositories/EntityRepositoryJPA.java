package app.repositories;

import java.util.List;

public interface EntityRepositoryJPA <E> {
    public List<E> findAll();
    public E findById(int id);
    public E save(E entity);
    public E deleteById(int id);
}

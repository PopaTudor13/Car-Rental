package Repository;

import Domain.Entity;

import java.util.Collection;

public interface IRepository<T extends Entity> extends Iterable<T>{
    public void add(T entity) throws DuplicateEntityExcept, RepositoryExcept;
    public void remove(int id) throws RepositoryExcept;

    public void update(T entity) throws RepositoryExcept;
    public T find(int id);
    public Collection<T> getAll();

    public void closeConnection();

    boolean isConnectionClosed();

    void openConnection();
}

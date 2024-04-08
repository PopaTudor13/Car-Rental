package Repository;

import Domain.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MemoryRepository<T extends Entity> extends AbstractRepository<T>{
    @Override
    public void add(T entity) throws DuplicateEntityExcept, RepositoryExcept {
        if (entity == null)
        {
            throw new IllegalArgumentException("Entitatea nu trebuie sa fie nula!");
        }
        if(find(entity.getId()) != null)
        {
            throw new DuplicateEntityExcept("Entitatea cu id-ul dat exista deja!");
        }
        //entities.add(entity);
        this.data.add(entity);
    }

    @Override
    public void remove(int id) throws RepositoryExcept {
        if(find(id) == null)
        {
            throw new IllegalArgumentException("Entitatea cu id-ul dat nu exista!");
        }
        Iterator<T> iterator = data.iterator();
        while(iterator.hasNext())
        {
            T entity = iterator.next();
            if(entity.getId() == id)
            {
                iterator.remove();
            }
        }
    }

    @Override
    public void update(T entity) throws RepositoryExcept {
        if (entity == null)
        {
            throw new IllegalArgumentException("Entitatea nu trebuie sa fie nula!");
        }
        if(find(entity.getId()) == null)
        {
            throw new RepositoryExcept("Entitatea cu id-ul dat nu exista!");
        }
        for(T entity1: data)
        {
            if(entity1.getId() == entity.getId())
            {
                int index = data.indexOf(entity1);
                data.set( index,entity);
            }
        }
    }

    @Override
    public T find(int id) {
        for(T entity: data)
        {
            if(entity.getId() == id)
            {
                return entity;
            }
        }
        return null;
    }




    @Override
    public Collection<T> getAll() {
        return new ArrayList<T>(data);
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayList<T>(data).iterator();
    }

    @Override
    public void closeConnection() {
    }

    @Override
    public void openConnection() {
    }

    @Override
    public boolean isConnectionClosed() {
        return true;
    }
}

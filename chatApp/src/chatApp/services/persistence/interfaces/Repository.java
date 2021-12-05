package chatApp.services.persistence.interfaces;

import chatApp.domain.exceptions.ChatAppDatabaseException;

import java.util.Collection;

public interface Repository<T, ID> {

    public Collection<T> get() throws ChatAppDatabaseException;

    public T get(ID id) throws ChatAppDatabaseException;

    public void add(T entity) throws ChatAppDatabaseException;

    public void delete(ID id) throws ChatAppDatabaseException;

    public void update(T entity) throws ChatAppDatabaseException;
}

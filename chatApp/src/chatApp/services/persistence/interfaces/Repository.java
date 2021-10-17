package chatApp.services.persistence.interfaces;

import java.util.Collection;

public interface Repository<T,ID> {

    public Collection<T> get() throws Exception;
    public T get(ID id) throws Exception;
    public void add(T entity) throws Exception;
    public void delete(ID id)throws Exception;
    public void update(T entity) throws Exception;
}

package chatApp.services.persistence.interfaces;

import java.util.Collection;

public interface Repository<T,ID> {

    public Collection<T> get();
    public T get(ID id);
    public void add(T entity);
    public void delete(T entity);
    public void update(T entity);
}

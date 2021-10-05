package chatApp.services.persistence.interfaces;

import java.util.Collection;

public interface Repository<T> {

    public Collection<T> get();
    public void add(T entity);
    public void delete(T entity);
    public void update(T entity);
}

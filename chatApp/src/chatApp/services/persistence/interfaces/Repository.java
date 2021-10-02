package chatApp.services.persistence.interfaces;

import java.util.Collection;

public interface Repository<T> {
    public Collection<T> get();
    public void add(T chat);
    public void delete(T chat);
}

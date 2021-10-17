package chatApp.services.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@FunctionalInterface
public interface Extractor<T> {
    public T extract(ResultSet resultSet) throws SQLException;
}

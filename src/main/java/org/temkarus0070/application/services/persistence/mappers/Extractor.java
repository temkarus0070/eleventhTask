package org.temkarus0070.application.services.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface Extractor<T> {
    public T extract(ResultSet resultSet) throws SQLException;
}

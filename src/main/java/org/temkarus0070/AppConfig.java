package org.temkarus0070;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


@Configuration
public class AppConfig {
    @Bean
    public DataSource dataSource() {
        DataSource dataSource = null;
        try {

            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup("java:/comp/env/jdbc/chatDB");
        } catch (
                NamingException ex) {
            dataSource = new SingleConnectionDataSource("jdbc:postgresql://localhost:5432/chat", "postgres", "postgres", true);
        }
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }


}

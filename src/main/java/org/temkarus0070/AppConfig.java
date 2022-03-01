package org.temkarus0070;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;

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
            throw new ChatAppDatabaseException(ex);
        }
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }


}

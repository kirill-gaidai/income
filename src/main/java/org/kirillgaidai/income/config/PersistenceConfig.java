package org.kirillgaidai.income.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.kirillgaidai.income.exception.IncomeException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@ComponentScan("org.kirillgaidai.income.dao")
public class PersistenceConfig {

    @Bean
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");

        URI dbUri;
        try {
            dbUri = new URI(databaseUrl);
        } catch (URISyntaxException e) {
            throw new IncomeException(e.getMessage());
        }

        String driverClassName = "org.postgresql.Driver";
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath()
                + "?sslmode=require";

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(driverClassName);
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;

        /*JndiDataSourceLookup jndiDataSourceLookup = new JndiDataSourceLookup();
        jndiDataSourceLookup.setResourceRef(true);
        return jndiDataSourceLookup.getDataSource("jdbc/income");*/
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

}

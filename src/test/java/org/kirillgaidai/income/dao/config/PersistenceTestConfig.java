package org.kirillgaidai.income.dao.config;

import org.kirillgaidai.income.dao.impl.AccountDao;
import org.kirillgaidai.income.dao.impl.BalanceDao;
import org.kirillgaidai.income.dao.impl.CategoryDao;
import org.kirillgaidai.income.dao.impl.CurrencyDao;
import org.kirillgaidai.income.dao.impl.OperationDao;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.kirillgaidai.income.dao.rowmappers.AccountEntityRowMapper;
import org.kirillgaidai.income.dao.rowmappers.BalanceEntityRowMapper;
import org.kirillgaidai.income.dao.rowmappers.CategoryEntityRowMapper;
import org.kirillgaidai.income.dao.rowmappers.CurrencyEntityRowMapper;
import org.kirillgaidai.income.dao.rowmappers.OperationEntityRowMapper;
import org.kirillgaidai.income.dao.util.DaoHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class PersistenceTestConfig {

    @Bean
    public DataSource getDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:db/schema_h2.sql")
                .build();
    }

    @Bean
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(getDataSource());
    }

    @Bean
    public DaoHelper getDaoHelper() {
        return new DaoHelper(getNamedParameterJdbcTemplate());
    }

    @Bean
    public AccountEntityRowMapper getAccountEntityRowMapper() {
        return new AccountEntityRowMapper();
    }

    @Bean
    public BalanceEntityRowMapper getBalanceEntityRowMapper() {
        return new BalanceEntityRowMapper();
    }

    @Bean
    public CategoryEntityRowMapper getCategoryEntityRowMapper() {
        return new CategoryEntityRowMapper();
    }

    @Bean
    public CurrencyEntityRowMapper getCurrencyEntityRowMapper() {
        return new CurrencyEntityRowMapper();
    }

    @Bean
    public OperationEntityRowMapper getOperationEntityRowMapper() {
        return new OperationEntityRowMapper();
    }

    @Bean
    public IAccountDao getAccountDao() {
        return new AccountDao(getNamedParameterJdbcTemplate(), getDaoHelper(), getAccountEntityRowMapper());
    }

    @Bean
    public IBalanceDao getBalanceDao() {
        return new BalanceDao(getNamedParameterJdbcTemplate(), getBalanceEntityRowMapper());
    }

    @Bean
    public ICategoryDao getCategoryDao() {
        return new CategoryDao(getNamedParameterJdbcTemplate(), getDaoHelper(), getCategoryEntityRowMapper());
    }

    @Bean
    public ICurrencyDao getCurrencyDao() {
        return new CurrencyDao(getNamedParameterJdbcTemplate(), getDaoHelper(), getCurrencyEntityRowMapper());
    }

    @Bean
    public IOperationDao getOperationDao() {
        return new OperationDao(getNamedParameterJdbcTemplate(), getDaoHelper(), getOperationEntityRowMapper());
    }

}

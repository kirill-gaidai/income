package org.kirillgaidai.income.dao.config;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.entity.RateEntity;
import org.kirillgaidai.income.dao.impl.AccountDao;
import org.kirillgaidai.income.dao.impl.BalanceDao;
import org.kirillgaidai.income.dao.impl.CategoryDao;
import org.kirillgaidai.income.dao.impl.CurrencyDao;
import org.kirillgaidai.income.dao.impl.OperationDao;
import org.kirillgaidai.income.dao.impl.RateDao;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.kirillgaidai.income.dao.intf.IRateDao;
import org.kirillgaidai.income.dao.rowmappers.AccountEntityRowMapper;
import org.kirillgaidai.income.dao.rowmappers.BalanceEntityRowMapper;
import org.kirillgaidai.income.dao.rowmappers.CategoryEntityRowMapper;
import org.kirillgaidai.income.dao.rowmappers.CurrencyEntityRowMapper;
import org.kirillgaidai.income.dao.rowmappers.OperationEntityRowMapper;
import org.kirillgaidai.income.dao.rowmappers.RateEntityRowMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
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
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(getDataSource());
    }

    @Bean
    public RowMapper<AccountEntity> accountEntityRowMapper() {
        return new AccountEntityRowMapper();
    }

    @Bean
    public RowMapper<BalanceEntity> balanceEntityRowMapper() {
        return new BalanceEntityRowMapper();
    }

    @Bean
    public RowMapper<CategoryEntity> categoryEntityRowMapper() {
        return new CategoryEntityRowMapper();
    }

    @Bean
    public RowMapper<CurrencyEntity> currencyEntityRowMapper() {
        return new CurrencyEntityRowMapper();
    }

    @Bean
    public RowMapper<OperationEntity> operationEntityRowMapper() {
        return new OperationEntityRowMapper();
    }

    @Bean
    public RowMapper<RateEntity> rateEntityRowMapper() {
        return new RateEntityRowMapper();
    }

    @Bean
    public IAccountDao accountDao() {
        return new AccountDao(namedParameterJdbcTemplate(), accountEntityRowMapper());
    }

    @Bean
    public IBalanceDao balanceDao() {
        return new BalanceDao(namedParameterJdbcTemplate(), balanceEntityRowMapper());
    }

    @Bean
    public ICategoryDao categoryDao() {
        return new CategoryDao(namedParameterJdbcTemplate(), categoryEntityRowMapper());
    }

    @Bean
    public ICurrencyDao currencyDao() {
        return new CurrencyDao(namedParameterJdbcTemplate(), currencyEntityRowMapper());
    }

    @Bean
    public IOperationDao operationDao() {
        return new OperationDao(namedParameterJdbcTemplate(), operationEntityRowMapper());
    }

    @Bean
    public IRateDao rateDao() {
        return new RateDao(namedParameterJdbcTemplate(), rateEntityRowMapper());
    }

}

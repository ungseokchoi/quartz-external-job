package com.core.db;

import com.core.yml.DataSourceProperty;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
public class DataSourceFactory {

    public DataSource getDataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
        dataSource.setDataSource(new LazyConnectionDataSourceProxy(properties
                                .initializeDataSourceBuilder()
                                .build()));
        return dataSource;
    }

    public LocalContainerEntityManagerFactoryBean getEntityManagerFactory(EntityManagerFactoryBuilder builder
            , DataSource dataSource, String entityPath, String persistenceUnitName) {
        return builder
                .dataSource(dataSource)
                .packages(entityPath)
                .persistenceUnit(persistenceUnitName)
                .build();
    }

    public PlatformTransactionManager getTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}

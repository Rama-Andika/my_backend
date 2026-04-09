package com.oxysystem.general.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ConditionalOnProperty(name = "app.db2.enabled", havingValue = "true", matchIfMissing = false)
public class Db2Config {

    @Bean
    @ConfigurationProperties("spring.datasource.db2")
    public DataSourceProperties db2DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "db2DataSource")
    public DataSource db1DataSource(
            @Qualifier("db2DataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "db2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean db2EntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("db2DataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.oxysystem.general.model.db2")
                .persistenceUnit("db2")
                .build();
    }

    @Bean(name = "db2TransactionManager")
    public PlatformTransactionManager db2TransactionManager(
            @Qualifier("db2EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


    /**
     * Repository scan khusus DB2, hanya aktif kalau app.db2.enabled=true
     */
    @EnableJpaRepositories(
            basePackages = "com.oxysystem.general.repository.db2",
            entityManagerFactoryRef = "db2EntityManagerFactory",
            transactionManagerRef = "db2TransactionManager"
    )
    static class Db2RepositoriesConfig {
    }
}

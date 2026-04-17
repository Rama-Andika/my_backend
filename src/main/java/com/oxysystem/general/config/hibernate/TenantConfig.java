package com.oxysystem.general.config.hibernate;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        // ⚠️ MASUKKAN SEMUA PACKAGE REPOSITORY KLIEN (TRANSAKSI) DI SINI
        basePackages = {
                "com.oxysystem.general.repository.tenant"
        },
        // Pastikan tidak ikut me-scan package master.repository!
        excludeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
                type = org.springframework.context.annotation.FilterType.REGEX,
                pattern = "com\\.oxysystem\\.general\\.repository\\.master\\..*"
        ),
        entityManagerFactoryRef = "tenantEntityManagerFactory",
        transactionManagerRef = "tenantTransactionManager"
)
public class TenantConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Primary // Jadikan ini prioritas agar @Transactional biasa langsung mengarah ke sini
    @Bean(name = "tenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("masterDataSource") DataSource masterDataSource,
            MultiTenantConnectionProvider multiTenantConnectionProvider,
            CurrentTenantIdentifierResolver tenantIdentifierResolver) {

        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());
        properties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
        properties.put(Environment.HBM2DDL_AUTO, "none");

        LocalContainerEntityManagerFactoryBean em = builder
                .dataSource(masterDataSource)
                // ⚠️ MASUKKAN SEMUA PACKAGE ENTITY KLIEN DI SINI
                .packages(
                        "com.oxysystem.general.model.tenant"
                )
                .persistenceUnit("tenant")
                .build();

        em.setJpaPropertyMap(properties);
        return em;
    }

    @Primary
    @Bean(name = "tenantTransactionManager")
    public PlatformTransactionManager tenantTransactionManager(
            @Qualifier("tenantEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
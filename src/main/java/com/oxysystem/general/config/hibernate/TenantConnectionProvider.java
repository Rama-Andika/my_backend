package com.oxysystem.general.config.hibernate;

import com.oxysystem.general.model.master.Tenant;
import com.oxysystem.general.repository.master.TenantRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private final Map<String, DataSource> tenantDataSources = new ConcurrentHashMap<>();

    @Autowired
    @Qualifier("masterDataSource")
    private DataSource masterDataSource;

    @Autowired
    @Lazy // Wajib pakai Lazy agar tidak terjadi Circular Dependency
    private TenantRepository tenantRepository;

    @Override
    protected DataSource selectAnyDataSource() {
        return masterDataSource;
    }

    @Override
    protected DataSource selectDataSource(String tenantId) {
        if ("master".equals(tenantId)) {
            return masterDataSource;
        }
        return tenantDataSources.computeIfAbsent(tenantId, this::createDataSourceForTenant);
    }

    private DataSource createDataSourceForTenant(String tenantId) {
        System.out.println("🔄 Meminta koneksi elegan untuk Tenant: " + tenantId);

        // AMAN DARI DEADLOCK! Karena TenantRepository menggunakan MasterConfig
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Klien tidak ditemukan: " + tenantId));

        System.out.println("✅ Data Klien ditemukan via JPA! URL: " + tenant.getDbUrl());

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(tenant.getDbUrl());
        config.setUsername(tenant.getDbUsername());
        config.setPassword(tenant.getDbPassword());
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        config.setMaximumPoolSize(5);
        config.setMinimumIdle(0);
        config.setIdleTimeout(300000);
        config.setConnectionTimeout(20000);
        config.setMaxLifetime(540000); // 9 Menit
        config.setIdleTimeout(300000); // 5 Menit
        config.setKeepaliveTime(30000); // Ping setiap 30 detik

        return new HikariDataSource(config);
    }
}
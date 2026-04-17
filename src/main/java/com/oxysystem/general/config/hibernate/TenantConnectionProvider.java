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
import java.util.concurrent.TimeUnit;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;

@Component
public class TenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private final Cache<String, DataSource> tenantDataSources = Caffeine.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .scheduler(Scheduler.systemScheduler()) // Memastikan Background Thread bekerja menghapus cache
            .removalListener((key, dataSource, cause) -> {
                if (dataSource instanceof HikariDataSource) {
                    ((HikariDataSource) dataSource).close();
                    System.out.println("🛑 HikariDataSource untuk Tenant [" + key + "] berhasil ditutup karena idle (hemat RAM).");
                }
            })
            .build();

    @Autowired
    @Qualifier("masterDataSource")
    private DataSource masterDataSource;

    @Autowired
    @Lazy // Wajib pakai Lazy agar tidak terjadi Circular Dependency
    private TenantRepository tenantRepository;

    /**
     * Memaksa aplikasi untuk mereset koneksi database dari Tenant.
     * Panggil method ini jika API Admin Anda mengganti password atau meng-suspend tenant.
     */
    public void clearTenantDataSource(String tenantId) {
        tenantDataSources.invalidate(tenantId);
        System.out.println("🧹 Cache koneksi database untuk Tenant [" + tenantId + "] telah dibersihkan.");
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return masterDataSource;
    }

    @Override
    protected DataSource selectDataSource(String tenantId) {
        if ("master".equals(tenantId)) {
            return masterDataSource;
        }
        return tenantDataSources.get(tenantId, this::createDataSourceForTenant);
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

        config.setMaximumPoolSize(5); // Diturunkan dari 10 ke 5 untuk menghindari "Too many connections" error di MySQL jika ratusan tenant login aktif bersamaan
        config.setMinimumIdle(0);
        config.setConnectionTimeout(20000);
        config.setIdleTimeout(600000); // 10 Menit koneksi dibiarkan idle sebelum dihancurkan
        config.setMaxLifetime(1800000); // Standard aman 30 Menit (cegah reset terus menerus)
        config.setKeepaliveTime(30000); // Ping setiap 30 detik

        return new HikariDataSource(config);
    }
}
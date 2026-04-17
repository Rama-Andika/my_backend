package com.oxysystem.general.service;


import com.oxysystem.general.model.master.Tenant;
import com.oxysystem.general.repository.master.TenantRepository;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Service
public class TenantProvisioningService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private DataSource masterDataSource;

    @Value("${app.tenant.db.host:localhost}")
    private String dbHost;

    @Value("${app.tenant.db.port:3306}")
    private String dbPort;

    @Transactional(value = "masterTransactionManager", rollbackFor = {Throwable.class})
    public String createNewTenant(String tenantId, String dbUsername, String dbPassword) {

        // Validasi Keamanan: Cegah SQL Injection di nama database
        if (!tenantId.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("Tenant ID hanya boleh berisi huruf dan angka tanpa spasi/simbol.");
        }

        String dbName = "client_" + tenantId;

        String dbUrl = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true", dbHost, dbPort, dbName);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(masterDataSource);

        // Eksekusi DDL: Buat Database Kosong di Server
        // Pakai backtick (`) untuk mengamankan nama database
        jdbcTemplate.execute("CREATE DATABASE IF NOT EXISTS `" + dbName + "`");

        // SECURITY BEST PRACTICE: Buat User MySQL Terpisah per Klien
        // Ini memastikan Toko A tidak akan pernah bisa mengintip database Toko B (Cross-Database Access)
        try {
            jdbcTemplate.execute("CREATE USER IF NOT EXISTS '" + dbUsername + "'@'%' IDENTIFIED BY '" + dbPassword + "'");
            jdbcTemplate.execute("GRANT ALL PRIVILEGES ON `" + dbName + "`.* TO '" + dbUsername + "'@'%'");
            jdbcTemplate.execute("FLUSH PRIVILEGES");
            System.out.println("Berhasil membuat User MySQL khusus untuk: " + dbUsername);
        } catch (Exception e) {
            // Kita tangkap errornya jika user sudah terlanjur ada agar sistem tidak crash
            System.err.println("⚠Info: User MySQL sudah ada atau hak akses RDS dibatasi. " + e.getMessage());
        }

        // Simpan data klien ini ke Buku Tamu (Database Master)
        Tenant newTenant = new Tenant();
        newTenant.setTenantId(tenantId);
        newTenant.setDbUrl(dbUrl);
        newTenant.setDbUsername(dbUsername);
        newTenant.setDbPassword(dbPassword);
        newTenant.setStatus("ACTIVE");
        tenantRepository.save(newTenant);

        // Bangun Tabel menggunakan Flyway
        // Wajib: Flyway masuk menggunakan user klien yang baru saja dibuat, BUKAN user master
        runFlywayMigration(dbUrl, dbUsername, dbPassword);

        return "Berhasil! Environment untuk klien '" + tenantId + "' siap digunakan di server " + dbHost;
    }

    private void runFlywayMigration(String dbUrl, String username, String password) {
        Flyway flyway = Flyway.configure()
                .dataSource(dbUrl, username, password)
                .locations("classpath:db/migration/tenants")
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();
    }
}
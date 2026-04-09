package com.oxysystem.general.repository.db1.general.custom.impl;

import com.oxysystem.general.dto.general.apiAppSync.view.ApiAppSyncViewDTO;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.repository.db1.general.custom.ApiAppSyncRepositoryCustom;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ApiAppSyncRepositoryCustomImpl implements ApiAppSyncRepositoryCustom {
    @PersistenceContext(unitName = "db1")
    private EntityManager entityManager;

    @Override
    public List<ApiAppSyncViewDTO> findApiAppSyncGrabNotSyncByTableName(String tableName) {
        String sql = "SELECT " +
                "  api.api_sync_id," +
                "  api.date," +
                "  api.table_name," +
                "  api.action," +
                "  api.owner_id," +
                "  api.STATUS," +
                "  api.location_id," +
                "  s.grab_merchant_id " +
                "FROM " +
                "  api_app_sync AS api " +
                "  INNER JOIN api_app AS app " +
                "    ON api.app_id = app.api_app_id " +
                "  INNER JOIN pos_struk_kasir AS s " +
                "    ON api.location_id = s.location_id " +
                "WHERE table_name = :tableName " +
                "  AND app.name = :apiApp " +
                "  AND api.STATUS = :status " +
                "  AND s.grab_merchant_id IS NOT NULL AND s.grab_merchant_id != '' " +
                "GROUP BY owner_id, " +
                "  api.location_id " +
                "LIMIT 1000";

        Query query = entityManager.createNativeQuery(sql);

        query.setParameter("tableName", tableName);
        query.setParameter("apiApp", Product.GRAB_MART.name());
        query.setParameter("status", 0);

        List<Object[]> results = query.getResultList();

        return results.stream().map(r -> new ApiAppSyncViewDTO(
                r[0] != null ? ((BigInteger) r[0]).longValue() : null, // api sync id
                r[1] != null ? ((Timestamp) r[1]).toLocalDateTime() : null, // date
                (String) r[2], // table name
                (String) r[3], // action
                r[4] != null ? ((BigInteger) r[4]).longValue() : null, // owner id
                r[5] != null ? ((Number) r[5]).intValue() : null, // status
                r[6] != null ? ((BigInteger) r[6]).longValue() : null, // location id
                (String) r[7] // grab merchant id
        )).collect(Collectors.toList());
    }

    @Override
    public List<ApiAppSyncViewDTO> findApiAppSyncGrabFoodNotSyncByTableName(String tableName) {
        String sql = "SELECT " +
                "  api.api_sync_id," +
                "  api.date," +
                "  api.table_name," +
                "  api.action," +
                "  api.owner_id," +
                "  api.STATUS," +
                "  api.location_id," +
                "  s.grab_food_merchant_id " +
                "FROM " +
                "  api_app_sync AS api " +
                "  INNER JOIN api_app AS app " +
                "    ON api.app_id = app.api_app_id " +
                "  INNER JOIN pos_struk_kasir AS s " +
                "    ON api.location_id = s.location_id " +
                "WHERE table_name = :tableName " +
                "  AND app.name = :apiApp " +
                "  AND api.STATUS = :status " +
                "  AND s.grab_food_merchant_id IS NOT NULL AND s.grab_food_merchant_id != '' " +
                "GROUP BY owner_id, " +
                "  api.location_id " +
                "LIMIT 1000";

        Query query = entityManager.createNativeQuery(sql);

        query.setParameter("tableName", tableName);
        query.setParameter("apiApp", Product.GRAB_FOOD.name());
        query.setParameter("status", 0);

        List<Object[]> results = query.getResultList();

        return results.stream().map(r -> new ApiAppSyncViewDTO(
                r[0] != null ? ((BigInteger) r[0]).longValue() : null, // api sync id
                r[1] != null ? ((Timestamp) r[1]).toLocalDateTime() : null, // date
                (String) r[2], // table name
                (String) r[3], // action
                r[4] != null ? ((BigInteger) r[4]).longValue() : null, // owner id
                r[5] != null ? ((Number) r[5]).intValue() : null, // status
                r[6] != null ? ((BigInteger) r[6]).longValue() : null, // location id
                (String) r[7] // grab merchant id
        )).collect(Collectors.toList());
    }
}

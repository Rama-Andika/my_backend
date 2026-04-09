package com.oxysystem.general.repository.db1.general.custom.impl;

import com.oxysystem.general.dto.general.location.view.GrabPackagingItemLocationViewDTO;
import com.oxysystem.general.dto.general.location.view.LocationViewDTO;
import com.oxysystem.general.repository.db1.general.custom.LocationRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class LocationRepositoryCustomImpl implements LocationRepositoryCustom {
    @PersistenceContext(unitName = "db1")
    private EntityManager entityManager;
    @Override
    public Page<LocationViewDTO> getLocations(String name, String grabMerchantId, String grabFoodMerchantId, Pageable pageable) {
        // Query for select data
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT " +
                " l.location_id," +
                " l.name," +
                " s.grab_merchant_id, " +
                " gm.status AS mart_status, " +
                " s.grab_food_merchant_id, " +
                " gf.status AS food_status " +
                "  FROM " +
                "  pos_struk_kasir s " +
                "  INNER JOIN pos_location l " +
                "   ON s.location_id = l.location_id " +
                "  LEFT JOIN grab_menu_sync_status gm " +
                "   ON s.grab_merchant_id = gm.merchant_id " +
                "  LEFT JOIN grab_menu_sync_status gf " +
                "   ON s.grab_food_merchant_id = gf.merchant_id " +
                "   WHERE l.is_active = 1 ");

        // Query For count data
        StringBuilder sqlCountBuilder = new StringBuilder();
        sqlCountBuilder.append("SELECT COUNT(*) FROM (" );
        sqlCountBuilder.append(sqlBuilder);

        if(name != null && !name.isEmpty()){
            sqlBuilder.append(" AND l.name LIKE :name ");
            sqlCountBuilder.append(" AND l.name LIKE :name ");
        }
        if(grabMerchantId != null && !grabMerchantId.isEmpty()){
            sqlBuilder.append(" AND s.grab_merchant_id LIKE :grabMerchantId ");
            sqlCountBuilder.append(" AND s.grab_merchant_id LIKE :grabMerchantId ");
        }
        if(grabFoodMerchantId != null && !grabFoodMerchantId.isEmpty()){
            sqlBuilder.append(" AND s.grab_food_merchant_id LIKE :grabFoodMerchantId ");
            sqlCountBuilder.append(" AND s.grab_food_merchant_id LIKE :grabFoodMerchantId ");
        }

        sqlBuilder.append(" GROUP BY s.location_id ORDER BY l.name ");
        sqlCountBuilder.append(" GROUP BY s.location_id) AS tmp");

        String sql = sqlBuilder.toString();
        String sqlCount = sqlCountBuilder.toString();

        Query query = entityManager.createNativeQuery(sql);
        Query queryCount = entityManager.createNativeQuery(sqlCount);

        if(name != null && !name.isEmpty()){
            query.setParameter("name", "%" + name + "%");
            queryCount.setParameter("name", "%" + name + "%");
        }
        if(grabMerchantId != null && !grabMerchantId.isEmpty()){
            query.setParameter("grabMerchantId", "%" + grabMerchantId + "%");
            queryCount.setParameter("grabMerchantId", "%" + grabMerchantId + "%");
        }
        if(grabFoodMerchantId != null && !grabFoodMerchantId.isEmpty()){
            query.setParameter("grabFoodMerchantId", "%" + grabFoodMerchantId + "%");
            queryCount.setParameter("grabFoodMerchantId", "%" + grabFoodMerchantId + "%");
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> results = query.getResultList();
        List<LocationViewDTO> contents = results.stream()
                .map(r -> new LocationViewDTO(
                        ((BigInteger) r[0]).toString(), // id
                        (String) r[1], // name
                        r[2] != null ? (String) r[2] : "", // grab merchant id
                        r[3] != null ? (String) r[3] : "", // grab menu sync status
                        r[4] != null ? (String) r[4] : "", // grab food merchant id
                        r[5] != null ? (String) r[5] : ""  // grab menu food sync status
                )).collect(Collectors.toList());
        long total = ((Number) queryCount.getSingleResult()).longValue();
        return new PageImpl<>(contents, pageable, total);
    }

    @Override
    public Page<GrabPackagingItemLocationViewDTO> getPackagingItemLocations(String name, String product, Pageable pageable) {
        // Query for select data
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("" +
                "SELECT " +
                "  l.location_id, " +
                "  l.name AS location_name," +
                "  pos_item_master.name AS item_name," +
                "  pos_item_master.barcode," +
                "  pos_item_master.item_master_id," +
                "  grab_packaging_item.id AS grab_packaging_item_id " +
                "FROM " +
                "  pos_struk_kasir s " +
                "  INNER JOIN pos_location l " +
                "    ON s.location_id = l.location_id " +
                "  LEFT JOIN " +
                "    (SELECT " +
                "      * " +
                "    FROM " +
                "      grab_packaging_item " +
                "    WHERE product = :product) AS grab_packaging_item " +
                "    ON l.location_id = grab_packaging_item.location_id " +
                "  LEFT JOIN pos_item_master " +
                "    ON grab_packaging_item.item_master_id = pos_item_master.item_master_id " +
                "WHERE l.is_active = 1 ");

        // Query For count data
        StringBuilder sqlCountBuilder = new StringBuilder();
        sqlCountBuilder.append("SELECT COUNT(*) FROM (" );
        sqlCountBuilder.append(sqlBuilder);

        if(name != null && !name.isEmpty()){
            sqlBuilder.append(" AND l.name LIKE :name ");
            sqlCountBuilder.append(" AND l.name LIKE :name ");
        }

        sqlBuilder.append(" GROUP BY s.location_id ORDER BY l.name ");
        sqlCountBuilder.append(" GROUP BY s.location_id) AS tmp");

        String sql = sqlBuilder.toString();
        String sqlCount = sqlCountBuilder.toString();

        Query query = entityManager.createNativeQuery(sql);
        Query queryCount = entityManager.createNativeQuery(sqlCount);

        if(name != null && !name.isEmpty()){
            query.setParameter("name", "%" + name + "%");
            queryCount.setParameter("name", "%" + name + "%");
        }
        if(product != null && !product.isEmpty()){
            query.setParameter("product", product);
            queryCount.setParameter("product", product);
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> results = query.getResultList();
        List<GrabPackagingItemLocationViewDTO> contents = results.stream()
                .map(r -> new GrabPackagingItemLocationViewDTO(
                        ((BigInteger) r[0]).toString(), // id
                        (String) r[1], // location name
                        r[2] != null ? (String) r[2] : "", // item name
                        r[3] != null ? (String) r[3] : "", // barcode
                        r[4] != null ? ((BigInteger) r[4]).toString() : "", // item id
                        r[5] != null ? ((BigInteger) r[5]).toString() : "" // grab packaging id
                )).collect(Collectors.toList());
        long total = ((Number) queryCount.getSingleResult()).longValue();
        return new PageImpl<>(contents, pageable, total);
    }
}

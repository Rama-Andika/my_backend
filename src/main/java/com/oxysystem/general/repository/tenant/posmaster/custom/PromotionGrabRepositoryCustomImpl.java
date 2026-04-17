package com.oxysystem.general.repository.tenant.posmaster.custom;

import com.oxysystem.general.dto.posmaster.promotionGrab.view.PromotionGrabViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class PromotionGrabRepositoryCustomImpl implements PromotionGrabRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Page<PromotionGrabViewDTO> getPromotionsGrab(Long locationId, String name, String product, Pageable pageable) {
        String sql = getStringBuilderQuery(locationId, name, product);
        String sqlCount = getStringBuilderQueryCount(locationId, name, product);

        Query query = entityManager.createNativeQuery(sql);
        Query queryCount = entityManager.createNativeQuery(sqlCount);
        if(locationId != null){
            query.setParameter("locationId", locationId);
            queryCount.setParameter("locationId", locationId);
        }
        if(name != null && !name.isEmpty()){
            query.setParameter("name", "%"+name+"%");
            queryCount.setParameter("name", "%"+name+"%");
        }

        if(product != null && !product.isEmpty()){
            query.setParameter("product", product);
            queryCount.setParameter("product", product);
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> results = query.getResultList();
        List<PromotionGrabViewDTO> contents = results.stream()
                .map(r -> new PromotionGrabViewDTO(
                        ((BigInteger) r[0]).toString(), // id
                        (String) r[1], // name
                        r[2] != null ? ((Timestamp) r[2]).toLocalDateTime() : null, // start time
                        r[3] != null ? ((Timestamp) r[3]).toLocalDateTime() : null, // end time
                        r[4] != null ? (String) r[4] : "", // type
                        r[5] != null ? (String) r[5] : "", // Status
                        r[6] != null ? (String) r[6] : "" // product
                )).collect(Collectors.toList());

        long total = ((Number) queryCount.getSingleResult()).longValue();
        return new PageImpl<>(contents, pageable, total);
    }

    private static String getStringBuilderQuery(Long locationId, String name, String product) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT pg.id, pg.name, pg.start_time, pg.end_time, pg.type, pg.status, pg.product ");
        sqlBuilder.append("FROM pos_promotion_grab pg ");
        sqlBuilder.append("LEFT JOIN pos_promotion_grab_location pl ");
        sqlBuilder.append("ON pg.id = pl.promotion_grab_id ");
        sqlBuilder.append("WHERE 1=1 ");

        if(locationId != null){
            sqlBuilder.append("AND pl.location_id = :locationId ");
        }
        if(name != null && !name.isEmpty()){
            sqlBuilder.append("AND pg.name LIKE :name ");
        }
        if(product != null && !product.isEmpty()){
            sqlBuilder.append("AND pg.product = :product ");
        }

        sqlBuilder.append("GROUP BY pg.id ");
        sqlBuilder.append("ORDER BY pg.start_time desc ");

        String sql = sqlBuilder.toString();
        return sql;
    }

    private static String getStringBuilderQueryCount(Long locationId, String name,  String product) {
        StringBuilder sqlCountBuilder = new StringBuilder();
        sqlCountBuilder.append("SELECT COUNT(*) ");
        sqlCountBuilder.append("FROM ( ");
        sqlCountBuilder.append("SELECT pg.id, pg.name, pg.start_time, pg.end_time, pg.type ");
        sqlCountBuilder.append("FROM pos_promotion_grab pg ");
        sqlCountBuilder.append("LEFT JOIN pos_promotion_grab_location pl ");
        sqlCountBuilder.append("ON pg.id = pl.promotion_grab_id ");
        sqlCountBuilder.append("WHERE 1=1 ");

        if(locationId != null){
            sqlCountBuilder.append("AND pl.location_id = :locationId ");
        }
        if(name != null && !name.isEmpty()){
            sqlCountBuilder.append("AND pg.name LIKE :name ");
        }
        if(product != null && !product.isEmpty()){
            sqlCountBuilder.append("AND pg.product = :product ");
        }

        sqlCountBuilder.append("GROUP BY pg.id ");
        sqlCountBuilder.append(") AS tmp ");
        return sqlCountBuilder.toString();
    }
}

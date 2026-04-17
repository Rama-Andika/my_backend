package com.oxysystem.general.repository.tenant.transaction.custom.impl;

import com.oxysystem.general.dto.transaction.promotion.view.PromotionViewDto;
import com.oxysystem.general.repository.tenant.transaction.custom.PromotionRepositoryCustom;
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

public class PromotionRepositoryCustomImpl implements PromotionRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<PromotionViewDto> getPromotions(String number, String startDate, String endDate, Integer type, Integer subType, String description, Pageable pageable) {
        StringBuilder sqlMain = new  StringBuilder("SELECT " +
                "  promotion_id," +
                "  number," +
                "  start_date," +
                "  end_date," +
                "  tipe," +
                "  promo_desc," +
                "  STATUS ");

        StringBuilder sqlCount = new StringBuilder("SELECT count(*) ");

        String sqlFrom = "FROM pos_promotion ";

        StringBuilder sqlWhere = new StringBuilder("WHERE 1=1 ");

        if(number != null && !number.isEmpty()){
            sqlWhere.append("AND number LIKE :number ");
        }
        if(startDate != null && !startDate.isEmpty()){
            sqlWhere.append("AND start_date >= :startDate ");
        }
        if(endDate != null && !endDate.isEmpty()){
            sqlWhere.append("AND end_date <= :endDate ");
        }
        if(type != null){
            sqlWhere.append("AND tipe = :type ");
        }
        if(subType != null){
            sqlWhere.append("AND sub_type = :subType ");
        }

        sqlMain.append(sqlFrom).append(sqlWhere).append("ORDER BY start_date DESC ");
        sqlCount.append(sqlFrom).append(sqlWhere);

        Query query = entityManager.createNativeQuery(sqlMain.toString());
        Query queryCount = entityManager.createNativeQuery(sqlCount.toString());

        if(number != null && !number.isEmpty()){
            query.setParameter("number", "%" +number+ "%");
            queryCount.setParameter("number", "%" +number+ "%");
        }
        if(startDate != null && !startDate.isEmpty()){
            query.setParameter("startDate", startDate + " 00:00:00");
            queryCount.setParameter("startDate", startDate + " 00:00:00");
        }
        if(endDate != null && !endDate.isEmpty()){
            query.setParameter("startDate", endDate + " 23:59:59");
            queryCount.setParameter("startDate", endDate + " 23:59:59");
        }
        if(type != null){
            query.setParameter("type", type);
            queryCount.setParameter("type", type);
        }
        if(subType != null){
            query.setParameter("subType", subType);
            queryCount.setParameter("subTypehttp://localhost:5173/akidsmaginepg", subType);
        }

        query.setFirstResult((int)  pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> results = query.getResultList();
        List<PromotionViewDto> contents = results.stream()
                .map(r -> new PromotionViewDto(
                        ((BigInteger) r[0]).toString(), //Promotion Id
                        (String) r[1], //Promotion Number
                        ((Timestamp) r[2]).toLocalDateTime(), //Start Date
                        ((Timestamp) r[3]).toLocalDateTime(), //End Time
                        ((Number) r[4]).intValue(), //Type
                        r[5] != null ? (String) r[5] : "", //Description
                        r[6] != null ? (String) r[6] : "" //Status
                )).collect(Collectors.toList());

        long total = ((Number) queryCount.getSingleResult()).longValue();
        return new PageImpl<>(contents, pageable, total);
    }
}

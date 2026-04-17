package com.oxysystem.general.repository.tenant.transaction.custom.impl;

import com.oxysystem.general.dto.transaction.playgroundSession.view.PlaygroundSessionViewDTO;
import com.oxysystem.general.repository.tenant.transaction.custom.PlaygroundSessionRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PlaygroundSessionRepositoryCustomImpl implements PlaygroundSessionRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PlaygroundSessionViewDTO> getLiveSession() {
        String sql = "SELECT " +
                "  s.id," +
                "  c.name as child_name," +
                "  s.end_time," +
                "  CASE WHEN s.is_active = 0 THEN 'INACTIVE' " +
                "  WHEN s.end_time <= CURRENT_TIMESTAMP THEN 'EXPIRED'" +
                "  WHEN TIMESTAMPDIFF(MINUTE,CURRENT_TIMESTAMP,s.end_time) <= 2 THEN 'ENDING_SOON' " +
                "  WHEN TIMESTAMPDIFF(MINUTE,CURRENT_TIMESTAMP,s.end_time) <= 5 THEN 'ALMOST_FINISHED' " +
                "  ELSE 'ACTIVE' " +
                "  END AS STATUS," +
                "  GREATEST(" +
                "    TIMESTAMPDIFF(" +
                "      MINUTE," +
                "      CURRENT_TIMESTAMP," +
                "      s.end_time " +
                "    )," +
                "    0 " +
                "  ) AS minute_left, " +
                "  r.number, " +
                "  p.name AS parent_name, " +
                "  s.code AS session_code, " +
                "  p.phone AS parent_phone, " +
                "  p.code AS parent_code " +
                "FROM " +
                "  playground_session s " +
                "  JOIN playground_children c ON s.child_id = c.id " +
                "  JOIN playground_parent p ON c.parent_id = p.id " +
                "  JOIN playground_registration r ON p.id = r.parent_id " +
                "WHERE s.is_active = 1 " +
                "ORDER BY s.end_time ASC";

        Query query =  entityManager.createNativeQuery(sql);

        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(r -> new PlaygroundSessionViewDTO(
                        ((BigInteger)r[0]).toString(), //playground id
                        (String) r[1], //children name
                        ((Timestamp)r[2]).toLocalDateTime(), //end time
                        (String) r[3], //status
                        ((Number)r[4]).longValue(), //minute left
                        (String)r[5], // Registration Number
                        (String)r[6], // Parent Name
                        r[7] != null ? (String) r[7] : "", //Session code
                        r[8] != null ? (String) r[8] : "", //Parent Phone
                        r[9] != null ? (String) r[9] : "" //Parent Code
                )).collect(Collectors.toList());
    }

    @Override
    public Page<PlaygroundSessionViewDTO> getEndSession(String date, Pageable pageable) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT " +
                "  s.id," +
                "  c.name," +
                "  s.end_time," +
                "  'INACTIVE' AS STATUS," +
                "  0 AS minute_left, " +
                "  r.number, " +
                "  p.name AS parent_name, " +
                "  s.code AS session_code, " +
                "  p.phone AS parent_phone, " +
                "  p.code AS parent_code ");
        String sqlFrom = "FROM " +
                "  playground_session s " +
                "  JOIN playground_children c ON s.child_id = c.id " +
                "  JOIN playground_parent p ON c.parent_id = p.id " +
                "  JOIN playground_registration r ON p.id = r.parent_id ";
        String sqlWhere = "WHERE s.is_active = 0 ";
        if(date != null && !date.isEmpty()) {
            sqlWhere += "AND DATE(s.updated_at) = :date ";
        }
        sql.append(sqlFrom);
        sql.append(sqlWhere);
        sql.append(" ORDER BY s.end_time ASC");

        StringBuilder sqlCount = new StringBuilder();
        sqlCount.append("SELECT COUNT(*) ");
        sqlCount.append(sqlFrom);
        sqlCount.append(sqlWhere);

        Query query =  entityManager.createNativeQuery(sql.toString());
        Query queryCount = entityManager.createNativeQuery(sqlCount.toString());

        if(date != null && !date.isEmpty()) {
            query.setParameter("date", date);
            queryCount.setParameter("date", date);
        }

        query.setFirstResult((int)pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> results = query.getResultList();
        List<PlaygroundSessionViewDTO> contents = results.stream()
                .map(r -> new PlaygroundSessionViewDTO(
                        ((BigInteger)r[0]).toString(), //playground id
                        (String) r[1], //children name
                        ((Timestamp)r[2]).toLocalDateTime(), //end time
                        (String) r[3], //status
                        ((Number)r[4]).longValue(), //minute left
                        (String)r[5], // Registration Number
                        (String)r[6], // Parent Name
                        r[7] != null ? (String) r[7] : "", //Session code
                        r[8] != null ? (String) r[8] : "", //Parent Phone
                        r[9] != null ? (String) r[9] : "" //Parent Code
                )).collect(Collectors.toList());

        long total = ((Number) queryCount.getSingleResult()).longValue();

         return new PageImpl<>(contents, pageable, total);
    }
}

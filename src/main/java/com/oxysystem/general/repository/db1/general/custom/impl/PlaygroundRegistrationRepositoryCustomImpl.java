package com.oxysystem.general.repository.db1.general.custom.impl;

import com.oxysystem.general.dto.general.playgroundRegistration.view.PlaygroundRegistrationViewDTO;
import com.oxysystem.general.repository.db1.general.custom.PlaygroundRegistrationRepositoryCustom;
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
public class PlaygroundRegistrationRepositoryCustomImpl implements PlaygroundRegistrationRepositoryCustom {
    @PersistenceContext(unitName = "db1")
    private EntityManager entityManager;


    @Override
    public Page<PlaygroundRegistrationViewDTO> getPlaygroundRegistrations(String number, Pageable pageable) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT " +
                "  playground_registration.id," +
                "  playground_registration.number," +
                "  playground_parent.name," +
                "  playground_registration.created_at, " +
                "  pos_sales_taking.doc_status ");
        String sqlFrom = "FROM " +
                "  playground_registration " +
                "  INNER JOIN playground_parent " +
                "   ON playground_registration.parent_id = playground_parent.id " +
                "  LEFT JOIN pos_sales_taking " +
                "   ON playground_registration.number = pos_sales_taking.number ";
        StringBuilder sqlWhere = new StringBuilder("WHERE 1=1 ");
        if(number != null && !number.isEmpty()){
            sqlWhere.append(" AND playground_registration.number LIKE :number");
        }

        sql.append(sqlFrom);
        sql.append(sqlWhere);
        sql.append(" ORDER BY playground_registration.created_at DESC");

        StringBuilder sqlCount = new StringBuilder();
        sqlCount.append("SELECT COUNT(*) ");
        sqlCount.append(sqlFrom);
        sqlCount.append(sqlWhere);

        Query query = entityManager.createNativeQuery(sql.toString());
        Query queryCount = entityManager.createNativeQuery(sqlCount.toString());

        if(number != null && !number.isEmpty()){
            query.setParameter("number", "%" +  number + "%");
            queryCount.setParameter("number", "%" +  number + "%");
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> result = query.getResultList();
        List<PlaygroundRegistrationViewDTO> contents = result.stream()
                .map(r -> new PlaygroundRegistrationViewDTO(
                        ((BigInteger)r[0]).toString(), //Playground registration id
                        (String) r[1], //Playground registration number
                        (String) r[2], //Playground parent name
                        ((Timestamp)r[3]).toLocalDateTime(), //Playground registration created date
                        r[4] != null ? (String)  r[4] : "" //Sales taking status
                )).collect(Collectors.toList());

        long total = ((Number)queryCount.getSingleResult()).longValue();
        return new PageImpl<>(contents, pageable, total);
    }
}

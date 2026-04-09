package com.oxysystem.general.repository.db1.general.custom.impl;

import com.oxysystem.general.dto.general.customer.view.CustomerViewDTO;
import com.oxysystem.general.repository.db1.general.custom.CustomerRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomerRepositoryCustomImpl implements CustomerRepositoryCustom {
    @PersistenceContext(unitName = "db1")
    private EntityManager entityManager;

    @Override
    public Page<CustomerViewDTO> getCustomers(String name, Pageable pageable) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT " +
                "customer_id," + //0
                "name," + //1
                "phone "); //2
        String fromSql = "FROM customer ";
        StringBuilder whereSql = new StringBuilder("WHERE 1=1 ");
        if(name != null && !name.isEmpty()){
            whereSql.append("AND name LIKE :name ");
        }
        sqlBuilder.append(fromSql);
        sqlBuilder.append(whereSql);
        sqlBuilder.append("ORDER BY name ");

        StringBuilder countSqlBuilder = new StringBuilder("SELECT COUNT(*) ");
        countSqlBuilder.append(fromSql);
        countSqlBuilder.append(whereSql);

        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        Query queryCount = entityManager.createNativeQuery(countSqlBuilder.toString());

        if(name != null && !name.isEmpty()){
            query.setParameter("name", "%"+name+"%");
            queryCount.setParameter("name", "%"+name+"%");
        }

        query.setFirstResult((int)pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> result = query.getResultList();
        List<CustomerViewDTO> contents = result.stream()
                .map(r -> new CustomerViewDTO(
                        ((BigInteger)r[0]).toString(), //Customer id
                        r[1] != null ? (String) r[1] : "", //Customer name
                        r[2] != null ? (String) r[2] : "" //Customer phone
                )).collect(Collectors.toList());

        long total = ((Number)queryCount.getSingleResult()).longValue();
        return new PageImpl<>(contents, pageable, total);
    }
}

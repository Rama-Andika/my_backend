package com.oxysystem.general.repository.tenant.transaction.custom.impl;

import com.oxysystem.general.dto.transaction.playgroundSession.report.PlaygroundSessionReportDTO;
import com.oxysystem.general.repository.tenant.transaction.custom.PlaygroundSessionReportRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PlaygroundSessionReportRepositoryCustomImpl implements PlaygroundSessionReportRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<PlaygroundSessionReportDTO> getPlaygroundSessionReport(String registrationNumber, Pageable pageable) {
        StringBuilder sqlBuilder = new StringBuilder("select " +
                "  playground_registration.number as registration_number," + //0
                "  playground_parent.name as parent_name," + //1
                "  playground_parent.phone as parent_phone," + //2
                "  playground_children.name as child_name," + //3
                "  playground_session.code as code," + //4
                "  timestampdiff(minute,playground_session.start_time,playground_session.end_time) as duration," + //6
                "  playground_session.end_time "); //7
        String fromSql = "from " +
                "  playground_registration " +
                "  join playground_parent " +
                "    on playground_registration.parent_id = playground_parent.id " +
                "  join playground_children " +
                "    on playground_children.parent_id = playground_parent.id " +
                "  join playground_session " +
                "    on playground_session.child_id = playground_children.id ";
        StringBuilder whereSql = new StringBuilder("where 1 = 1 ");
        if(registrationNumber != null && !registrationNumber.isEmpty()){
            whereSql.append("AND playground_registration.number LIKE :registrationNumber");
        }
        sqlBuilder.append(fromSql);
        sqlBuilder.append(whereSql);
        sqlBuilder.append(" order by playground_registration.created_at desc");

        StringBuilder countSql = new StringBuilder("select count(*) ");
        countSql.append(fromSql);
        countSql.append(whereSql);

        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        if(registrationNumber != null && !registrationNumber.isEmpty()){
            query.setParameter("registrationNumber", "%"+registrationNumber+"%");
            countQuery.setParameter("registrationNumber", "%"+registrationNumber+"%");
        }

        query.setFirstResult((int)  pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> result = query.getResultList();
        List<PlaygroundSessionReportDTO> playgroundSessionReportDTOS = result.stream()
                .map(r -> new PlaygroundSessionReportDTO(
                        (String) r[0], //Registration number
                        (String) r[1], //Parent name
                        (String) r[2], //Parent Phone
                        (String) r[3], //Child Name
                        (String) r[4], //Code
                        ((Number)r[5]).doubleValue(), //Duration
                        ((Timestamp)r[6]).toLocalDateTime() //End time
                )).collect(Collectors.toList());
        long total = ((Number) countQuery.getSingleResult()).longValue();
        return new PageImpl<>(playgroundSessionReportDTOS, pageable, total);
    }
}

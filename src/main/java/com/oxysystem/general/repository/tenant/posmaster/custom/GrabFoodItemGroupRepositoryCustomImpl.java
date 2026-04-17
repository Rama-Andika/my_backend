package com.oxysystem.general.repository.tenant.posmaster.custom;

import com.oxysystem.general.dto.posmaster.grabFoodGroup.view.GrabFoodItemGroupViewDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GrabFoodItemGroupRepositoryCustomImpl implements GrabFoodItemGroupRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<GrabFoodItemGroupViewDTO> getGrabFoodItemGroups(String name) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT " +
                "  id," +
                "  NAME," +
                "  sequence " +
                "FROM " +
                "  grabfood_item_group " +
                "WHERE 1=1 ");

        if (name != null && !name.isEmpty()){
            sqlBuilder.append("AND name LIKE :name ");
        }

        sqlBuilder.append("ORDER BY sequence");

        String sql = sqlBuilder.toString();

        Query query = entityManager.createNativeQuery(sql);

        if (name != null && !name.isEmpty()){
            query.setParameter("name", "%" + name + "%");
        }

        List<Object[]> results = query.getResultList();
        return results.stream().map(r -> new GrabFoodItemGroupViewDTO(
                ((BigInteger) r[0]).toString(), // id
                r[1] != null ? (String) r[1] : "", // name
                r[2] != null ? ((Number) r[2]).intValue() : 0
        )).collect(Collectors.toList());
    }
}

package com.oxysystem.general.repository.tenant.posmaster.custom;

import com.oxysystem.general.dto.posmaster.cogs.view.CogsViewDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CogsRepositoryCustomImpl implements CogsRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CogsViewDTO> cogsByItems(List<Long> itemMasterIds) {
        String sql = "" +
                "SELECT " +
                "  im.item_master_id," +
                "  IFNULL(c.new_cogs, 0) AS cogs " +
                "FROM " +
                "  pos_item_master im " +
                "  LEFT JOIN cogs c " +
                "    ON c.cogs_id = " +
                "    (SELECT " +
                "      cogs_id " +
                "    FROM " +
                "      cogs " +
                "    WHERE item_master_id = im.item_master_id " +
                "    ORDER BY last_update DESC " +
                "    LIMIT 1) " +
                "WHERE im.item_master_id IN :itemMasterIds";

        Query query = entityManager.createNativeQuery(sql);

        query.setParameter("itemMasterIds", itemMasterIds);

        List<Object[]> results = query.getResultList();
        return results.stream().map(r -> new CogsViewDTO(
                ((BigInteger) r[0]).longValue(), // item id
                r[1] != null ? ((Number) r[1]).doubleValue() : 0 // cogs
        )).collect(Collectors.toList());
    }
}

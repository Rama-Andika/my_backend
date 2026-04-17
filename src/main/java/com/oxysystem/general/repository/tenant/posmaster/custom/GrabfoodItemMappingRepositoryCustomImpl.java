package com.oxysystem.general.repository.tenant.posmaster.custom;

import com.oxysystem.general.dto.posmaster.grabFoodItemMapping.view.GrabfoodItemMappingViewDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GrabfoodItemMappingRepositoryCustomImpl implements GrabfoodItemMappingRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<GrabfoodItemMappingViewDTO> getGrabfoodItemMapping(String golPrice, Long locationId) {
        String sql = "SELECT " +
                "gim.grabfood_item_group_id, " +
                "gim.item_master_id, " +
                "gig.name AS grabfood_category," +
                "gig.id AS grabfood_category_id," +
                "gig.sequence AS grabfood_category_sequence, " +
                "im.name," +
                "im.barcode, " +
                "COALESCE(pt." + golPrice + ",0) AS price," +
                "COALESCE( " +
                "    (SELECT " +
                "      SUM(qty * in_out) " +
                "    FROM " +
                "      pos_stock " +
                "    WHERE item_master_id = im.item_master_id " +
                "      AND location_id = :locationId)," +
                "    0 " +
                "  ) as stock," +
                "gim.special_type, "+
                "gim.description " +
                " FROM grabfood_item_mapping gim" +
                " INNER JOIN grabfood_item_group gig" +
                " ON gim.grabfood_item_group_id = gig.id" +
                " INNER JOIN pos_item_master im" +
                " ON gim.item_master_id = im.item_master_id" +
                " LEFT JOIN pos_price_type pt " +
                " ON pt.price_type_id = ("+
                "   SELECT price_type_id FROM pos_price_type" +
                "   WHERE item_master_id = im.item_master_id" +
                "   ORDER BY conv_qty DESC, qty_from DESC" +
                "   LIMIT 1" +
                " )" +
                " WHERE gim.is_published = 1 AND gim.location_id = :locationId " +
                " GROUP BY gim.item_master_id " +
                " ORDER BY gig.sequence";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("locationId",locationId);

        List<Object[]> results = query.getResultList();

        return results.stream()
                .map(row -> new GrabfoodItemMappingViewDTO(
                        ((BigInteger) row[0]).longValue(), // grabfood item group id
                        ((BigInteger) row[1]).longValue(), // item master id
                        (String) row[2], // grabfood category
                        ((BigInteger) row[3]).toString(), // grabfood category id
                        row[4] != null ? ((Number) row[4]).intValue() : null, // grabfood category sequence
                        (String) row[5], // item name
                        (String) row[6], // barcode
                        ((Number) row[7]).doubleValue(), // price
                        ((Number) row[8]).doubleValue(), // stock
                        (String) row[9], // special type
                        row[10] != null ? (String) row[10] : "" // description
                ))
                .collect(Collectors.toList());
    }
}

package com.oxysystem.general.repository.tenant.posmaster.custom;

import com.oxysystem.general.dto.posmaster.grabMartItemMapping.view.GrabmartItemMappingViewDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GrabmartItemMappingRepositoryCustomImpl implements GrabmartItemMappingRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<GrabmartItemMappingViewDTO> getGrabmartItemMapping(String golPrice, Long locationId) {
        String sql = "SELECT " +
                        "gim.grabmart_item_group_id, " +
                        "gim.grabmart_item_category_id, " +
                        "gim.item_master_id, " +
                        "gig.name AS grabmart_category," +
                        "gig.grabmart_item_group_id AS grabmart_category_id," +
                        "gig.sequence AS grabmart_category_sequence, " +
                        "gic.name AS grabmart_sub_category," +
                        "gic.grabmart_item_category_id AS grabmart_sub_category_id, " +
                        "gic.sequence AS grabmart_sub_category_sequence, " +
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
                        "gim.special_type,"+
                        "gim.grabmart_unit,"+
                        "gim.value "+
                    " FROM grabmart_item_mapping gim" +
                    " INNER JOIN grabmart_item_group gig" +
                    " ON gim.grabmart_item_group_id = gig.id" +
                    " INNER JOIN grabmart_item_category gic" +
                    " ON gim.grabmart_item_category_id = gic.id" +
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
                .map(row -> new GrabmartItemMappingViewDTO(
                        ((Number) row[0]).longValue(), // grabmart item group id
                        ((Number) row[1]).longValue(), // grabmart item category id
                        ((Number) row[2]).longValue(), // item master id
                        (String) row[3], // grabmart category
                        (String) row[4], // grabmart category id
                        row[5] != null ? ((Number) row[5]).intValue() : null, // grabmart category sequence
                        (String) row[6], // grabmart sub category
                        (String) row[7], // grabmart sub category id
                        row[8] != null ? ((Number) row[8]).intValue() : null, // grabmart sub category sequence
                        (String) row[9], // item name
                        (String) row[10], // barcode
                        ((Number) row[11]).doubleValue(), // price
                        ((Number) row[12]).doubleValue(), // stock
                        (String) row[13], // special type
                        (String) row[14], // Grabmart Unit
                        ((Number) row[15]).doubleValue() // Value
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<GrabmartItemMappingViewDTO> getGrabMartItemMappingByBarcodes(List<String> barcodes) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT " +
                "  gim.grabmart_item_group_id, " +
                "  gim.grabmart_item_category_id, " +
                "  gim.item_master_id, " +
                "  im.name, " +
                "  im.barcode, " +
                "  gim.special_type, " +
                "  gim.grabmart_unit, " +
                "  gim.value " +
                "FROM " +
                "  grabmart_item_mapping gim " +
                "  INNER JOIN grabmart_item_group gig " +
                "    ON gim.grabmart_item_group_id = gig.id " +
                "  INNER JOIN grabmart_item_category gic " +
                "    ON gim.grabmart_item_category_id = gic.id " +
                "  INNER JOIN pos_item_master im " +
                "    ON gim.item_master_id = im.item_master_id " +
                "WHERE gim.is_published = 1 " +
                "  and im.barcode IN :barcodes " +
                "GROUP BY gim.item_master_id");

        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("barcodes",barcodes);

        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(r -> new GrabmartItemMappingViewDTO(
                        ((BigInteger) r[0]).longValue(), // grab mart item group id
                        ((BigInteger) r[1]).longValue(), // grab mart item category id
                        ((BigInteger) r[2]).longValue(), // item master id
                        (String) r[3], // item name
                        (String) r[4], // barcode
                        (String) r[5], // special type
                        (String) r[6], // grab mart unit
                        ((Number) r[7]).doubleValue() // value
                )).collect(Collectors.toList());
    }

}

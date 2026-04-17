package com.oxysystem.general.repository.tenant.posmaster.custom;

import com.oxysystem.general.dto.posmaster.itemMaster.view.ItemMasterView2DTO;
import com.oxysystem.general.dto.posmaster.itemMaster.view.ItemMasterViewDTO;
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
public class ItemMasterRepositoryCustomImpl implements ItemMasterRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Page<ItemMasterViewDTO> getItemMasters(String name, String barcode, String code, Long itemGroupId, Long itemCategoryId, Pageable pageable) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT im.item_master_id, im.name, im.barcode, im.code, ig.name AS item_group, ic.name AS item_category ")
                .append("FROM pos_item_master im ")
                .append("LEFT JOIN pos_item_group ig ")
                .append("ON im.item_group_id = ig.item_group_id ")
                .append("LEFT JOIN pos_item_category ic ")
                .append("ON im.item_category_id = ic.item_category_id ");

        StringBuilder sqlCountBuilder = new StringBuilder();
        sqlCountBuilder.append("SELECT COUNT(*) ")
                .append("FROM pos_item_master im ")
                .append("LEFT JOIN pos_item_group ig ")
                .append("ON im.item_group_id = ig.item_group_id ")
                .append("LEFT JOIN pos_item_category ic ")
                .append("ON im.item_category_id = ic.item_category_id ");

        StringBuilder filter = new StringBuilder("WHERE im.is_active = 1 ");
        if(name != null && !name.isEmpty()){
            filter.append("AND im.name LIKE :name ");
        }
        if(barcode != null && !barcode.isEmpty()){
            filter.append("AND im.barcode LIKE :barcode ");
        }
        if(code != null && !code.isEmpty()){
            filter.append("AND im.code LIKE :code ");
        }
        if(itemGroupId != null){
            filter.append("AND im.item_group_id = :itemGroupId ");
        }
        if(itemCategoryId != null){
            filter.append("AND im.item_category_id = :itemCategoryId ");
        }

        sqlBuilder.append(filter);
        sqlCountBuilder.append(filter);
        sqlBuilder.append("ORDER BY im.name asc ");

        String sql = sqlBuilder.toString();
        String sqlCount = sqlCountBuilder.toString();

        // Query
        Query query = entityManager.createNativeQuery(sql);
        Query countQuery = entityManager.createNativeQuery(sqlCount);

        if(name != null && !name.isEmpty()){
            query.setParameter("name", "%" + name + "%");
            countQuery.setParameter("name", "%" + name + "%");
        }
        if(barcode != null && !barcode.isEmpty()){
            query.setParameter("barcode", "%" + barcode + "%");
            countQuery.setParameter("barcode", "%" + barcode + "%");
        }
        if(code != null && !code.isEmpty()){
            query.setParameter("code", "%" + code + "%");
            countQuery.setParameter("code", "%" + code + "%");
        }
        if(itemGroupId != null){
            query.setParameter("itemGroupId", itemGroupId);
            countQuery.setParameter("itemGroupId", itemGroupId);
        }
        if(itemCategoryId != null){
            query.setParameter("itemCategoryId", itemCategoryId);
            countQuery.setParameter("itemCategoryId", itemCategoryId);
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> results = query.getResultList();
        List<ItemMasterViewDTO> contents = results.stream()
                .map(r -> new ItemMasterViewDTO(
                        ((BigInteger) r[0]).toString(), // Item Id
                        r[1] != null ? (String) r[1].toString().trim() : "", // Item Name
                        r[2] != null ? (String) r[2].toString().trim() : "", // Barcode
                        r[3] != null ? (String) r[3].toString().trim() : "", // Code
                        r[4] != null ? (String) r[4].toString().trim() : "", // Item Group
                        r[5] != null ? (String) r[5].toString().trim() : "" // Item Category
                )).collect(Collectors.toList());
        long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(contents, pageable, total);
    }

    @Override
    public Page<ItemMasterViewDTO> getItemMastersWithPrice(String name, String barcode, String code, Long itemGroupId, Long itemCategoryId, String golPrice, Pageable pageable) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT " +
                        "im.item_master_id, " + //0
                        "im.name, " + //1
                        "im.barcode, " + //2
                        "im.code, " + //3
                        "ig.name AS item_group, " + //4
                        "ic.name AS item_category, ") //5
                .append("COALESCE(pt.").append(golPrice).append(",0) AS price, ") //6
                .append("u.unit AS unit," + //7
                        "u.uom_id," + //8
                        "pt.conv_qty "); //9

        StringBuilder sqlFromBuilder = new StringBuilder();
        sqlFromBuilder.append("FROM pos_item_master im ")
                .append("LEFT JOIN pos_item_group ig ")
                .append("ON im.item_group_id = ig.item_group_id ")
                .append("LEFT JOIN pos_item_category ic ")
                .append("ON im.item_category_id = ic.item_category_id ")
                .append("LEFT JOIN pos_price_type pt ")
                .append("ON im.item_master_id = pt.item_master_id ")
                .append("LEFT JOIN pos_unit u ")
                .append("ON pt.uom_id = u.uom_id ");
        sqlBuilder.append(sqlFromBuilder);

        StringBuilder sqlCountBuilder = new StringBuilder();
        sqlCountBuilder.append("SELECT COUNT(*) ");
        sqlCountBuilder.append(sqlFromBuilder);

        StringBuilder filter = new StringBuilder("WHERE im.is_active = 1 ");
        if(name != null && !name.isEmpty()){
            filter.append("AND im.name LIKE :name ");
        }
        if(barcode != null && !barcode.isEmpty()){
            filter.append("AND im.barcode = :barcode ");
        }
        if(code != null && !code.isEmpty()){
            filter.append("AND im.code = :code ");
        }
        if(itemGroupId != null){
            filter.append("AND im.item_group_id = :itemGroupId ");
        }
        if(itemCategoryId != null){
            filter.append("AND im.item_category_id = :itemCategoryId ");
        }

        sqlBuilder.append(filter);
        sqlCountBuilder.append(filter);
        sqlBuilder.append("ORDER BY im.name asc ");

        String sql = sqlBuilder.toString();
        String sqlCount = sqlCountBuilder.toString();

        // Query
        Query query = entityManager.createNativeQuery(sql);
        Query countQuery = entityManager.createNativeQuery(sqlCount);

        if(name != null && !name.isEmpty()){
            query.setParameter("name", "%" + name + "%");
            countQuery.setParameter("name", "%" + name + "%");
        }
        if(barcode != null && !barcode.isEmpty()){
            query.setParameter("barcode", barcode);
            countQuery.setParameter("barcode", barcode);
        }
        if(code != null && !code.isEmpty()){
            query.setParameter("code", code );
            countQuery.setParameter("code", code);
        }
        if(itemGroupId != null){
            query.setParameter("itemGroupId", itemGroupId);
            countQuery.setParameter("itemGroupId", itemGroupId);
        }
        if(itemCategoryId != null){
            query.setParameter("itemCategoryId", itemCategoryId);
            countQuery.setParameter("itemCategoryId", itemCategoryId);
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> results = query.getResultList();
        List<ItemMasterViewDTO> contents = results.stream()
                .map(r -> new ItemMasterViewDTO(
                        ((BigInteger) r[0]).toString(), // Item Id
                        r[1] != null ? (String) r[1].toString().trim() : "", //Item Name
                        r[2] != null ? (String) r[2].toString().trim() : "", //Barcode
                        r[3] != null ? (String) r[3].toString().trim() : "", //Code
                        r[4] != null ? (String) r[4].toString().trim() : "", //Item Group
                        r[5] != null ? (String) r[5].toString().trim() : "", //Item Category
                        r[7] != null ? (String) r[7] : "", //Unit
                        r[6] != null ? ((Number) r[6]).doubleValue() : 0.0, //Price
                        r[8] != null ? ((BigInteger) r[8]).toString() : "", //Uom id
                        r[9] != null ? ((Number) r[9]).doubleValue() : 0.0 //Conv Qty
                )).collect(Collectors.toList());
        long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(contents, pageable, total);
    }

    @Override
    public Page<ItemMasterView2DTO> getItemMastersByLocation(String name, String barcode, String code, String imgLink, String golPrice, Long locationId, String category, boolean availability, Pageable pageable) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT " +
                "  pos_item_master.item_master_id," + //0
                "  pos_item_master.name AS item_name," + //1
                "  pos_item_master.code," + //2
                "  barcode," + //3
                "  pos_item_group.name AS category," + //4
                "  pos_item_category.name AS sub_category," + //5
                "  COALESCE(stock.qty_stock, 0) AS stock," + //6
                "  COALESCE(price.").append(golPrice).append(", 0) AS price," + //7
                "  COALESCE(pos_item_master_image.path, '') AS path_image "); //8

        StringBuilder sqlCountBuilder = new StringBuilder("SELECT COUNT(DISTINCT pos_item_master.item_master_id) ");

        StringBuilder sqlFrom = new StringBuilder("FROM " +
                "  pos_item_master " +
                "  INNER JOIN pos_item_group " +
                "    ON pos_item_master.item_group_id = pos_item_group.item_group_id " +
                "  INNER JOIN pos_item_category " +
                "    ON pos_item_master.item_category_id = pos_item_category.item_category_id " +
                "  LEFT JOIN pos_item_master_image " +
                "    ON pos_item_master.item_master_id = pos_item_master_image.item_master_id " +
                "  LEFT JOIN " +
                "    (SELECT " +
                "      item_master_id, " +
                "      SUM(qty * in_out) AS qty_stock " +
                "    FROM " +
                "      pos_stock " +
                "    WHERE location_id = :locationId " +
                "    GROUP BY item_master_id) AS stock " +
                "    ON pos_item_master.item_master_id = stock.item_master_id " +
                " LEFT JOIN " +
                "    (SELECT " +
                "      t1.item_master_id, " +
                "      t1.").append(golPrice).append(" " +
                "    FROM " +
                "      pos_price_type t1 " +
                "      INNER JOIN " +
                "        (SELECT " +
                "          item_master_id," +
                "          MIN( " +
                "            CONCAT( " +
                "              LPAD(qty_from, 10, '0')," +
                "              LPAD(conv_qty, 10, '0') " +
                "            ) " +
                "          ) AS min_key " +
                "        FROM " +
                "          pos_price_type " +
                "        GROUP BY item_master_id) t2 " +
                "        ON t1.item_master_id = t2.item_master_id " +
                "        AND CONCAT( " +
                "          LPAD(t1.qty_from, 10, '0'), " +
                "          LPAD(t1.conv_qty, 10, '0') " +
                "        ) = t2.min_key) price " +
                "    ON price.item_master_id = pos_item_master.item_master_id ");

        StringBuilder sqlWhere = new StringBuilder("WHERE pos_item_master.is_active = 1 AND pos_item_master.for_sales = 1 ");
        if(name!=null && !name.isEmpty()){
            sqlWhere.append("AND pos_item_master.name LIKE :name ");
        }
        if(barcode!=null && !barcode.isEmpty()){
            sqlWhere.append("AND pos_item_master.barcode LIKE :barcode ");
        }
        if(code!=null && !code.isEmpty()){
            sqlWhere.append("AND pos_item_master.code LIKE :code ");
        }
        if(category!=null && !category.isEmpty()){
            sqlWhere.append("AND pos_item_group.name LIKE :category ");
        }
        if(availability){
            sqlWhere.append("AND COALESCE(stock.qty_stock, 0) > 0 ");
        }

        sqlBuilder.append(sqlFrom).append(sqlWhere).append("ORDER BY item_name");
        sqlCountBuilder.append("FROM " +
                "  pos_item_master " +
                "  INNER JOIN pos_item_group " +
                "    ON pos_item_master.item_group_id = pos_item_group.item_group_id " +
                "  INNER JOIN pos_item_category " +
                "    ON pos_item_master.item_category_id = pos_item_category.item_category_id " +
                "  LEFT JOIN pos_item_master_image " +
                "    ON pos_item_master.item_master_id = pos_item_master_image.item_master_id " +
                "  LEFT JOIN " +
                "    (SELECT " +
                "      item_master_id, " +
                "      SUM(qty * in_out) AS qty_stock " +
                "    FROM " +
                "      pos_stock " +
                "    WHERE location_id = :locationId " +
                "    GROUP BY item_master_id) AS stock " +
                "    ON pos_item_master.item_master_id = stock.item_master_id ").append(sqlWhere);

        Query query = entityManager.createNativeQuery(String.valueOf(sqlBuilder));
        Query countQuery = entityManager.createNativeQuery(String.valueOf(sqlCountBuilder));

        if(locationId != null){
            query.setParameter("locationId", locationId);
            countQuery.setParameter("locationId", locationId);
        }
        if(name!=null && !name.isEmpty()){
            query.setParameter("name", "%" + name + "%");
            countQuery.setParameter("name", "%" + name + "%");
        }
        if(barcode!=null && !barcode.isEmpty()){
            query.setParameter("barcode", "%" + barcode + "%");
            countQuery.setParameter("barcode", "%" + barcode + "%");
        }
        if(code!=null && !code.isEmpty()){
            query.setParameter("code", "%" + code + "%");
            countQuery.setParameter("code", "%" + code + "%");
        }
        if(category!=null && !category.isEmpty()){
            query.setParameter("category", "%" + category + "%");
            countQuery.setParameter("category", "%" + category + "%");
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> results = query.getResultList();
        List<ItemMasterView2DTO> contents = results.stream()
                .collect(Collectors.groupingBy(o -> ((BigInteger)o[0]).toString() ))
                .values()
                .stream()
                .map(rows -> {
                    Object[] firstRow = rows.get(0);

                    ItemMasterView2DTO dto = new ItemMasterView2DTO();
                    dto.setItemId(((BigInteger) firstRow[0]).toString());
                    dto.setItemName((String) firstRow[1]);
                    dto.setCode((String) firstRow[2]);
                    dto.setBarcode((String) firstRow[3]);
                    dto.setCategory((String) firstRow[4]);
                    dto.setSubCategory((String) firstRow[5]);
                    dto.setStock(firstRow[6] != null ? ((Number) firstRow[6]).doubleValue() : 0d);
                    dto.setPrice(firstRow[7] != null ? ((Number) firstRow[7]).doubleValue() : 0d);

                    // Collect images
                    List<String> images = rows.stream()
                            .map(r -> (String) r[8])
                            .filter(path -> path != null && !path.isEmpty())
                            .map(path -> imgLink + "/" + path)
                            .distinct()
                            .collect(Collectors.toList());

                    dto.setImages(images.isEmpty() ? null : images);

                    return dto;
                }).collect(Collectors.toList());

        long total = ((Number) countQuery.getSingleResult()).longValue();
        return new PageImpl<>(contents, pageable, total);
    }

}

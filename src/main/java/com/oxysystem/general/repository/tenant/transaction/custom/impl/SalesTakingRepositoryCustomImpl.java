package com.oxysystem.general.repository.tenant.transaction.custom.impl;

import com.oxysystem.general.dto.transaction.payment.view.PaymentViewDTO;
import com.oxysystem.general.dto.transaction.returnPayment.view.ReturnPaymentViewDTO;
import com.oxysystem.general.dto.transaction.salesTaking.view.SalesTakingPlaygroundViewDTO;
import com.oxysystem.general.dto.transaction.salesTakingDetail.view.SalesTakingDetailQtyReturnDTO;
import com.oxysystem.general.dto.transaction.salesTakingDetail.view.SalesTakingDetailViewDTO;
import com.oxysystem.general.repository.tenant.transaction.custom.SalesTakingRepositoryCustom;
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
public class SalesTakingRepositoryCustomImpl implements SalesTakingRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public SalesTakingPlaygroundViewDTO getSalesTakingPlaygroundByNumber(String number) {
        String sql = "SELECT " +
                "  pos_sales_taking.id, " + //0
                "  pos_sales_taking.number," + //1
                "  pos_sales_taking.date," + //2
                "  playground_parent.name AS parent_name," + //3
                "  playground_parent.phone AS parent_phone," + //4
                "  pos_sales_taking.doc_status," + //5
                "  pos_sales_taking.amount," + //6
                "  pos_sales_taking.global_diskon," + //7
                "  pos_item_master.name AS item_name," + //8
                "  pos_item_master.barcode AS item_barcode," + //9
                "  pos_sales_taking_detail.selling_price," + //10
                "  pos_sales_taking_detail.qty," + //11
                "  pos_sales_taking_detail.total, " + //12
                "  pos_sales_taking_detail.id as sales_taking_detail_id, " + //13
                "  pos_item_master.item_master_id, " + //14
                "  pos_sales_taking_detail.discount_item, " + //15
                "  customer.name AS customer_name," + //16
                "  customer.customer_id, " + //17
                "  pos_sales_taking_detail.uom_id, " + //18
                "  pos_sales_taking_detail.conv_qty, " + //19
                "  customer.phone as customer_phone, " + //20
                "  pos_sales_taking.sales_retur_id, " + //21
                "  pos_sales_taking_detail.sales_detail_retur_id," + //22
                "  COALESCE(SUM(sales_detail_return.qty), 0) AS prev_return, " + //23
                "  pos_payment.payment_id," + //24
                "  COALESCE(pos_payment.amount, 0) AS payment_amount," + //25
                "  COALESCE(pos_return_payment.amount, 0) AS return_payment_amount," + //26
                "  COALESCE(pos_payment.cost_card_amount, 0) AS cost_card_amount," + //27
                "  COALESCE(pos_payment.cost_card_percent,0) AS cost_card_percent," + //28
                "  bank.name AS bank," + //29
                "  merchant.description AS merchant," + //30
                "  payment_method.description AS payment_method " + //31
                "FROM " +
                "  pos_sales_taking " +
                "  JOIN customer " +
                "    ON pos_sales_taking.customer_id = customer.customer_id " +
                "  JOIN pos_sales_taking_detail " +
                "    ON pos_sales_taking.id = pos_sales_taking_detail.sales_id " +
                "  LEFT JOIN " +
                "    (SELECT " +
                "      sales_detail_retur_id, " +
                "      qty " +
                "    FROM " +
                "      pos_sales_taking_detail) AS sales_detail_return " +
                "    ON pos_sales_taking_detail.id = sales_detail_return.sales_detail_retur_id " +
                "  JOIN pos_item_master " +
                "    ON pos_sales_taking_detail.product_master_id = pos_item_master.item_master_id " +
                "  LEFT JOIN playground_registration " +
                "    ON pos_sales_taking.number = playground_registration.number " +
                "  LEFT JOIN playground_parent " +
                "    ON playground_registration.parent_id = playground_parent.id " +
            "     LEFT JOIN pos_sales " +
                "    ON pos_sales.number = pos_sales_taking.number " +
                "  LEFT JOIN pos_payment " +
                "    ON pos_payment.sales_id = pos_sales.sales_id " +
                "  LEFT JOIN bank " +
                "    ON bank.bank_id = pos_payment.bank_id " +
                "  LEFT JOIN merchant " +
                "    ON merchant.merchant_id = pos_payment.merchant_id " +
                "  LEFT JOIN payment_method " +
                "    ON payment_method.payment_method_id = pos_payment.payment_method_id" +
                "  LEFT JOIN pos_return_payment " +
                "    ON pos_return_payment.sales_id = pos_sales.sales_id " +
                "WHERE pos_sales_taking.number = :number " +
                "GROUP BY pos_sales_taking_detail.id ";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("number", number);

        return ((List<?>) query.getResultList())
                .stream()
                .map(o -> (Object[]) o)
                .collect(Collectors.groupingBy(
                        o -> ((BigInteger) o[0]).toString()
                ))
                .values()
                .stream()
                .findFirst()
                .map(rows -> {
                    Object[] first = rows.get(0);

                    SalesTakingPlaygroundViewDTO dto = new SalesTakingPlaygroundViewDTO();
                    dto.setId(((BigInteger) first[0]).toString());
                    dto.setSalesReturnId(first[21] != null ? ((BigInteger)first[21]).toString() : "");
                    dto.setNumber((String) first[1]);
                    dto.setDate(((Timestamp) first[2]).toLocalDateTime());
                    dto.setParentName(first[3] != null ? (String) first[3] : "");
                    dto.setParentPhone(first[4] != null ? (String) first[4] : "");
                    dto.setDocStatus((String) first[5]);
                    dto.setAmount(first[6] != null ? ((Number) first[6]).doubleValue() : 0d);
                    dto.setGlobalDiscount(first[7] != null ? ((Number) first[7]).doubleValue() : 0d);
                    dto.setCustomerName((String) first[16]);
                    dto.setCustomerId(((BigInteger)first[17]).toString());
                    dto.setCustomerPhone(first[20] != null ? ((String) first[20]): "");

                    PaymentViewDTO paymentViewDTO = new PaymentViewDTO();
                    paymentViewDTO.setId(first[24] != null ? ((BigInteger)first[24]).toString() : "");
                    paymentViewDTO.setAmount(((Number) first[25]).doubleValue());
                    paymentViewDTO.setCostCardAmount(((Number) first[27]).doubleValue());
                    paymentViewDTO.setCostCardPercent(((Number) first[28]).doubleValue());
                    paymentViewDTO.setBank(first[29] != null ? ((String) first[29]): "");
                    paymentViewDTO.setMerchant(first[30] != null ? ((String) first[30]) : "");
                    paymentViewDTO.setPaymentMethod(first[31] != null ? ((String) first[31]): "");
                    dto.setPayment(paymentViewDTO);

                    ReturnPaymentViewDTO returnPaymentViewDTO = new ReturnPaymentViewDTO();
                    returnPaymentViewDTO.setAmount(((Number) first[26]).doubleValue());
                    dto.setReturnPayment(returnPaymentViewDTO);

                    dto.setSalesTakingDetails(
                            rows.stream()
                                    .map(r -> {
                                        SalesTakingDetailViewDTO d = new SalesTakingDetailViewDTO();
                                        d.setId(((BigInteger) r[13]).toString());
                                        d.setSalesDetailReturnId(r[22] != null ? ((BigInteger)r[22]).toString() : "");
                                        d.setItemMasterId(((BigInteger) r[14]).toString());
                                        d.setItemMasterName((String) r[8]);
                                        d.setItemMasterBarcode((String) r[9]);
                                        d.setPrice(((Number) r[10]).doubleValue());
                                        d.setQty(((Number) r[11]).doubleValue());
                                        d.setTotal(((Number) r[12]).doubleValue());
                                        d.setDiscountItem(((Number) r[15]).doubleValue());
                                        d.setUomId(r[18] != null ? ((BigInteger)r[18]).toString() : "");
                                        d.setConvQty(r[19] != null ? ((Number)r[19]).doubleValue() : 0.0);
                                        d.setPrevReturn(((Number)r[23]).doubleValue());
                                        return d;
                                    })
                                    .collect(Collectors.toList())
                    );

                    return dto;
                })
                .orElse(null);
    }

    @Override
    public Page<SalesTakingPlaygroundViewDTO> getSalesTakingsWithPlayground(String number,String status, Pageable pageable) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT " +
                "  pos_sales_taking.id, " + //0
                "  pos_sales_taking.number," + //1
                "  pos_sales_taking.date," + //2
                "  playground_parent.name AS parent_name," + //3
                "  playground_parent.phone AS parent_phone," + //4
                "  pos_sales_taking.doc_status," + //5
                "  pos_sales_taking.amount," + //6
                "  pos_sales_taking.global_diskon," + //7
                "  customer.name AS customer_name," + //8
                "  customer.customer_id, " + //9
                "  pos_sales_taking.sales_retur_id "); //10
        String fromSql =  "FROM " +
                "  pos_sales_taking " +
                "  JOIN customer " +
                "    ON pos_sales_taking.customer_id = customer.customer_id " +
                "  LEFT JOIN playground_registration " +
                "    ON pos_sales_taking.number = playground_registration.number " +
                "  LEFT JOIN playground_parent " +
                "    ON playground_registration.parent_id = playground_parent.id ";
        StringBuilder whereSqlBuilder = new StringBuilder("WHERE 1 = 1 ");
        if(number != null && !number.isEmpty()) {
            whereSqlBuilder.append("AND pos_sales_taking.number LIKE :number ");
        }
        if(status != null && !status.isEmpty()) {
            whereSqlBuilder.append("AND pos_sales_taking.doc_status = :status ");
        }
        sqlBuilder.append(fromSql);
        sqlBuilder.append(whereSqlBuilder);
        sqlBuilder.append("ORDER BY pos_sales_taking.date DESC ");

        StringBuilder countSqlBuilder = new StringBuilder("SELECT COUNT(*) ");
        countSqlBuilder.append(fromSql);
        countSqlBuilder.append(whereSqlBuilder);

        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        Query countQuery = entityManager.createNativeQuery(countSqlBuilder.toString());

        if(number != null && !number.isEmpty()) {
            query.setParameter("number", "%" + number + "%");
            countQuery.setParameter("number", "%" + number + "%");
        }
        if(status != null && !status.isEmpty()) {
            query.setParameter("status", status);
            countQuery.setParameter("status", status);
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> list = query.getResultList();
        List<SalesTakingPlaygroundViewDTO> dtoList = list.stream()
                .map(r -> {
                    SalesTakingPlaygroundViewDTO dto = new SalesTakingPlaygroundViewDTO();
                    dto.setId(((BigInteger)r[0]).toString());
                    dto.setSalesReturnId(r[10] != null ? ((BigInteger)r[10]).toString() : "");
                    dto.setNumber((String)r[1]);
                    dto.setDate(((Timestamp)r[2]).toLocalDateTime());
                    dto.setParentName(r[3] != null ? (String)r[3] : "");
                    dto.setParentPhone(r[4] != null ? (String)r[4] : "");
                    dto.setCustomerId(((BigInteger)r[9]).toString());
                    dto.setCustomerName((String)r[8]);
                    dto.setDocStatus((String)r[5]);
                    dto.setAmount(r[6] != null ? ((Number)r[6]).doubleValue() : 0.0);
                    dto.setGlobalDiscount(r[7] != null ? ((Number)r[7]).doubleValue() : 0.0);
                    return dto;
                }).collect(Collectors.toList());

        long total = ((Number) countQuery.getSingleResult()).longValue();
        return new PageImpl<>(dtoList, pageable, total);
    }

    @Override
    public List<SalesTakingDetailQtyReturnDTO> getSalesTakingDetailQtyReturnBySalesId(Long salesId) {
        String sql = "SELECT " +
                "  detail.id," +
                "  coalesce(sum(detail_return.qty),0) as qty_return " +
                "FROM " +
                "  pos_sales_taking_detail detail " +
                "  INNER JOIN pos_sales_taking_detail detail_return " +
                "    ON detail.id = detail_return.sales_detail_retur_id " +
                "WHERE detail.sales_id = :salesId " +
                "GROUP BY detail.id";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("salesId", salesId);

        List<Object[]> results = query.getResultList();
        return results.stream().map(r -> new SalesTakingDetailQtyReturnDTO(
                ((BigInteger)r[0]).toString(), // Sales Taking Detail Id
                ((Number)r[1]).doubleValue() // Qty Return
        )).collect(Collectors.toList());
    }
}

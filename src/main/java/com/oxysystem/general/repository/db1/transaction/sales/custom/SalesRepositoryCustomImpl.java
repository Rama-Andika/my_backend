package com.oxysystem.general.repository.db1.transaction.sales.custom;

import com.oxysystem.general.dto.general.dashboard.view.DashboardSummaryGrabDTO;
import com.oxysystem.general.dto.general.dashboard.view.RecentSalesGrabDTO;
import com.oxysystem.general.dto.transaction.sales.view.SalesViewDTO;
import com.oxysystem.general.enums.CashMasterType;
import com.oxysystem.general.enums.grab.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SalesRepositoryCustomImpl implements SalesRepositoryCustom {
    @PersistenceContext(unitName = "db1")
    private EntityManager entityManager;
    @Override
    public DashboardSummaryGrabDTO mappingToDashboardSummaryByCashMasterType(int type) {
        String sql = "SELECT" +
                "  IFNULL(SUM((sd.selling_price * sd.qty) - sd.discount_amount), 0) AS sales_amount," +
                "  IFNULL(SUM(CASE WHEN DATE(DATE) = CURDATE() THEN (sd.selling_price * sd.qty) - sd.discount_amount ELSE 0 END), 0) AS sales_amount_today," +
                "  COUNT(DISTINCT s.sales_id) AS total_sales," +
                "  COUNT(DISTINCT CASE WHEN DATE(DATE) = CURDATE() THEN s.sales_id ELSE NULL END ) AS total_sales_today " +
                "FROM" +
                "  pos_sales_detail sd" +
                "  INNER JOIN pos_sales s ON sd.sales_id = s.sales_id" +
                "  INNER JOIN pos_cash_master cm ON s.cash_master_id = cm.cash_master_id" +
                "  WHERE cm.type = :type";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("type", type);

        Object[] result = (Object[]) query.getSingleResult();

        BigDecimal salesAmount = result[0] != null ? new BigDecimal(result[0].toString()) : BigDecimal.ZERO;
        BigDecimal salesAmountToday = result[1] != null ? new BigDecimal(result[1].toString()) : BigDecimal.ZERO;
        Integer totalSales = result[2] != null ? ((Number) result[2]).intValue() : 0;
        Integer totalSalesToday = result[3] != null ? ((Number) result[3]).intValue() : 0;
        String product = type == CashMasterType.GRAB_POS.ordinal() ? Product.GRAB_MART.name() : Product.GRAB_FOOD.name();

        return new DashboardSummaryGrabDTO(salesAmount, salesAmountToday, totalSales, totalSalesToday, product);
    }

    @Override
    public List<RecentSalesGrabDTO> recentSalesByCashMasterType(int type) {
        String sql = "SELECT " +
                "  l.name AS store, " +
                "  s.number, " +
                "  s.date, " +
                "  SUM(sd.selling_price * sd.qty) AS sales_amount " +
                "FROM " +
                "  pos_sales s " +
                "  INNER JOIN pos_location l " +
                "    ON s.location_id = l.location_id " +
                "  INNER JOIN pos_cash_master cm " +
                "    ON s.cash_master_id = cm.cash_master_id " +
                "  INNER JOIN pos_sales_detail sd " +
                "    ON s.sales_id = sd.sales_id " +
                "WHERE DATE(s.DATE) >= :startDate " +
                "  AND DATE(s.DATE) <= :endDate " +
                "  AND cm.type = :type " +
                "GROUP BY s.sales_id " +
                "ORDER BY s.date DESC " +
                "LIMIT 10";

        Query query = entityManager.createNativeQuery(sql);

        LocalDate startDate = LocalDate.now().minusDays(6);
        LocalDate endDate = LocalDate.now();
        query.setParameter("startDate",startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("type", type);

        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(row -> new RecentSalesGrabDTO(
                        (String) row[0], // Store
                        (String) row[1], // Number
                        row[2] != null ? ((Timestamp) row[2]).toLocalDateTime().toLocalDate() : null, // Date
                        row[3] != null ? new BigDecimal(row[3].toString()) : BigDecimal.ZERO
                )).collect(Collectors.toList());
    }

    @Override
    public List<SalesViewDTO> getSalesGrabSplitOrder(String date, String userId) {
        String sql = "select " +
                "  sales_id," +
                "  number, " +
                "  struk_kasir.grab_merchant_id, " +
                "  struk_kasir.grab_food_merchant_id " +
                "from " +
                "  pos_sales as sales" +
                "  join pos_struk_kasir as struk_kasir " +
                "    on sales.location_id = struk_kasir.location_id " +
                "where sales.user_id = :userId " +
                "  and number regexp '-[0-9]+$' " +
                "  and date(sales.date) = :date ";

        Query query = entityManager.createNativeQuery(sql);

        query.setParameter("userId", userId);
        query.setParameter("date", date);

        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(r -> new SalesViewDTO(
                        ((BigInteger) r[0]).toString(), //sales id
                        r[1] != null ? (String) r[1] : "", //number
                        r[2] != null ? (String) r[2] : "", // grab merchant id
                        r[3] != null ? (String) r[3] : "" // grab food merchant id
                )).collect(Collectors.toList());
    }
}

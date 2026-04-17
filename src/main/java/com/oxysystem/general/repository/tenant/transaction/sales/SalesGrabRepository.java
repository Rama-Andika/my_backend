package com.oxysystem.general.repository.tenant.transaction.sales;

import com.oxysystem.general.model.tenant.transaction.sales.SalesGrab;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SalesGrabRepository extends JpaRepository<SalesGrab, Long> {
    @Query(value = "SELECT s.number, l.name, s.date, s.status FROM pos_sales_grab s " +
                   "INNER JOIN pos_location l " +
                   "ON s.location_id = l.location_id " +
                   "WHERE (:locationId IS NULL OR l.location_id = :locationId) " +
                   "AND (:date IS NULL OR DATE(s.date) = :date) " +
                   "AND (:number IS NULL OR s.number LIKE :number) " +
                   "ORDER BY s.date desc",nativeQuery = true)
    List<Object[]> findSalesGrab(@Param("locationId") Long locationId,
                                  @Param("date")String date,
                                  @Param("number") String number, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM pos_sales_grab s " +
            "INNER JOIN pos_location l " +
            "ON s.location_id = l.location_id " +
            "WHERE (:locationId IS NULL OR l.location_id = :locationId) " +
            "AND (:date IS NULL OR DATE(s.date) = :date) " +
            "AND (:number IS NULL OR s.number LIKE :number) ",nativeQuery = true)
    Optional<Long> countSalesGrab(@Param("locationId") Long locationId,
                            @Param("date")String date,
                            @Param("number") String number);
    
    @Query("SELECT s FROM SalesGrab s LEFT JOIN FETCH s.salesGrabDetails WHERE s.number = :number")
    Optional<SalesGrab> findSalesGrabByNumber(String number);

    @Modifying
    @Query("UPDATE SalesGrab s SET s.date = :date WHERE s.number = :number")
    void updateSalesGrabDateByNumber(@Param("date")LocalDateTime date, @Param("number") String number);

    @Modifying
    @Query("DELETE FROM SalesGrab s WHERE s.number = :number")
    void deleteSalesGrabByNumber(@Param("number") String number);



}

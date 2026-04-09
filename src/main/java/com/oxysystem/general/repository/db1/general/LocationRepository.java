package com.oxysystem.general.repository.db1.general;

import com.oxysystem.general.model.db1.general.Location;
import com.oxysystem.general.repository.db1.general.custom.LocationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>, LocationRepositoryCustom {
    @Query(value = "SELECT " +
            "  l.location_id," +
            "  l.name " +
            "FROM " +
            "  pos_struk_kasir s " +
            "  INNER JOIN pos_location l " +
            "    ON s.location_id = l.location_id " +
            "WHERE l.is_active = 1 " +
            "  AND s.grab_merchant_id IS NOT NULL " +
            "  AND s.grab_merchant_id != '' ORDER BY l.name ", nativeQuery = true)
    List<Object[]> getLocationsGrabMartForSelect();

    @Query(value = "SELECT " +
            "  l.location_id," +
            "  l.name " +
            "FROM " +
            "  pos_struk_kasir s " +
            "  INNER JOIN pos_location l " +
            "    ON s.location_id = l.location_id " +
            "WHERE l.is_active = 1 " +
            "  AND s.grab_food_merchant_id IS NOT NULL " +
            "  AND s.grab_food_merchant_id != '' ORDER BY l.name ", nativeQuery = true)
    List<Object[]> getLocationsGrabFoodForSelect();

    @Query(value = "SELECT " +
            "  l.location_id," +
            "  l.name " +
            "FROM " +
            "  pos_struk_kasir s " +
            "  INNER JOIN pos_location l " +
            "    ON s.location_id = l.location_id " +
            "WHERE l.is_active = 1 " +
            " ORDER BY l.name ", nativeQuery = true)
    List<Object[]> getLocationsForSelect();

    @Query("SELECT l FROM Location l WHERE l.locationId IN :locationIDs")
    List<Location> findAllByIDs(@Param("locationIDs") List<Long> locationIDs);
}

package com.oxysystem.general.repository.db1.posmaster.custom;

import com.oxysystem.general.model.db1.posmaster.CashMaster;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class CashMasterRepositoryCustomImpl implements CashMasterRepositoryCustom {
    @PersistenceContext(unitName = "db1")
    private EntityManager entityManager;
    @Override
    public Optional<CashMaster> findCashMasterByType(Long locationId, int type) {
        String sql = "SELECT * FROM pos_cash_master c " +
                "WHERE c.type = :cashMasterType AND c.location_id = :locationId LIMIT 1";

        Query query = entityManager.createNativeQuery(sql,CashMaster.class);
        query.setParameter("cashMasterType", type);
        query.setParameter("locationId",locationId);

        try {
            CashMaster result = (CashMaster) query.getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty(); // No record found
        }
    }
}

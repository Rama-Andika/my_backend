package com.oxysystem.general.repository.db1.general;

import com.oxysystem.general.model.db1.general.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    @Query(value = "SELECT c.* FROM currency c WHERE LOWER(c.description) = 'rupiah' LIMIT 1", nativeQuery = true)
    Optional<Currency> findIdrCurrency();
}

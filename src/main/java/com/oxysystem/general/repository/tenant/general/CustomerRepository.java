package com.oxysystem.general.repository.tenant.general;

import com.oxysystem.general.model.tenant.general.Customer;
import com.oxysystem.general.repository.tenant.general.custom.CustomerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {
    @Query(value = "SELECT c.* FROM customer c WHERE lower(c.name) = 'public' LIMIT 1", nativeQuery = true)
    Optional<Customer> findCustomerPublic();

    @Query(value = "SELECT c.* FROM customer c WHERE c.code = :code OR c.phone = :code OR c.hp = :code LIMIT 1", nativeQuery = true)
    Optional<Customer> findCustomerByCode(@Param("code") String code);
}

package com.oxysystem.general.service.general;

import com.oxysystem.general.model.db1.general.Customer;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface CustomerService {
    Optional<Customer> findCustomerPublic();
    Optional<Customer> findCustomerByCode(String code);
    Optional<Customer> findById(Long id);
    ResponseEntity<?> getCustomers(String name, int page, int size);
}

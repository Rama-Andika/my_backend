package com.oxysystem.general.service.general.impl;

import com.oxysystem.general.dto.general.customer.view.CustomerViewDTO;
import com.oxysystem.general.model.db1.general.Customer;
import com.oxysystem.general.repository.db1.general.CustomerRepository;
import com.oxysystem.general.response.PaginationResponse;
import com.oxysystem.general.response.SuccessPaginationResponse;
import com.oxysystem.general.service.general.CustomerService;
import com.oxysystem.general.util.ResponseUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> findCustomerPublic() {
        return customerRepository.findCustomerPublic();
    }

    @Override
    public Optional<Customer> findCustomerByCode(String code) {
        return customerRepository.findCustomerByCode(code);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public ResponseEntity<?> getCustomers(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerViewDTO> customerPage = customerRepository.getCustomers(name, pageable);
        PaginationResponse paginationResponse = ResponseUtils.createPaginationResponse(customerPage);
        SuccessPaginationResponse<?> response = new SuccessPaginationResponse<>("success", paginationResponse, customerPage.getContent());
        return ResponseEntity.ok(response);
    }
}

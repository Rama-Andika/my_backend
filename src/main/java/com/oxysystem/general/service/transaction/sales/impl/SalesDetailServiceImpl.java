package com.oxysystem.general.service.transaction.sales.impl;

import com.oxysystem.general.model.tenant.transaction.sales.SalesDetail;
import com.oxysystem.general.repository.tenant.transaction.sales.SalesDetailRepository;
import com.oxysystem.general.service.transaction.sales.SalesDetailService;
import org.springframework.stereotype.Service;

@Service
public class SalesDetailServiceImpl implements SalesDetailService {
    private final SalesDetailRepository salesDetailRepository;

    public SalesDetailServiceImpl(SalesDetailRepository salesDetailRepository) {
        this.salesDetailRepository = salesDetailRepository;
    }

    @Override
    public SalesDetail save(SalesDetail salesDetail) {
        return salesDetailRepository.save(salesDetail);
    }
}

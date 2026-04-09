package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.model.db1.posmaster.GrabmartItemCategory;
import com.oxysystem.general.repository.db1.posmaster.GrabmartItemCategoryRepository;
import com.oxysystem.general.service.posmaster.GrabmartItemCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GrabmartItemCategoryServiceImpl implements GrabmartItemCategoryService {
    private final GrabmartItemCategoryRepository grabmartItemCategoryRepository;

    public GrabmartItemCategoryServiceImpl(GrabmartItemCategoryRepository grabmartItemCategoryRepository) {
        this.grabmartItemCategoryRepository = grabmartItemCategoryRepository;
    }

    @Override
    public Optional<GrabmartItemCategory> findById(Long id) {
        return grabmartItemCategoryRepository.findById(id);
    }

    @Override
    public Optional<GrabmartItemCategory> findByGrabmartSubCategoryId(String id) {
        return grabmartItemCategoryRepository.findByGrabmartSubCategoryId(id);
    }

    @Override
    public List<GrabmartItemCategory> findAllGrabMartItemCategoryByIds(List<Long> ids) {
        return grabmartItemCategoryRepository.findAllGrabMartItemCategoryByIds(ids);
    }

    @Override
    @Transactional(value = "db1TransactionManager", rollbackFor = {Throwable.class})
    public void saveAll(List<GrabmartItemCategory> grabmartItemCategories) {
        grabmartItemCategoryRepository.saveAll(grabmartItemCategories);
    }
}

package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.model.db1.posmaster.GrabmartItemCategory;
import com.oxysystem.general.model.db1.posmaster.GrabmartItemCategoryMapping;

import java.util.List;
import java.util.Optional;

public interface GrabmartItemCategoryService {
    Optional<GrabmartItemCategory> findById(Long id);
    Optional<GrabmartItemCategory> findByGrabmartSubCategoryId(String id);
    List<GrabmartItemCategory> findAllGrabMartItemCategoryByIds(List<Long> ids);
    void saveAll(List<GrabmartItemCategory> grabmartItemCategories);
}

package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.model.db1.posmaster.Unit;
import com.oxysystem.general.repository.db1.posmaster.UnitRepository;
import com.oxysystem.general.service.posmaster.UnitService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UnitServiceImpl implements UnitService {
    private final UnitRepository unitRepository;

    public UnitServiceImpl(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @Override
    public List<Unit> findAllByUomIds(List<Long> uomIds) {
        return unitRepository.findAllByUomIds(uomIds);
    }
}

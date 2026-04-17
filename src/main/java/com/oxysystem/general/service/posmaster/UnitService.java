package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.model.tenant.posmaster.Unit;

import java.util.List;

public interface UnitService {
    List<Unit> findAllByUomIds(List<Long> uomIds);
}

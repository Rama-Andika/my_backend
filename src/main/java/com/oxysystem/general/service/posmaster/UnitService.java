package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.model.db1.posmaster.Unit;

import java.util.List;

public interface UnitService {
    List<Unit> findAllByUomIds(List<Long> uomIds);
}

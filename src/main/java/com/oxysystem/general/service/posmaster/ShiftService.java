package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.model.db1.posmaster.Shift;

import java.util.Optional;

public interface ShiftService {
    Optional<Shift> findFirstShift();
}

package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.model.db1.posmaster.Shift;
import com.oxysystem.general.repository.db1.posmaster.ShiftRepository;
import com.oxysystem.general.service.posmaster.ShiftService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShiftServiceImpl implements ShiftService {
   private final ShiftRepository shiftRepository;

    public ShiftServiceImpl(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    @Override
    public Optional<Shift> findFirstShift() {
        return shiftRepository.findFirstShift();
    }
}

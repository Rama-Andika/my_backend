package com.oxysystem.general.repository.db1.posmaster.custom;

import com.oxysystem.general.dto.posmaster.cogs.view.CogsViewDTO;

import java.util.List;

public interface CogsRepositoryCustom {
    List<CogsViewDTO> cogsByItems(List<Long> itemMasterIds);
}

package com.oxysystem.general.repository.tenant.posmaster.custom;

import com.oxysystem.general.dto.posmaster.grabMartGroup.view.GrabMartItemGroupViewDTO;

import java.util.List;

public interface GrabMartItemGroupRepositoryCustom {
    List<GrabMartItemGroupViewDTO> getGrabMartItemGroups(String name);
}

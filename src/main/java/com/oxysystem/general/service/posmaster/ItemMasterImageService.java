package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.model.db1.posmaster.ItemMasterImage;

import java.util.List;

public interface ItemMasterImageService {
    List<ItemMasterImage> getImagesByItems(List<Long> itemMasterIds);
}

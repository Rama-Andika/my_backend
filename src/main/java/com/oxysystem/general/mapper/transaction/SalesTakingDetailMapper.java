package com.oxysystem.general.mapper.transaction;

import com.oxysystem.general.dto.transaction.salesTakingDetail.view.SalesTakingDetailViewDTO;
import com.oxysystem.general.model.db1.posmaster.ItemMaster;
import com.oxysystem.general.model.db1.transaction.SalesTakingDetail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class SalesTakingDetailMapper {
    public static SalesTakingDetailViewDTO mappingSalesTakingDetailViewDto(SalesTakingDetail salesTakingDetail) {
        SalesTakingDetailViewDTO data = new SalesTakingDetailViewDTO();
        data.setId(String.valueOf(salesTakingDetail.getId()));

        SalesTakingDetail salesTakingDetailOriginal = salesTakingDetail.getSalesTakingDetailReturn();
        if(salesTakingDetailOriginal != null) {
            data.setSalesDetailReturnId(String.valueOf(salesTakingDetailOriginal.getId()));
        }

        ItemMaster itemMaster = salesTakingDetail.getItemMaster();
        if(itemMaster != null){
            data.setItemMasterName(itemMaster.getName());
            data.setItemMasterBarcode(itemMaster.getBarcode());
            data.setItemMasterId(String.valueOf(itemMaster.getItemMasterId()));
        }

        data.setPrice(salesTakingDetail.getSellingPrice());
        data.setQty(salesTakingDetail.getQty());
        data.setTotal(salesTakingDetail.getTotal());
        data.setTotal(salesTakingDetail.getTotal());
        data.setDiscountItem(salesTakingDetail.getDiscountItem());
        data.setUomId(String.valueOf(salesTakingDetail.getUomId()));
        data.setConvQty(salesTakingDetail.getConvQty());

        return data;
    }

    public static List<SalesTakingDetailViewDTO> mappingSalesTakingDetailViewDtos(List<SalesTakingDetail> salesTakingDetails) {
        List<SalesTakingDetailViewDTO> contents = new ArrayList<>();

        if(salesTakingDetails != null && !salesTakingDetails.isEmpty()) {
            for(SalesTakingDetail salesTakingDetail : salesTakingDetails) {
                SalesTakingDetailViewDTO data = mappingSalesTakingDetailViewDto(salesTakingDetail);
                contents.add(data);
            }
        }

        return contents;
    }
}

package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.dto.general.company.view.CompanyViewDTO;
import com.oxysystem.general.dto.posmaster.cogs.view.CogsViewDTO;
import com.oxysystem.general.enums.TypeItem;
import com.oxysystem.general.enums.VendorSystem;
import com.oxysystem.general.model.tenant.general.Location;
import com.oxysystem.general.model.tenant.posmaster.ItemMaster;
import com.oxysystem.general.model.tenant.posmaster.Vendor;
import com.oxysystem.general.model.tenant.posmaster.VendorCommision;
import com.oxysystem.general.model.tenant.posmaster.VendorItem;
import com.oxysystem.general.model.tenant.transaction.sales.Sales;
import com.oxysystem.general.model.tenant.transaction.sales.SalesDetail;
import com.oxysystem.general.repository.tenant.posmaster.CogsRepository;
import com.oxysystem.general.service.posmaster.CogsService;
import com.oxysystem.general.service.posmaster.VendorCommisionService;
import com.oxysystem.general.service.posmaster.VendorItemService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CogsServiceImpl implements CogsService {
    private final CogsRepository cogsRepository;
    private final VendorItemService vendorItemService;
    private final VendorCommisionService vendorCommisionService;

    public CogsServiceImpl(CogsRepository cogsRepository, VendorItemService vendorItemService, VendorCommisionService vendorCommisionService) {
        this.cogsRepository = cogsRepository;
        this.vendorItemService = vendorItemService;
        this.vendorCommisionService = vendorCommisionService;
    }

    @Override
    public CogsViewDTO cogsByItem(Long itemId) {
        List<CogsViewDTO> listCogs = cogsRepository.cogsByItems(Collections.singletonList(itemId));

        return listCogs.get(0);
    }

    @Override
    public List<CogsViewDTO> cogsByItems(List<Long> itemMasterIDs) {
        return cogsRepository.cogsByItems(itemMasterIDs);
    }

    @Override
    public double getLastCogsConsigment(ItemMaster itemMaster, SalesDetail salesDetail, CompanyViewDTO company) {
        double result = 0.0;

        double totalSalesDetail = ((salesDetail.getQty() * salesDetail.getSellingPrice()));

        if(itemMaster.getTypeItem() == TypeItem.KONSINYASI.ordinal()){
            Vendor vendor = itemMaster.getDefaultVendor();
            Sales sales = salesDetail.getSales();
            Location location = sales.getLocation();

            if(vendor == null) return 0.0;
            if(location == null) return 0.0;

            if(vendor.getIsKonsinyasi() == 1){
                if(vendor.getSystem().equals(VendorSystem.HARGA_BELI.getValue())){
                    salesDetail.setStatusKomisi(VendorSystem.HARGA_BELI.getValue());
                    Optional<VendorItem> optVendorItem = vendorItemService.findVendorItemByItemMasterAndVendor(itemMaster.getItemMasterId(), vendor.getVendorId());
                    if(!optVendorItem.isPresent()) return 0.0;

                    VendorItem vendorItem = optVendorItem.get();
                    result = (vendorItem.getLastPrice() / vendorItem.getConvQty()) * salesDetail.getConvQty();

                    if(vendor.getIsPKP()==1){
                        result = result + (result * company.getGovernmentVat() / 100.0);
                    }
                }else if(vendor.getSystem().equals(VendorSystem.HARGA_JUAL.getValue())){
                    salesDetail.setStatusKomisi(VendorSystem.HARGA_JUAL.getValue());
                    double hutang = totalSalesDetail;                 // total sales

                    if(salesDetail.getCompanyId()==1){
                        hutang = hutang * 100/(100 + company.getGovernmentVat());      // cari dppnya jika barang kena pajak
                    }
                    double percentMargin = vendor.getPercentMargin();

                    //Cek vendor commision by lokasi
                    Optional<VendorCommision> optVendorCommision = vendorCommisionService.findVendorCommisionByLocationAndVendor(location.getLocationId(), vendor.getVendorId());

                    if(optVendorCommision.isPresent()){
                        VendorCommision vendorCommision = optVendorCommision.get();
                        percentMargin = vendorCommision.getCommision();
                    }

                    hutang = (100 - percentMargin)/100.0 * hutang;    // cari margin setelah dpp
                    if(vendor.getIsPKP()==1){                                   // jika vendor pkp tambahkan pajak masukkan
                        hutang = hutang + (hutang * company.getGovernmentVat()/100.0);
                    }
                    result = hutang;
                    //result = (100 - vendor.getPercentMargin())/100 * totalSalesDetail;
                    result = result / salesDetail.getQty();
                }
            }
        }

        if (Double.isNaN(result) || Double.isInfinite(result)) {
            result = 0.0;
        }
        return result;
    }
}

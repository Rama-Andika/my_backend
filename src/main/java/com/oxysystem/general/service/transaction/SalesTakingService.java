package com.oxysystem.general.service.transaction;

import com.oxysystem.general.dto.general.children.data.ChildrenDTO;
import com.oxysystem.general.dto.transaction.salesTaking.data.SalesTakingDTO;
import com.oxysystem.general.model.db1.admin.User;
import com.oxysystem.general.model.db1.general.PlaygroundRegistration;
import com.oxysystem.general.model.db1.posmaster.ItemMaster;
import com.oxysystem.general.model.db1.transaction.SalesTaking;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface SalesTakingService {
    SalesTaking saveForPlayground(PlaygroundRegistration playgroundRegistration, Map<Long, ItemMaster> itemMasterMap, List<ChildrenDTO> childrenDTOS, User user);

    void saveAll(List<SalesTaking> salesTakings);

    ResponseEntity<?> getSalesPlaygroundByNumber(String number);

    ResponseEntity<?> getSalesTakingsWithPlayground(String number, String status, int page, int size);

    ResponseEntity<?> save(SalesTakingDTO body);

    ResponseEntity<?> update(Long id, SalesTakingDTO body);

    ResponseEntity<?> saveSalesReturn(Long id, SalesTakingDTO body);

    ResponseEntity<?> updateSalesReturn(Long id, SalesTakingDTO body);
}

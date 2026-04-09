package com.oxysystem.general.controller.posmaster.export;

import com.oxysystem.general.service.posmaster.ItemMasterExportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ItemMasterExportController {
    private final ItemMasterExportService itemMasterExportService;

    public ItemMasterExportController(ItemMasterExportService itemMasterExportService) {
        this.itemMasterExportService = itemMasterExportService;
    }

    @GetMapping("/export/item-master")
    public ResponseEntity<?> toCsv(String name, String barcode, String code, Long itemGroupId, Long itemCategoryId) throws IOException {
        return itemMasterExportService.toCsv(name, barcode, code, itemGroupId, itemCategoryId);
    }
    @GetMapping("/export/template-upload-item-grab")
    public ResponseEntity<?> templateUploadItemGrab() throws IOException {
        return itemMasterExportService.templateUploadItemGrab();
    }
}

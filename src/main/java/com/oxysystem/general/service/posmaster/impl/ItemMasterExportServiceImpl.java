package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.dto.posmaster.itemMaster.view.ItemMasterViewDTO;
import com.oxysystem.general.service.posmaster.ItemMasterExportService;
import com.oxysystem.general.service.posmaster.ItemMasterService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;


import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@Service
public class ItemMasterExportServiceImpl implements ItemMasterExportService {
    private final ItemMasterService itemMasterService;

    public ItemMasterExportServiceImpl(ItemMasterService itemMasterService) {
        this.itemMasterService = itemMasterService;
    }

    @Override
    public ResponseEntity<?> toCsv(String name, String barcode, String code, Long itemGroupId, Long itemCategoryId) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                    "Item Name", "Barcode", "Code", "Category", "Sub Category"
            ));

            int page = 0;
            int size = 10_000;

            Page<ItemMasterViewDTO> itemMasterPage;

            do{
                itemMasterPage = itemMasterService.getItemMasters(name, barcode, code, itemGroupId, itemCategoryId, page, size);
                for(ItemMasterViewDTO data: itemMasterPage.getContent()){
                    csvPrinter.printRecord(
                            data.getItemName(),
                            data.getBarcode(),
                            data.getCode(),
                            data.getItemGroup(),
                            data.getItemCategory()
                    );
                }

                page++;
            }while(!itemMasterPage.isLast());

            csvPrinter.flush();

        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=item-master.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @Override
    public ResponseEntity<Object> templateUploadItemGrab() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
               "barcode", "name"
        ));

        csvPrinter.flush();

        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=template.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(resource.contentLength())
                .body(resource);
    }
}

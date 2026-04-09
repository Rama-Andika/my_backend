package com.oxysystem.general.service.posmaster;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;

public interface ItemMasterExportService {
    ResponseEntity<?> toCsv(String name, String barcode, String code, Long itemGroupId, Long itemCategoryId) throws IOException;
    ResponseEntity<Object> templateUploadItemGrab() throws IOException;
}

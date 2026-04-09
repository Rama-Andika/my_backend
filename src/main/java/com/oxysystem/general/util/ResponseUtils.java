package com.oxysystem.general.util;

import com.oxysystem.general.response.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class

ResponseUtils {
    public static PaginationResponse createPaginationResponse(Page<?> page) {
        int currentPage = page.getNumber() + 1;
        int start = page.getNumber() * page.getSize() + 1;
        PaginationResponse pagination = new PaginationResponse(page.getTotalElements(),page.getTotalPages(),currentPage,start);
        return pagination;
    }
}

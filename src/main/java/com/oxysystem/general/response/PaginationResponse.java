package com.oxysystem.general.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse {
    private long total;
    private int totalPages;
    private int currentPage;
    private int start;
}

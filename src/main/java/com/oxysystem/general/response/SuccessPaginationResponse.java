package com.oxysystem.general.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SuccessPaginationResponse<T> {
    String message;
    PaginationResponse pagination;
    T data;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public SuccessPaginationResponse(String message, PaginationResponse paginationResponse, T data) {
        this.message = message;
        this.pagination = paginationResponse;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
}

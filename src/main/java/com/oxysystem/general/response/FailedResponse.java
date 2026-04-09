package com.oxysystem.general.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FailedResponse<T> {
    private String message;

    private T errors;

    private String requestId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public FailedResponse(String message, T errors) {
        this.message = message;
        this.errors = errors;
        this.requestId = null;
        this.timestamp = LocalDateTime.now();
    }

    public FailedResponse(String message, T errors, String requestId) {
        this.message = message;
        this.errors = errors;
        this.requestId = requestId;
        this.timestamp = LocalDateTime.now();
    }

}

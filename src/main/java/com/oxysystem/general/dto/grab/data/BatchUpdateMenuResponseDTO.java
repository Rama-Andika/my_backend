package com.oxysystem.general.dto.grab.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BatchUpdateMenuResponseDTO {
    private String merchantID;

    private String status;

    private List<Error> errors;
    @Getter
    @Setter
    public static class Error{
        private String entityID;
        private String errMsg;
    }
}

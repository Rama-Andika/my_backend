package com.oxysystem.general.dto.grab.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSelfServeRequestDTO {
    private Partner partner;

    @Getter
    @Setter
    public static class Partner{
        private String merchantID;
    }
}

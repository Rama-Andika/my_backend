package com.oxysystem.general.dto.grab.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oxysystem.general.enums.grab.SyncStateStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class MenuSyncStateRequestDTO {
    private String requestID;

    private String merchantID;

    private String partnerMerchantID;

    private String jobID;

    private Instant updatedAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SyncStateStatus status;

    private List<String> errors;
}

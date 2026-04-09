package com.oxysystem.general.dto.transaction.salesTaking.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.dto.posmaster.payment.data.PaymentDTO;
import com.oxysystem.general.dto.transaction.salesTakingDetail.data.SalesTakingDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesTakingDTO {
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private Long customerId;

    @NotNull(message = "User is required")
    private Long userId;
    private String docStatus;
    private Double amount;
    private Double globalDiskon;
    private String locationId;

    @Valid
    @NotEmpty(message = "Detail is required")
    private List<SalesTakingDetailDTO> salesTakingDetails;
    private PaymentDTO payment;
}

package com.oxysystem.general.dto.transaction.salesTaking.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oxysystem.general.dto.transaction.payment.view.PaymentViewDTO;
import com.oxysystem.general.dto.transaction.returnPayment.view.ReturnPaymentViewDTO;
import com.oxysystem.general.dto.transaction.salesTakingDetail.view.SalesTakingDetailViewDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalesTakingPlaygroundViewDTO {
    private String id;

    private String salesReturnId;

    private String number;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private String parentName;

    private String customerId;

    private String customerName;

    private String customerPhone;

    private String parentPhone;

    private String docStatus;

    private Double amount;

    private Double globalDiscount;

    private PaymentViewDTO payment;

    private ReturnPaymentViewDTO returnPayment;

    private List<SalesTakingDetailViewDTO> salesTakingDetails;
}

package com.oxysystem.general.dto.transaction.sales.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class SalesGrabDTO {
    private String number;
    private String storeId;
    private String store;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private String status;
    private List<SalesGrabDetailDTO> details;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SalesGrabDetailDTO{
        private String salesGrabDetailId;
        private Integer isVerified;
        private String itemId;
        private String itemName;
        private String itemCode;
        private String itemBarcode;
        private Double price;
        private Double qty;
        private Double total;
        private Double verifiedQty;
    }

    public SalesGrabDTO(String number, String store, LocalDateTime date, String status) {
        this.number = number;
        this.store = store;
        this.date = date;
        this.status = status;
    }
}

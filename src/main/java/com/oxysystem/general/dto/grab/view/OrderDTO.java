package com.oxysystem.general.dto.grab.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {
    private Boolean more;
    private List<DetailOrder> orders;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DetailOrder{
        private String orderNumber;
        private String storeId;
        private String store;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime orderDate;

        private Integer amount;
        private Integer discountAmount;
        private String grabStatus;
        private String status;
        private List<Item> items;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class Item{
            private String id;
            private String name;
            private String code;
            private String barcode;
            private Integer qty;
            private Integer price;
        }
    }
}

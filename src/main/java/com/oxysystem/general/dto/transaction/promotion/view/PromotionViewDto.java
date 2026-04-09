package com.oxysystem.general.dto.transaction.promotion.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromotionViewDto {
    private String id;
    private String number;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer type;
    private String description;
    private String status;
}

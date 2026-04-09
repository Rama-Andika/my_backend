package com.oxysystem.general.model.db1.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pos_struk_kasir")
public class StrukKasir {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "struk_kasir_id")
    private Long strukKasirId;

    @Column(name = "location_id", nullable = false, length = 20)
    private Long locationId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column(name = "user_id", nullable = false, length = 20)
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String header = "";

    @Column(columnDefinition = "TEXT")
    private String footer = "";

    @Column(name = "footer_1", columnDefinition = "TEXT")
    private String footer1 = "";

    @Column(name = "footer_2", columnDefinition = "TEXT")
    private String footer2 = "";

    @Column(name = "api_key")
    private String apiKey = "";

    @Column(name = "grab_merchant_id", length = 120)
    private String grabMerchantId = "";

    @Column(name = "grab_food_merchant_id")
    private String grabFoodMerchantId = "";
}

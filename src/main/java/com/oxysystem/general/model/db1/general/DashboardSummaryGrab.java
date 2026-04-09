package com.oxysystem.general.model.db1.general;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dashboard_summary_grab")
public class DashboardSummaryGrab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "dashboard_summary_grab_id")
    private Long dashboardSummaryId;

    @Column(name = "sales_amount")
    private BigDecimal salesAmount = BigDecimal.valueOf(0);

    @Column(name = "sales_amount_today")
    private BigDecimal salesAmountToday = BigDecimal.valueOf(0);

    @Column(name = "total_sales")
    private Integer totalSales = 0;

    @Column(name = "total_sales_today")
    private Integer totalSalesToday = 0;

    private String product;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void onPrePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onPreUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}

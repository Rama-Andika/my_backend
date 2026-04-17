package com.oxysystem.general.model.tenant.transaction.sales;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.enums.DocumentStatus;
import com.oxysystem.general.model.tenant.general.Customer;
import com.oxysystem.general.model.tenant.general.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pos_sales_grab",
    indexes = {
        @Index(name = "idx_location_date_number", columnList = "location_id, date, number")
    }
)
public class SalesGrab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "sales_grab_id")
    private Long salesGrabId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    @Column(length = 70,unique = true)
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    private Double amount = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "salesGrab", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SalesGrabDetail> salesGrabDetails;
}

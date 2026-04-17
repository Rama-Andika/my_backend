package com.oxysystem.general.model.tenant.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pos_shift")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "shift_id")
    private Long shiftId;

    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "end_time")
    private LocalDate endTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "start_time")
    private LocalDate startTime;

    @Column(name = "start_hours")
    private Integer startHours;

    @Column(name = "start_minutes")
    private Integer startMinutes;

    @Column(name = "end_hours")
    private Integer endHours;

    @Column(name = "end_minutes")
    private Integer endMinutes;

    @Column(name = "company_id")
    private Long companyId;
}

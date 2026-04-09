package com.oxysystem.general.dto.transaction.playgroundSession.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaygroundSessionReportDTO {
    private String registrationNumber;
    private String parentName;
    private String parentPhone;
    private String childName;
    private String code;
    private Double duration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}

package com.oxysystem.general.dto.transaction.playgroundSession.view;

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
public class PlaygroundSessionViewDTO {
    private String sessionId;
    private String childName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    private String status;
    private long minutesLeft;

    private String number;
    private String parentName;
    private String code;
    private String parentPhone;
    private String parentCode;

    public PlaygroundSessionViewDTO(String sessionId, String childName, LocalDateTime endTime, String status, long minutesLeft, String number, String parentName) {
        this.sessionId = sessionId;
        this.childName = childName;
        this.endTime = endTime;
        this.status = status;
        this.minutesLeft = minutesLeft;
        this.number = number;
        this.parentName = parentName;
    }
}

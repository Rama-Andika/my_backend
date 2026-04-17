package com.oxysystem.general.dto.transaction.playgroundSession.data;

import com.oxysystem.general.model.tenant.general.PlaygroundChildren;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaygroundSessionDTO {
    private PlaygroundChildren playgroundChildren;

    @Column(name = "start_time", columnDefinition = "DATETIME(0)")
    private LocalDateTime startTime;

    @Column(name = "end_time", columnDefinition = "DATETIME(0)")
    private LocalDateTime endTime;

    @Column(name = "is_active")
    private Integer isActive = 0;
}

package com.oxysystem.general.model.tenant.transaction;

import com.oxysystem.general.model.tenant.BaseEntity;
import com.oxysystem.general.model.tenant.admin.User;
import com.oxysystem.general.model.tenant.general.PlaygroundChildren;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "playground_session", indexes = {
        @Index(name = "idx_end_time", columnList = "end_time"),
        @Index(name = "idx_active_end", columnList = "is_active,end_time")
})
public class PlaygroundSession extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private PlaygroundChildren playgroundChildren;

    @Column(name = "start_time", columnDefinition = "DATETIME(0)")
    private LocalDateTime startTime;

    @Column(name = "end_time", columnDefinition = "DATETIME(0)")
    private LocalDateTime endTime;

    @Column(name = "is_active")
    private Integer isActive = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String code = "";
}

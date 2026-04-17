package com.oxysystem.general.model.tenant.transaction;

import com.oxysystem.general.enums.grab.SyncStateStatus;
import com.oxysystem.general.model.tenant.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "grab_menu_sync_status")
public class GrabMenuSyncStatus extends BaseEntity {
    @Column(name = "merchant_id")
    private String merchantId;

    @Enumerated(EnumType.STRING)
    private SyncStateStatus status;

    private String product;
}

package com.oxysystem.general.model.tenant.posmaster;


import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.model.tenant.BaseEntity;
import com.oxysystem.general.model.tenant.general.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "grab_packaging_item")
@Entity
public class GrabPackagingItem extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_master_id")
    private ItemMaster itemMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
}

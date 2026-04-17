package com.oxysystem.general.model.tenant.general;

import com.oxysystem.general.model.tenant.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "playground_parent")
public class PlaygroundParent extends BaseEntity {
    private String name;

    private String phone;

    private String code;

    @OneToMany(mappedBy = "playgroundParent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlaygroundChildren> children;
}

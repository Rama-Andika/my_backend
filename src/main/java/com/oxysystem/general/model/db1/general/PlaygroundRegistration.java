package com.oxysystem.general.model.db1.general;

import com.oxysystem.general.model.db1.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "playground_registration")
@Entity
public class PlaygroundRegistration extends BaseEntity {
    @Column(unique = true)
    private String number;

    private String prefix;

    private Integer counter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private PlaygroundParent parent;
}

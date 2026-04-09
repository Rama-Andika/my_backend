package com.oxysystem.general.model.db1.general;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Entity
@Table(name = "playground_children")
public class PlaygroundChildren extends BaseEntity {
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private PlaygroundParent playgroundParent;

    private String name;

    private Integer age = 0;
}

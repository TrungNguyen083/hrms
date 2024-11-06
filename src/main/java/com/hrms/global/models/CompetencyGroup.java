package com.hrms.global.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompetencyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competency_group_id")
    private Integer id;

    @Column(name = "competency_group_name")
    private String competencyGroupName;

    @Column(name = "description")
    private String description;

    @Column(name = "weight")
    private Integer weight;

    public CompetencyGroup(Integer id) {
        this.id = id;
    }
}

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
public class ProficiencyLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proficiency_level_id")
    private Integer id;

    @Column(name = "proficiency_level_name")
    private String proficiencyLevelName;

    @Column(name = "proficiency_level_description")
    private String proficiencyLevelDescription;

    @Column(name = "score")
    private Float score;

    public ProficiencyLevel(Integer id) {
        this.id = id;
    }
}


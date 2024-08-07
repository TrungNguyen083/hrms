package com.hrms.global.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PositionLevelSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_level_skill_set_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "position_id", referencedColumnName = "position_id")
    private Position position;

    @ManyToOne
    @JoinColumn(name = "job_level_id", referencedColumnName = "job_level_id")
    private JobLevel jobLevel;

    @ManyToOne
    @JoinColumn(name = "skill_id", referencedColumnName = "skill_id")
    private Skill skill;

    @ManyToOne
    @JoinColumn(name = "proficiency_level_id", referencedColumnName = "proficiency_level_id")
    private ProficiencyLevel proficiencyLevel;
}

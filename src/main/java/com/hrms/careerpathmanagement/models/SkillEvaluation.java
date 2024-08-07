package com.hrms.careerpathmanagement.models;


import com.hrms.employeemanagement.models.Employee;
import com.hrms.employeemanagement.models.Skill;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_set_evaluation_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competency_cycle_id")
    private CompetencyCycle competencyCycle;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Column(name = "self_score")
    private Integer selfScore;

    @Column(name = "evaluator_score")
    private Integer evaluatorScore;

    @Column(name = "final_score")
    private Integer finalScore;
}

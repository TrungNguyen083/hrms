package com.hrms.careerpathmanagement.models;

import com.hrms.employeemanagement.models.Employee;
import com.hrms.global.models.Competency;
import com.hrms.global.models.EvaluateCycle;
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
public class CompetencyEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competency_evaluation_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluate_cycle_id", referencedColumnName = "evaluate_cycle_id")
    private EvaluateCycle evaluateCycle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competency_id", referencedColumnName = "competency_id")
    private Competency competency;

    @Column(name = "self_evaluation")
    private Float selfEvaluation;

    @Column(name = "supervisor_evaluation")
    private Float supervisorEvaluation;

    @Column(name = "final_evaluation")
    private Float finalEvaluation;
}

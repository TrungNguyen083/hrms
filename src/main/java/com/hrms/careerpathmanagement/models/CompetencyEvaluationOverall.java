package com.hrms.careerpathmanagement.models;

import com.hrms.employeemanagement.models.Employee;
import com.hrms.global.models.EvaluateCycle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompetencyEvaluationOverall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_overall_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "employee_status")
    private String employeeStatus;

    @Column(name = "evaluator_status")
    private String evaluatorStatus;

    @Column(name = "final_status")
    private String finalStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluate_cycle_id")
    private EvaluateCycle evaluateCycle;

    @Column(name = "score")
    private Float score;

    @Column(name = "last_updated")
    private Date lastUpdated;

    @Column(name = "completed_date")
    private Date completedDate;

    @Column(name = "is_self_submitted")
    private Boolean isSelfSubmitted;

    @Column(name = "is_evaluator_submitted")
    private Boolean isEvaluatorSubmitted;

    @Column(name = "is_final_submitted")
    private Boolean isFinalSubmitted;
}

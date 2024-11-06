package com.hrms.careerpathmanagement.models;

import com.hrms.employeemanagement.models.Employee;
import com.hrms.global.models.EvaluateCycle;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Column(name = "self_assessment")
    private Float selfAssessment;

    @Column(name = "supervisor_assessment")
    private Float supervisorAssessment;

    @Column(name = "final_assessment")
    private Float finalAssessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluate_cycle_id")
    private EvaluateCycle evaluateCycle;

    @Column(name = "last_updated")
    private Date lastUpdated;

    @Column(name = "completed_date")
    private Date completedDate;
}

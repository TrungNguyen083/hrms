package com.hrms.performancemanagement.model;

import com.hrms.employeemanagement.models.Employee;
import com.hrms.global.models.EvaluateCycle;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "performance_evaluation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_evaluation_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "self_assessment")
    private Float selfAssessment;

    @Column(name = "supervisor_assessment")
    private Float supervisorAssessment;

    @Column(name = "final_assessment")
    private Float finalAssessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluate_cycle_id")
    private EvaluateCycle evaluateCycle;

    @Column(name = "potential_score")
    private Float potentialScore;

    @Column(name = "status")
    private String status;

    @Column(name = "completed_date")
    private Date completedDate;

    @Column(name = "last_updated")
    private Date lastUpdated;
}

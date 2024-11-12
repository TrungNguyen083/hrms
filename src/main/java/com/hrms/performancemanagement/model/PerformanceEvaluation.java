package com.hrms.performancemanagement.model;

import com.hrms.employeemanagement.models.Employee;
import com.hrms.global.models.EvaluateCycle;
import com.hrms.global.models.Question;
import jakarta.persistence.*;
import lombok.*;

@Entity
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
    @JoinColumn(name = "evaluate_cycle_id")
    private EvaluateCycle evaluateCycle;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "question")
    private Question question;

    @Column(name = "self_evaluation")
    private Float selfEvaluation;

    @Column(name = "supervisor_evaluation")
    private Float supervisorEvaluation;

    @Column(name = "final_evaluation")
    private Float finalEvaluation;

    @Column(name = "self_comment")
    private String selfComment;

    @Column(name = "supervisor_comment")
    private String supervisorComment;

    @Column(name = "final_comment")
    private String finalComment;
}

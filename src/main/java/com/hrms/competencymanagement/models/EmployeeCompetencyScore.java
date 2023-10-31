package com.hrms.competencymanagement.models;

import com.hrms.employeemanagement.models.Employee;
import jakarta.persistence.*;

@Entity
@Table(name = "employee_competency_score")
public class EmployeeCompetencyScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_competency_score_id")
    private Integer employeeCompetencyScoreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private Float score;
}

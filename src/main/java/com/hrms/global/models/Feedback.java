package com.hrms.global.models;

import com.hrms.employeemanagement.models.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "feedbacker_id", referencedColumnName = "employee_id")
    private Employee feedBacker;

    @ManyToOne
    @JoinColumn(name = "evaluate_cycle_id")
    private EvaluateCycle evaluateCycle;

    @JoinColumn(name = "content")
    private String content;

    @JoinColumn(name = "created_at")
    private Date createdAt;
}

package com.hrms.performancemanagement.model;

import com.hrms.careerpathmanagement.models.CategoryQuestion;
import com.hrms.employeemanagement.models.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "performance_cycle_id", referencedColumnName = "performance_cycle_id")
    private PerformanceCycle cycle;

    @ManyToOne
    @JoinColumn(name = "category_question_id", referencedColumnName = "category_question_id")
    private CategoryQuestion categoryQuestion;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;

    @Column(name = "answer_result")
    private Float answerResult;

    private Timestamp savedAt;

    @Column(name = "is_final")
    private Boolean isFinal;
}

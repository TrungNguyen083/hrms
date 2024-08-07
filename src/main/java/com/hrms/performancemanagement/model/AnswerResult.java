package com.hrms.performancemanagement.model;

import com.hrms.global.models.CategoryQuestion;
import com.hrms.employeemanagement.models.Employee;
import com.hrms.global.models.EvaluateCycle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_result_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "evaluate_cycle_id", referencedColumnName = "evaluate_cycle_id")
    private EvaluateCycle cycle;

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

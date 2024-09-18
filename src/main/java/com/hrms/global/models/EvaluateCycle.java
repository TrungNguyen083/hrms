package com.hrms.global.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "evaluate_cycle")
public class EvaluateCycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluate_cycle_id")
    private Integer id;

    @Column(name = "evaluate_cycle_name")
    private String evaluateCycleName;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "year")
    private Integer year;

    @Column(name = "status")
    @Builder.Default
    private String status = "Not Start";

    @Column(name = "initial_date")
    private Date initialDate;

    public EvaluateCycle(Integer id) {
        this.id = id;
    }
}

package com.hrms.global.models;

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
@Table(name = "evaluate_time_line")
public class EvaluateTimeLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluate_time_line_id")
    private Integer id;

    @Column(name = "evaluate_time_line_name")
    private String evaluateTimeLineName;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "due_date")
    private Date dueDate;

    @ManyToOne
    @JoinColumn(name = "evaluate_cycle_id")
    private EvaluateCycle evaluateCycle;
    
    @Column(name = "is_done")
    private Boolean isDone = false;

}

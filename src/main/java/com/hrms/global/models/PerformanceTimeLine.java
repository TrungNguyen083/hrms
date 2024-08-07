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
public class PerformanceTimeLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_time_line_id")
    private Integer id;

    @Column(name = "performance_time_line_name")
    private String performanceTimeLineName;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "due_date")
    private Date dueDate;

    @ManyToOne
    @JoinColumn(name = "performance_cycle_id")
    private PerformanceCycle performanceCycle;

    @Column(name = "is_done")
    private Boolean isDone = false;
}

package com.hrms.performancemanagement.model;

import com.hrms.careerpathmanagement.models.Template;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceCycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_cycle_id")
    private Integer performanceCycleId;

    @Column(name = "performance_cycle_name")
    private String performanceCycleName;

    @Column(name = "description")
    private String description;

    @Column(name = "performance_cycle_start_date")
    private Date performanceCycleStartDate;

    @Column(name = "performance_cycle_end_date")
    private Date performanceCycleEndDate;

    @Column(name = "is_done")
    private Boolean isDone = false;

    @Column(name = "status")
    private String status = "Not Start";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private Template template;

    @Column(name = "performance_weightage")
    private Float performanceWeightage;

    @Column(name = "goal_weightage")
    private Float goalWeightage;

    @Column(name = "initial_date")
    private Date initialDate;

    // For modification time
    @Column(name = "modification_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date modificationTime;

    // For insertion time
    @Column(name = "insertion_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date insertionTime;
}

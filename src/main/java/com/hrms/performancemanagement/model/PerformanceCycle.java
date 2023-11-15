package com.hrms.performancemanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceCycle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "performance_cycle_id")
    private Integer performanceCycleId;

    @Column(name = "performance_cycle_name")
    private String performanceCycleName;

    @Column(name = "performance_cycle_start_date")
    private Date performanceCycleStartDate;

    @Column(name = "performance_cycle_end_date")
    private Date performanceCycleEndDate;

    @Column(name = "is_done")
    private Boolean isDone;

    @Column(name = "status")
    private String status;

    // For modification time
    @Column(name = "modification_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date modificationTime;

    // For insertion time
    @Column(name = "insertion_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date insertionTime;
}

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
public class Competency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competency_id")
    private Integer id;

    @Column(name = "competency_name")
    private String competencyName;

    @Column(name = "description")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competency_group_id")
    private CompetencyGroup competencyGroup;

    @Column(name = "ordered")
    private Integer ordered;

    // For modification time
    @Column(name = "modification_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationTime;

    // For insertion time
    @Column(name = "insertion_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertionTime;
}


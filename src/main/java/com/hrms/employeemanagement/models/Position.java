package com.hrms.employeemanagement.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private int id;
    @Column(name = "position_name")
    private String positionName;
    @Column(name = "has_level")
    private Boolean hasLevel;
    @Column(name = "has_department")
    private Boolean hasDepartment;
    // For modification time
    @Column(name = "modification_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationTime;

    // For insertion time
    @Column(name = "insertion_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertionTime;
    public Position(Integer id) {
        this.id = id;
    }
}

package com.hrms.employeemanagement.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_level_id")
    private int id;
    @Column(name = "job_level_name")
    private String jobLevelName;

    public JobLevel(Integer id) {
        this.id = id;
    }
}

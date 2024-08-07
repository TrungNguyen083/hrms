package com.hrms.careerpathmanagement.models;

import com.hrms.employeemanagement.models.Employee;
import com.hrms.global.models.JobLevel;
import com.hrms.global.models.Position;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCareerPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_career_path_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne
    @JoinColumn(name = "job_level_id")
    private JobLevel jobLevel;

    @Column(name = "ordered")
    Integer ordered;

    @Column(name = "match_percentage")
    Float matchPercentage;
}

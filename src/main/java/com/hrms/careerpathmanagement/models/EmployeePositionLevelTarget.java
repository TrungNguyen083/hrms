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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePositionLevelTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_position_level_target_id")
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
}

package com.hrms.careerpathmanagement.models;

import com.hrms.employeemanagement.models.Employee;
import com.hrms.global.models.CompetencyCycle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "competency_cycle_id", referencedColumnName = "competency_cycle_id")
    private CompetencyCycle competencyCycle;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "progress")
    private Float progress;

    @Column(name = "is_approved", columnDefinition = "boolean default false")
    private Boolean isApproved;

    @Column(name = "approved_at")
    private Date approvedAt;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    private String status;
}

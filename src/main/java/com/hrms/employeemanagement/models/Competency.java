package com.hrms.employeemanagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Competency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competency_id")
    private int ID;
    @Column(name = "competency_name")
    private String competencyName;
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "competency_group_id")
    private CompetencyGroup competencyGroup;
}

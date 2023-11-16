package com.hrms.employeemanagement.models;

import com.hrms.careerpathmanagement.models.Competency;
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
public class SkillSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_set_id")
    private int id;

    @Column(name = "skill_set_name")
    private String skillSetName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competency_id")
    private Competency competency;

    @Column(name = "description")
    private String description;

    @Column(name = "modification_time")
    private Date modificationTime;

    @Column(name = "insertion_time")
    private Date insertionTime;
}
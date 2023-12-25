package com.hrms.employeemanagement.models;

import com.hrms.careerpathmanagement.models.Competency;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_set_id")
    private Integer id;

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

    public SkillSet(Integer id) {
        this.id = id;
    }
}
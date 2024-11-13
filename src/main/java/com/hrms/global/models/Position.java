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
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Integer id;
    @Column(name = "position_name")
    private String positionName;
    @Column(name = "has_level")
    private Boolean hasLevel;
    @Column(name = "has_department")
    private Boolean hasDepartment;
    @Column(name = "has_evaluation")
    private Boolean hasEvaluation;
    public Position(Integer id) {
        this.id = id;
    }
}

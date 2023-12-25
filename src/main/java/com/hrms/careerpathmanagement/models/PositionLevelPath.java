package com.hrms.careerpathmanagement.models;

import com.hrms.employeemanagement.models.PositionLevel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PositionLevelPath {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "current_position_level_id", referencedColumnName = "position_level_id")
    private PositionLevel current;

    @ManyToOne
    @JoinColumn(name = "next_position_level_id", referencedColumnName = "position_level_id")
    private PositionLevel next;
}

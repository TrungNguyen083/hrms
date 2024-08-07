package com.hrms.careerpathmanagement.models;

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
public class PositionLevelPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_level_path_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "current_position_id", referencedColumnName = "position_id")
    private Position positionCurrent;

    @ManyToOne
    @JoinColumn(name = "current_level_id", referencedColumnName = "job_level_id")
    private JobLevel jobLevelCurrent;

    @ManyToOne
    @JoinColumn(name = "next_position_id", referencedColumnName = "position_id")
    private Position positionNext;

    @ManyToOne
    @JoinColumn(name = "next_level_id", referencedColumnName = "job_level_id")
    private JobLevel jobLevelNext;
}

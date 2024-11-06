package com.hrms.careerpathmanagement.dto;

import com.hrms.global.models.Skill;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompetencyForm {
    private CompetencyRating competency;
    private List<Skill> skills;
    private Integer competencyGroupId;
}

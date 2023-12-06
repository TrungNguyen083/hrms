package com.hrms.careerpathmanagement.input;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompetencyEvaluationInput {
    private Integer employeeId;
    private Integer competencyCycleId;
    private Boolean isSubmitted;
    private Float score;
    private List<SkillSetScoreInput> skillSetScores;
}

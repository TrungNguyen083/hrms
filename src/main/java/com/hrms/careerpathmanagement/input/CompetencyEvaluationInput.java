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
    private Integer evaluateCycleId;
    private Boolean isSubmitted;
    private Float score;
    private List<SkillScoreInput> skillScores;
}

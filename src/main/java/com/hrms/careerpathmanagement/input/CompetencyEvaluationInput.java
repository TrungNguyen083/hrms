package com.hrms.careerpathmanagement.input;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompetencyEvaluationInput {
    private Integer employeeId;
    private Integer cycleId;
    private Boolean isSubmit;
    private List<ICompetencySubmit> competencyRating;
}

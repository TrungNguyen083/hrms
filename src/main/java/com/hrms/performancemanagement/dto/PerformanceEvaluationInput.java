package com.hrms.performancemanagement.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceEvaluationInput {
    private Integer employeeId;
    private Integer cycleId;
    private Boolean isSubmit;
    private List<QuestionSubmit> questionRating;
}

package com.hrms.performancemanagement.input;

import com.hrms.performancemanagement.input.QuestionSubmit;
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
    private Integer potential;
    private Boolean isSubmit;
    private List<QuestionSubmit> questionRating;
}

package com.hrms.performancemanagement.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceQuestionRating {
    private Integer questionId;
    private String questionName;
    private String questionDescription;
    private String comment;
    private Float rating;
    private Integer categoryId;
}

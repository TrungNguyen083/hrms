package com.hrms.performancemanagement.input;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionSubmit {
    private Integer questionId;
    private String comment;
    private Integer rating;
}

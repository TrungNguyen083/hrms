package com.hrms.careerpathmanagement.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EvaluationTitle {
    private String title;
    private String status;
    private String startDate;
    private String dueDate;
}

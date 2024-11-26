package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompareGoalItem {
    private String goalName;
    private Integer goalProgress;
}

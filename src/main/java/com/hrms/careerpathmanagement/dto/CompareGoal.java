package com.hrms.careerpathmanagement.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompareGoal {
    private String firstName;
    private String lastName;
    private List<CompareGoalItem> goals;
}

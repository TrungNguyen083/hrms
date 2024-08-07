package com.hrms.careerpathmanagement.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EvaluateCycleInput {
    private String cycleName;
    private String description;
    private String startDate;
    private String dueDate;
    private String evaluatorType;
}

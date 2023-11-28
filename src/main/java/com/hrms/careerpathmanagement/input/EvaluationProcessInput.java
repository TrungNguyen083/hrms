package com.hrms.careerpathmanagement.input;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluationProcessInput {
    private List<TimeLineInput> timeLines;
    private String initialDate;
    private Integer cycleId;
}

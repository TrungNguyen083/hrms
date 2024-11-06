package com.hrms.global.input;

import com.hrms.global.input.TimeLineInput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EvaluateCycleInput {
    private String cycleName;
    private String description;
    private String startDate;
    private String endDate;
    private String initialDate;
    private List<TimeLineInput> timeLines;
}

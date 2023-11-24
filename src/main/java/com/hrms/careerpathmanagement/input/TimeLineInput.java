package com.hrms.careerpathmanagement.input;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class TimeLineInput {
    private String timeLineName;
    private String startDate;
    private String dueDate;
}

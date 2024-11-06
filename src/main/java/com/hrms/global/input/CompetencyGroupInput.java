package com.hrms.global.input;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompetencyGroupInput {
    private String competencyGroupName;
    private String description;
}

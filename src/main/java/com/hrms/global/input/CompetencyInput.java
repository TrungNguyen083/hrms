package com.hrms.global.input;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompetencyInput {
    private String competencyName;
    private String description;
    private Integer competencyGroupId;
}

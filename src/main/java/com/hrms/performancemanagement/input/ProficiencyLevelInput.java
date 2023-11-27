package com.hrms.performancemanagement.input;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProficiencyLevelInput {
    private String name;
    private String description;
    private Float score;
}

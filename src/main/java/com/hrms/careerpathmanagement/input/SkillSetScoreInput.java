package com.hrms.careerpathmanagement.input;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillSetScoreInput {
    private Integer skillSetId;
    private Integer score;
}

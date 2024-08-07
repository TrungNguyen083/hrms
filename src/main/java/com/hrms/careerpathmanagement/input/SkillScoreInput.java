package com.hrms.careerpathmanagement.input;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillScoreInput {
    private Integer skillId;
    private Integer score;
}

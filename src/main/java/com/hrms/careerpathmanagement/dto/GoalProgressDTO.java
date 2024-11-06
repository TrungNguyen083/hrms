package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoalProgressDTO {
    private Integer employeeId;
    private String goalName;
    private String profileImage;
    private Float goalProgress;
}

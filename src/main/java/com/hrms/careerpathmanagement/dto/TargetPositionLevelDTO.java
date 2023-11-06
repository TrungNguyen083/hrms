package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TargetPositionLevelDTO {
    Integer positionLevelId;
    String title;
    Float matchPercentage;
}
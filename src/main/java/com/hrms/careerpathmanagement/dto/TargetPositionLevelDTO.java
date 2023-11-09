package com.hrms.careerpathmanagement.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TargetPositionLevelDTO {
    Integer positionLevelId;
    String title;
    Float matchPercentage;
    List<TargetPositionLevelDTO> nextsPosLevel;
}
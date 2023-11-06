package com.hrms.careerpathmanagement.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PositionLevelNode {
    String title;
    Float matchPercentage;
    List<PositionLevelNode> nextPositionLevels;
}

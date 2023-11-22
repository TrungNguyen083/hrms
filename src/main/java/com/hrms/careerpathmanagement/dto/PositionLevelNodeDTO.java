package com.hrms.careerpathmanagement.dto;

import lombok.*;

import java.util.LinkedList;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PositionLevelNodeDTO {
    Integer id;
    String title;
    LinkedList<PositionLevelNodeDTO> nextPositionLevels;
}
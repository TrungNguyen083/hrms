package com.hrms.careerpathmanagement.dto;

import lombok.*;

import java.util.LinkedList;
import java.util.List;

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
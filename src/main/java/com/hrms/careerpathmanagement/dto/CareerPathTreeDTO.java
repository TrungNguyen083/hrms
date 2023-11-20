package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CareerPathTree {
    String title;
    PositionLevelNodeDTO root;
}

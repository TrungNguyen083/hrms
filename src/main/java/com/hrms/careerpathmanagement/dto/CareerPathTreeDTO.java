package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CareerPathTreeDTO {
    String title;
    PositionLevelNodeDTO root;
}

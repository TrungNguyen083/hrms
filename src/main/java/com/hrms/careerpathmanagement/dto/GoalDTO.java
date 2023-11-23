package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalDTO {
    Integer id;
    Integer title;
    String competencyCycleName;
    Float progress;
}

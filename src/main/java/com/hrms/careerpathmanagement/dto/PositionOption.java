package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PositionOption {
    private Integer positionId;
    private String positionLevelName;
    private Integer skillNo;
}

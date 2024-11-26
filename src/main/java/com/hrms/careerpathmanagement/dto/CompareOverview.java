package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompareOverview {
    private String profileImage;
    private String firstName;
    private String lastName;
    private String currentPosition;
    private String targetPosition;
}

package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeFeedback {
    private String feedBackerProfileImage;
    private String feedBackerFirstName;
    private String feedBackerLastName;
    private String content;
    private String createdAt;
}

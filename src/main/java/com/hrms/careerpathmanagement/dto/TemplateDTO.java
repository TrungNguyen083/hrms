package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemplateDTO {
    private Integer id;
    private String templateName;
    private String templateDescription;
    private String createdAt;
    private Integer createdById;
    private String createdBy;
}

package com.hrms.performancemanagement.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateDTO {
    private Integer id;
    private String templateName;
    private String templateDescription;
    private String createdAt;
    private Integer createdBy;
}

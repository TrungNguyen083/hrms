package com.hrms.careerpathmanagement.input;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TemplateInput {
    private String templateName;
    private String templateDescription;
    private Integer createdById;
    private List<CategoryInput> categories;
}

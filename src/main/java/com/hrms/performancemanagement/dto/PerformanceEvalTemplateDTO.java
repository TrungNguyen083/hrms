package com.hrms.performancemanagement.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceEvalTemplateDTO {
    TemplateDTO template;
    List<CategoryDTO> categories;

    public PerformanceEvalTemplateDTO(TemplateDTO template) {
        this.template = template;
        categories = new ArrayList<>();
    }
}

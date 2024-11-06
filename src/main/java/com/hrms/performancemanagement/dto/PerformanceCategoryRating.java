package com.hrms.performancemanagement.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceCategoryRating {
    private Integer categoryId;
    private String categoryName;
    private String categoryDescription;
    private Integer weight;
    private Float rating;
}

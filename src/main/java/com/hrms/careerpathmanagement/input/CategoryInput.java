package com.hrms.careerpathmanagement.input;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryInput {
    private String categoryName;
    private String categoryDescription;
    private Float weight;
    private List<QuestionInput> questions;
}

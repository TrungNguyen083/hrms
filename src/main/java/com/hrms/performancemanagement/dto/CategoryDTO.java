package com.hrms.performancemanagement.dto;

import com.hrms.global.models.Question;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private Integer categoryId;
    private String categoryName;
    private List<Question> questions;

    public void addQuestion(Question question) {
        this.questions.add(question);
    }
}

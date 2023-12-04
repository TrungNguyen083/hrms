package com.hrms.careerpathmanagement.input;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionInput {
    private String questionName;
    private String questionDescription;
    private Float weight;
}

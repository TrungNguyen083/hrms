package com.hrms.careerpathmanagement.input;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ICompetencySubmit {
    private Integer competencyId;
    private String comment;
    private Integer rating;
}

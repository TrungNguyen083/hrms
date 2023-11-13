package com.hrms.search.document;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GlobalSearch {
    private List<EmployeeDocument> employeeDocuments;
    private List<SkillDocument> skillDocuments;
}

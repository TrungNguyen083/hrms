package com.hrms.search.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Document(indexName = "employees")
public class EmployeeDocument {
    @Id
    private String id;

    private String index;
    private float score;

    @Field(name = "firstName")
    private String firstName;

    @Field(name = "lastName")
    private String lastName;
}

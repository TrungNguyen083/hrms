package com.hrms.search.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "performance-cycle")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceCycleDocument {
    @Id
    private String id;

    @Field(name = "performance_cycle_name", type = FieldType.Text)
    private String performanceCycleName;

    @Field(name = "description", type = FieldType.Text)
    private String description;
}
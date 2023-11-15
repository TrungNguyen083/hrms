package com.hrms.search.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "competency-cycle")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompetencyCycleDocument {
    @Id
    private String id;

    @Field(name = "competency_cycle_name", type = FieldType.Text)
    private String competencyCycleName;

    @Field(name = "description", type = FieldType.Text)
    private String description;
}
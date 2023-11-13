package com.hrms.search.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(indexName = "skills")
public class SkillDocument {
    @Id
    private String id;
    private String index;
    private float score;
    @Field(name = "skillName")
    private String skillName;
    @Field(name = "skillSetId")
    private String skillSetId;
}

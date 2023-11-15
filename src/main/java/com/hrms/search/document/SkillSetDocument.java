package com.hrms.search.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "skill-set")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SkillSetDocument {
    @Id
    private String id;

    @Field(name = "skill_set_name")
    private String skillSetName;
}

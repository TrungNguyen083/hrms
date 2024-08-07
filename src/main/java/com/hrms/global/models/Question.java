package com.hrms.global.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer id;

    @Column(name = "question_name")
    private String questionName;

    @Column(name = "question_description")
    private String questionDescription;

    @Column(name = "question_weight")
    private Float questionWeight;
}

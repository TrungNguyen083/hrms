package com.hrms.employeemanagement.documents;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document(indexName = "employee")
public class EmployeeDocument {
    @Id
    private Integer id;

    @Field(type = FieldType.Text)
    private String lastName;

    @Field(type = FieldType.Text)
    private String firstName;

    @Field(type = FieldType.Text)
    private String email;

//    @Field(type = FieldType.Date)
//    private Date joinedDate;
//
//    @Field(type = FieldType.Text)
//    private String gender;
//
//    @Field(type = FieldType.Text)
//    private String address;
//
//    @Field(type = FieldType.Text)
//    private Date dateOfBirth;
//
//    @Field(type = FieldType.Text)
//    private String phoneNumber;
//
//    @Field(type = FieldType.Text)
//    private Integer currentContract;
//
//    @Field(type = FieldType.Text)
//    private String profileBio;
//
//    @Field(type = FieldType.Text)
//    private String facebookLink;
//    @Field(type = FieldType.Text)
//    private String twitterLink;
//    @Field(type = FieldType.Text)
//    private String linkedinLink;
//    @Field(type = FieldType.Text)
//    private String instagramLink;
//    @Field(type = FieldType.Text)
//    private String department;
//    @Field(type = FieldType.Text)
//    private String position;
//    @Field(type = FieldType.Text)
//    private String jobLevel;
//    @Field(type = FieldType.Text)
//    private String status;
//    @Field(type = FieldType.Date)
//    private Date leftDate;
}

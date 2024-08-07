package com.hrms.employeemanagement.models;

import com.hrms.global.models.Department;
import com.hrms.global.models.JobLevel;
import com.hrms.global.models.Position;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document(indexName = "employee_index")
public class Employee {
	@Id
	@Column(name = "employee_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Field(type = FieldType.Text)
	@Column(name = "last_name")
	private String lastName;

	@Field(type = FieldType.Text)
	@Column(name = "first_name")
	private String firstName;

	@Column(name = "email")
	private String email;
	@Column(name = "joined_date")
	private Date joinedDate;
	@Column(name = "gender")
	private String gender;
	@Column(name = "address")
	private String address;
	@Column(name = "date_of_birth")
	private Date dateOfBirth;
	@Column(name = "phone_number")
	private String phoneNumber;
	@Column(name = "current_contract")
	private Integer currentContract;
	@Column(name = "profile_bio")
	private String profileBio;
	@Column(name = "facebook_link")
	private String facebookLink;
	@Column(name = "twitter_link")
	private String twitterLink;
	@Column(name = "linkedin_link")
	private String linkedinLink;
	@Column(name = "instagram_link")
	private String instagramLink;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	private Department department;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "position_id")
	private Position position;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_level_id")
	private JobLevel jobLevel;
	@Column(name = "status")
	private Boolean status;
	@Column(name = "left_date")
	private Date leftDate;

	// For modification time
	@Column(name = "modification_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modificationTime;

	// For insertion time
	@Column(name = "insertion_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insertionTime;
	public String getFullName() {
		return this.lastName + " " + this.firstName;
	}

	public Employee(Integer id) {
		this.id = id;
	}
}
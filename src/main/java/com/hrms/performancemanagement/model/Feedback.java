package com.hrms.performancemanagement.model;

import com.hrms.employeemanagement.models.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "feedback_request_id")
    private FeedbackRequest feedbackRequest;

    private String feedbackContent;

    private Date createdAt;
}

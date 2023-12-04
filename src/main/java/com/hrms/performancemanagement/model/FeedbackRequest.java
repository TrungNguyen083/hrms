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
public class FeedbackRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requestor_id", referencedColumnName = "employee_id")
    private Employee requester;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_receiver_id", referencedColumnName = "employee_id")
    private Employee requestReceiver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "feedback_receiver_id", referencedColumnName = "employee_id")
    private Employee feedbackReceiver;

    @ManyToOne
    @JoinColumn(name = "cycle_id", referencedColumnName = "performance_cycle_id")
    private PerformanceCycle cycle;

    private String message;

    private Date createdAt;
}

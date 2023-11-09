package com.hrms.employeemanagement.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDamInfo {
    @Id
    @Column(name = "employee_dam_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "public_id")
    private String publicId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "type")
    private String type;

    @Column(name = "extension", length = 10)
    private String extension;

    @Column(name = "uploaded_at")
    private Timestamp uploadedAt;

}


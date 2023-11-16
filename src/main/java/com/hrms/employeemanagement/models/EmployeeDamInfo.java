package com.hrms.employeemanagement.models;

import com.hrms.digitalassetmanagement.model.DamExtension;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

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

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dam_extension_id")
    private DamExtension extension;

    @Column(name = "uploaded_at")
    private Date uploadedAt;

    @Column(name = "url")
    private String url;
}
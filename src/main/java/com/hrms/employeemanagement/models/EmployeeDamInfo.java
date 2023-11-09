package com.hrms.employeemanagement.models;

import jakarta.persistence.*;
import lombok.*;

<<<<<<< HEAD
import java.sql.Timestamp;
=======
import java.util.Date;

>>>>>>> 8dd1e773209e31c3ae8778294673222599dfc142

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

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "type")
    private String type;
<<<<<<< HEAD

    @Column(name = "extension", length = 10)
    private String extension;

    @Column(name = "uploaded_at")
    private Timestamp uploadedAt;

}
=======
>>>>>>> 8dd1e773209e31c3ae8778294673222599dfc142

    @Column(name = "extension", length = 10)
    private String extension;

    @Column(name = "uploaded_at")
    private Date uploadedAt;

    @Column(name = "url")
    private String url;
}
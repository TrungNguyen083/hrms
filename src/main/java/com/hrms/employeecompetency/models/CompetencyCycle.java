package com.hrms.employeecompetency.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrms.employeemanagement.models.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompetencyCycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competency_cycle_id")
    private int id;

    @Column(name = "competency_cycle_name")
    private String competencyCycleName;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "due_date")
    private Date dueDate;
    @Column(name = "year")
    private String year;

    public String getStartDate() {
        return startDate.toString();
    }

    public String getDueDate() {
        return dueDate.toString();
    }
}
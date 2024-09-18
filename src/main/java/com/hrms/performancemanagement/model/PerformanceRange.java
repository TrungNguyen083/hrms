package com.hrms.performancemanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceRange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_range_id")
    private Integer id;

    @Column(name = "text")
    private String text;

    @Column(name = "description")
    private String description;

    @Column(name = "min_value")
    private Float minValue;

    @Column(name = "max_value")
    private Float maxValue;

    @Column(name = "ordered")
    private Integer ordered;

    public PerformanceRange(Float minValue, Float maxValue, String text) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.text = text;
    }
}

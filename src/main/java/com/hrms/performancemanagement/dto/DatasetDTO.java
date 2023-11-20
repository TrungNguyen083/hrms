package com.hrms.performancemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DatasetDTO {
    private String tag;
    private List<Float> data;

    public void addData(Float data) {
        this.data.add(data);
    }
}